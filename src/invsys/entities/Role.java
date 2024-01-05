package invsys.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class Role implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id")
	private int roleId;

	@ManyToMany(mappedBy = "role", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	private Set<Users> users;

	
	@Column(name = "role_name", nullable = false , unique = true)
	private String roleName;

	@Column(name = "role_desc", nullable = true)
	private String roleDesc;
	
	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@JoinTable(name="role_and_functions" , joinColumns = {
			@JoinColumn(referencedColumnName = "role_id" , nullable = false) }, inverseJoinColumns = {
					@JoinColumn(referencedColumnName  ="func_id" , nullable = false)
	})
	private Set<RoleFunctions> roleFunctions;

	public Role() {

	}

	public Role(int roleId, Set<Users> users, String roleName, String roleDesc) {
		super();
		this.roleId = roleId;
		this.users = users;
		this.roleName = roleName;
		this.roleDesc = roleDesc;
	}

	public Role(int roleId, Set<Users> users, String roleName, String roleDesc, Set<RoleFunctions> roleFunctions) {
		super();
		this.roleId = roleId;
		this.users = users;
		this.roleName = roleName;
		this.roleDesc = roleDesc;
		this.roleFunctions = roleFunctions;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public Set<Users> getUsers() {
		return users;
	}

	public void setUsers(Set<Users> users) {
		this.users = users;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public Set<RoleFunctions> getRoleFunctions() {
		return roleFunctions;
	}

	public void setRoleFunctions(Set<RoleFunctions> roleFunctions) {
		this.roleFunctions = roleFunctions;
	}

	@Override
	public String toString() {
		return "Role [roleId=" + roleId + ", users=" + users + ", roleName=" + roleName + ", roleDesc=" + roleDesc
				+ ", roleFunctions=" + roleFunctions + "]";
	}
	
	


}
