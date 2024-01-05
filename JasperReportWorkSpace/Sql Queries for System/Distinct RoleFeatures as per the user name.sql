select distinct * from  rolefunctions rf inner join 
role_and_functions rfunc on rf.func_id=rfunc.roleFunctions_func_id inner join
role r on r.role_id=rfunc.rolelist_role_id inner join user_role ur on ur.role_role_id=r.role_id inner join
users u on u.user_id=ur.users_user_id left join rolefunctions rightRoleF on rf.func_id=rightRoleF.main_role_function where
user_name= "root" and rightRoleF.func_id is null;
