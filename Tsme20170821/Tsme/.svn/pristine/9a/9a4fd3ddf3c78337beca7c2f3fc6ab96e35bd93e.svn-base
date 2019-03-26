package mvc.security.role;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class RecRole {
	
	/**
	 * 
	 */
	@NotEmpty
	@Length(max=50)
	private String role_name;
	
	/**
	 * 
	 */
	@NotEmpty
	@Length(max=50)
	private String role_code;

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}

	public String getRole_code() {
		return role_code;
	}

	public void setRole_code(String role_code) {
		this.role_code = role_code;
	}
}
