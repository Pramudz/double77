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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name ="rolefunctions")
public class RoleFunctions implements Serializable {

	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Column(name ="func_id")
	 private int id;
	 	
	 @ManyToMany(mappedBy = "roleFunctions" , cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	 private Set<Role> rolelist;
	 
	 @Column(name ="role_function_name" , nullable =false, unique = true)
	 private String roleFunction;
	 
	 @ManyToOne(cascade = CascadeType.ALL)
	 @JoinColumn(name = "main_role_function", nullable = true, referencedColumnName = "func_id")
	 private RoleFunctions mainRoleFunction;
	 
	 @Column(name ="role_access")
	 private boolean roleAcess;
	 
	
	public RoleFunctions() {
		
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Set<Role> getRolelist() {
		return rolelist;
	}


	public void setRolelist(Set<Role> rolelist) {
		this.rolelist = rolelist;
	}


	public String getRoleFunction() {
		return roleFunction;
	}


	public void setRoleFunction(String roleFunction) {
		this.roleFunction = roleFunction;
	}


	public RoleFunctions getMainRoleFunction() {
		return mainRoleFunction;
	}


	public void setMainRoleFunction(RoleFunctions mainRoleFunction) {
		this.mainRoleFunction = mainRoleFunction;
	}


	public boolean isRoleAcess() {
		return roleAcess;
	}


	public void setRoleAcess(boolean roleAcess) {
		this.roleAcess = roleAcess;
	}


	@Override
	public String toString() {
		return "RoleFunctions [id=" + id + ", rolelist=" + rolelist + ", roleFunctions=" + roleFunction
				+ ", mainRoleFunction=" + mainRoleFunction + ", roleAcess=" + roleAcess + "]";
	}



	
}
	