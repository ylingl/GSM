package logic.account;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import mvc.account.AccountInfo;
import tsme.table.account.DAO.AccountDAO;
import tsme.table.account.bean.ACCOUNT;
import tsme.table.accountProperty.DAO.AccountPropertyDAO;
import tsme.table.accountProperty.bean.ACCOUNTPROPERTY;
import tsme.table.accountRegister.DAO.AccountRegisterDAO;
import tsme.table.accountRegister.bean.ACCOUNTREGISTER;
import tsme.table.accountRetreive.DAO.AccountRetrieveDAO;
import tsme.table.accountRetreive.bean.ACCOUNTRETRIEVE;
import tsme.table.trainerAudit.DAO.TrainerAuditDAO;
import tsme.table.trainerAudit.bean.TRAINERAUDIT;
import utils.AccountTools;
import utils.RandomStampTools;

@Service("accountService")
public class AccountServiceImpl implements AccountService {

	@Autowired
	@Qualifier("accountDAO")
	private AccountDAO accountDAO;
	
	@Autowired
	@Qualifier("accountRegisterDAO")
	private AccountRegisterDAO accountRegisterDAO;
	
	@Autowired
	@Qualifier("accountRetrieveDAO")
	private AccountRetrieveDAO accountRetrieveDAO;
	
	@Autowired
	@Qualifier("trainerAuditDAO")
	private TrainerAuditDAO trainerAuditDAO;
	
	@Autowired
	@Qualifier("accountPropertyDAO")
	private AccountPropertyDAO accountPropertyDAO;
	
	public boolean isUserNameExisted(String username) {
		String sql = "SELECT id FROM account WHERE username='" + username + "'";
		
		Long num = accountDAO.getTotalItemsNumBySelectQuery(sql);
		
		return num > 0 ? true : false;
	}
	
	public String saveRegisterInfoByEmail(String email){
		ACCOUNTREGISTER accountRegister = createRegisterWithApplyId(email);
		
		RandomStampTools randomStampTools = new RandomStampTools();
		do{
			accountRegister.setStamp(randomStampTools.getRandomString(15));
		}
		
		while(accountRegisterDAO.findAccountRegisterByStamp(accountRegister.getStamp()) != null);
		
		accountRegisterDAO.save(accountRegister);
		
		return accountRegister.getStamp();
	}
	
	private ACCOUNTRETRIEVE createRetrieveWithApplyId(String applyId){
		ACCOUNTRETRIEVE accountRetrieve = new ACCOUNTRETRIEVE();
		
		accountRetrieve.setApply_id(applyId);
		accountRetrieve.setRetrieve_date(new Date());
		accountRetrieve.setRetrieve_done(false);
		accountRetrieve.setRetrieve_done_date(null);
		
		return accountRetrieve;
	}
	
	public String saveRetrieveInfoByEmail(String email){
		
		ACCOUNTRETRIEVE accountRetrieve = createRetrieveWithApplyId(email);
		
		RandomStampTools randomStampTools = new RandomStampTools();
		do{
			accountRetrieve.setStamp(randomStampTools.getRandomString(15));
		}
		while(accountRetrieveDAO.save(accountRetrieve) == 0);
		
		return accountRetrieve.getStamp();
	}
	
	private ACCOUNTREGISTER createRegisterWithApplyId(String applyId){
		
		ACCOUNTREGISTER accountRegister = new ACCOUNTREGISTER();
		
		accountRegister.setApply_id(applyId);
		accountRegister.setReg_date(new Date());
		accountRegister.setReg_done(false);
		accountRegister.setReg_done_date(null);
		
		return accountRegister;
	}
	
	public String validateEmailRegisterByStamp(String stamp){
		ACCOUNTREGISTER accountRegister =  accountRegisterDAO.findAccountRegisterByStamp(stamp);
		
		if(accountRegister == null)
			return null;
		if (accountRegister.isReg_done())
			return null;
		
		Date currentDate = new Date();
		
		if(currentDate.getTime() - accountRegister.getReg_date().getTime() < 24*60*60*1000)
			return accountRegister.getApply_id();
		
		return null;
	}
	
	public String validateRetrieveInfo(String stamp){
		ACCOUNTRETRIEVE accountRetrieve =  accountRetrieveDAO.findAccountRetrieveByStamp(stamp);
		
		if(accountRetrieve == null)
			return null;
		if(accountRetrieve.isRetrieve_done())
			return null;
		
		Date currentDate = new Date();
		
		if(!accountRetrieve.getApply_id().contains("@")){
			if(currentDate.getTime() - accountRetrieve.getRetrieve_date().getTime() < 10*60*1000)
				return accountRetrieve.getApply_id();
		} else if (currentDate.getTime() - accountRetrieve.getRetrieve_date().getTime() < 24*60*60*1000){
			return accountRetrieve.getApply_id();
		}
		
		return null;
	}
	
	public boolean register(String stamp, String applyId, String password){
		ACCOUNTREGISTER accountRegister = accountRegisterDAO.findAccountRegisterByStampAndApplyId(stamp, applyId);
		
		if (accountRegister == null)
			return false;
		
		if (accountDAO.findByUsername(applyId) != null)
			return false;
		
		if (accountRegister.isReg_done())
			return false;
		
		if (!applyId.contains("@")){
			if (accountRegister.getReg_date().getTime() > new Date().getTime() + 10*60*1000)
				return false;
		}
		else if (accountRegister.getReg_date().getTime() > new Date().getTime() + 24*60*60*1000)
				return false;
		
		accountRegister.setReg_done(true);
		accountRegister.setReg_done_date(new Timestamp(Calendar.getInstance().getTime().getTime()));
		accountRegisterDAO.update(accountRegister);
		
		return accountDAO.saveAccountWithApplyId(applyId, password);
	}
	
	public String findIdByUsername(String username) {
		ACCOUNT account = accountDAO.findByUsername(username);
		String accountId = "";
		if(account != null){
			accountId = account.getId();
		}
		return accountId;
	}
	
	public ACCOUNT findBothById(String accountId){
		return accountDAO.findBothById(accountId);
	}
	
	public boolean ResetUserPassword(String aId, String password){
		ACCOUNT account = accountDAO.findActivatedById(aId);
		
		account.setPassword(password);
		
		if (accountDAO.update(account) > 0)
			return true;
		else
			return false;
	}
	
	public boolean retrieveCompleted(String stamp){
		ACCOUNTRETRIEVE accountRetrieve = accountRetrieveDAO.findAccountRetrieveByStamp(stamp);
		
		if (accountRetrieve == null)
			return false;
		if (accountRetrieve.isRetrieve_done())
			return false;
		
		accountRetrieve.setRetrieve_done(true);
		accountRetrieve.setRetrieve_done_date(new Timestamp(Calendar.getInstance().getTime().getTime()));
		
		if (accountRetrieveDAO.update(accountRetrieve) > 0)
			return true;
		else
			return false;
	}
	
	public boolean audit(String id, String state, String auditor_opinions, String auditor_id) {
		TRAINERAUDIT trainerAudit = trainerAuditDAO.findActivatedById(id);
		
		if(trainerAudit == null)
			return true;
		
		if(state.equals("pass")){
			auditor_opinions = "同意";
		}
		
		trainerAudit.setAudit_opinions(auditor_opinions);
		trainerAudit.setAuditor_id(auditor_id);
		trainerAudit.setAuditor_name(accountDAO.findActivatedById(auditor_id).getReal_name());
		trainerAudit.setAudit_date(new Date());
		trainerAudit.setState(state);
		
		trainerAuditDAO.update(trainerAudit);
		
		if(state.equals("pass")){
			String sql = "UPDATE account SET real_name='" + trainerAudit.getReal_name() + 
						 "', phone_num='" + trainerAudit.getPhone_num() + "' WHERE id='" + trainerAudit.getAccount_id() + "'";
			
			accountDAO.executeBySql(sql);
			
			ACCOUNTPROPERTY accountProperty = new ACCOUNTPROPERTY();
			
			accountProperty.setAccount_id(trainerAudit.getAccount_id());
			accountProperty.setActive(true);
			accountProperty.setCreate_time(System.currentTimeMillis());
			accountProperty.setRole_code("ROLE_TRAINER");
			
			accountPropertyDAO.save(accountProperty);
		}
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean canThisAccountApplyForTrainer(String accountId){
		String sql = "SELECT create_time FROM trainerAudit WHERE state='waiting' AND active=1 AND account_id='" + accountId + "'";
		List<Long> createTimeList = (List<Long>) trainerAuditDAO.findByQueryForList(sql, Long.class);
		AccountTools accountTools = new AccountTools();
		if(accountTools.doesCurrentAccountHasTesterRole() && (createTimeList.isEmpty() || System.currentTimeMillis() - createTimeList.get(0) > 24 * 60 * 60 * 1000)){//超过1天
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean applyForTrainer(String accountId, String realName, String phoneNum){
		String sql = "DELETE FROM trainerAudit WHERE account_id='" + accountId + "' AND state='waiting'";
		trainerAuditDAO.executeBySql(sql);
		
		TRAINERAUDIT trainerAudit = new TRAINERAUDIT();
		
		trainerAudit.setAccount_id(accountId);
		trainerAudit.setReal_name(realName);
		trainerAudit.setPhone_num(phoneNum);
		trainerAudit.setActive(true);
		trainerAudit.setCreate_date(new Date());
		trainerAudit.setCreate_time(System.currentTimeMillis());
		trainerAudit.setState("waiting");
		
		trainerAuditDAO.save(trainerAudit);
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TRAINERAUDIT> findAccountByState(String state){
		String sql = "SELECT * FROM trainerAudit WHERE state='" + state + "' AND active=1";
		return (List<TRAINERAUDIT>) trainerAuditDAO.findByQuery(sql, TRAINERAUDIT.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AccountInfo> getAllAccountInfo(){
		AccountTools accountTools = new AccountTools();
		String sql = "";
		
		if(accountTools.doesCurrentAccountHasSuperadminRole()) {
			sql = "SELECT * FROM account";
		} else if(accountTools.doesCurrentAccountHasAdminRole()) {
			sql = "SELECT * FROM account WHERE department_id IN ( SELECT department_id FROM account WHERE id='" + accountTools.getCurrentAccountId() + "' )";
		}
		
		List<AccountInfo> accountInfoList = (List<AccountInfo>) accountDAO.findByQuery(sql, AccountInfo.class);
		
		for(AccountInfo accountInfo : accountInfoList) {
			sql = "SELECT role_code FROM accountProperty WHERE account_id='" + accountInfo.getId() + "'";
			List<String> roleCodeList = (List<String>) accountPropertyDAO.findByQueryForList(sql, String.class);
			String roles = "";
			for(String roleCode : roleCodeList){
				if(roleCode.equalsIgnoreCase("ROLE_SUPERADMIN")){
					roles = roles + "超级管理员 ";
					continue;
				}
				if(roleCode.equalsIgnoreCase("ROLE_ADMIN")){
					roles = roles + "管理员 ";
					continue;
				}
				if(roleCode.equalsIgnoreCase("ROLE_TRAINER")){
					roles = roles + "训练员 ";
					continue;
				}
				if(roleCode.equalsIgnoreCase("ROLE_TESTER")){
					roles = roles + "检测员 ";
					continue;
				}
			}
			accountInfo.setRoles(roles);
		}
		
		return accountInfoList;
	}
	
	@Override
	public boolean setAccountActiveAttribute(String accountId, boolean active){
		String sql = "UPDATE account SET active=" + (active ? "1" : "0") + " WHERE id='" + accountId + "'";
		
		accountDAO.executeBySql(sql);
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAccountRoleList(String accountId){
		String sql = "SELECT role_code FROM accountProperty WHERE account_id='" + accountId + "' ORDER BY create_time ASC";
		return (List<String>) accountPropertyDAO.findByQueryForList(sql, String.class);
	}
	
	@Override
	public boolean modifyAccountProperty(String accountId, String roleList) {
		String[] roles = roleList.split(",");
		
		if(roles[0].contains("ROLE_")){
			List<ACCOUNTPROPERTY> accountPropertyList = new ArrayList<ACCOUNTPROPERTY>();
			for(String role : roles){
				ACCOUNTPROPERTY accountProperty = new ACCOUNTPROPERTY();
				
				accountProperty.setAccount_id(accountId);
				accountProperty.setActive(true);
				accountProperty.setCreate_time(System.currentTimeMillis());
				accountProperty.setRole_code(role);
				
				accountPropertyList.add(accountProperty);
			}
			String sql = "DELETE FROM accountProperty WHERE account_id='" + accountId + "'";
			
			accountPropertyDAO.executeBySql(sql);
			accountPropertyDAO.batchSave(accountPropertyList);
		}
		
		return true;
	}
}
