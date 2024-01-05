select r1.role_function_name  as level1 , r2.role_function_name  as level2 ,r3.role_function_name  as level3 , r4.role_function_name  as level4
from rolefunctions r1  
left join rolefunctions r2 on r1.func_id=r2.main_role_function
left join rolefunctions r3 on r2.func_id=r3.main_role_function
left join rolefunctions r4 on r3.func_id=r4.main_role_function  order by level1,level2,level3,level4 ;