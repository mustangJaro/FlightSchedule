package com.flightSchedule.baseObjects;

public enum Role {
	VIEW_ONLY("View Only", 1),
	EDIT_SCHEDULE("Edit Schedule", 2),
	EDIT_ALL("Edit All", 6),
	ADMIN("Admin", 10)
	;

	private String roleName;
	private int id;
	
	Role(String roleName, int id){
		this.roleName = roleName;
		this.id = id;
	}
	public String getRoleName(){
		return roleName;
	}
	public int getId(){
		return id;
	}
	public static Role findEnum(String value){
		for(Role role : Role.values()){
			if(role.getRoleName().equals(value))
				return role;
		}
		return Role.VIEW_ONLY;
	}
}
