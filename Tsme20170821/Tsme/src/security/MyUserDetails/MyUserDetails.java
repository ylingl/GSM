package security.MyUserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class MyUserDetails implements UserDetails {
	
	private static final long serialVersionUID = 613006150663630105L;

	private String id;
	
	private String username;
	
	private String password;
	
	private List<MyProperty> myPropertyList = new LinkedList<MyProperty>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean equals(Object o){
		if(o instanceof MyUserDetails){
			if(((MyUserDetails) o).getId().equals(this.id))
				return true; 
		}
		return false;
	}
	
	@Override
	public int hashCode(){ 
		return this.id.hashCode();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();  
		for (MyProperty myProperty : this.myPropertyList) {
			String authory = myProperty.getRole();
			authorities.add(new SimpleGrantedAuthority(authory));
		}
		
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public List<MyProperty> getMyPropertyList() {
		return myPropertyList;
	}

	public void setMyPropertyList(List<MyProperty> myPropertyList) {
		this.myPropertyList = myPropertyList;
	}

}
