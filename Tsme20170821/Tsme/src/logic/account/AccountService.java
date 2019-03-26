package logic.account;

import java.util.List;

import mvc.account.AccountInfo;
import tsme.table.account.bean.ACCOUNT;
import tsme.table.trainerAudit.bean.TRAINERAUDIT;

public interface AccountService {

	public boolean isUserNameExisted(String username);
	
	public String saveRegisterInfoByEmail(String email);
	public String saveRetrieveInfoByEmail(String email);
	
	public String validateEmailRegisterByStamp(String stamp);
	public String validateRetrieveInfo(String stamp);
	
	public boolean register(String stamp, String applyId, String password);
	
	public String findIdByUsername(String username);
	public ACCOUNT findBothById(String accountId);
	
	public boolean ResetUserPassword(String aId, String password);
	
	public boolean retrieveCompleted(String stamp);
	
	public boolean audit(String id, String state, String auditor_opinions, String auditor_id);
	
	public boolean canThisAccountApplyForTrainer(String accountId);
	
	public boolean applyForTrainer(String accountId, String realName, String phoneNum);
	
	public List<TRAINERAUDIT> findAccountByState(String state);
	
	public List<AccountInfo> getAllAccountInfo();
	
	public boolean setAccountActiveAttribute(String accountId, boolean active);
	
	public List<String> getAccountRoleList(String accountId);
	
	public boolean modifyAccountProperty(String accountId, String roleList);
}
