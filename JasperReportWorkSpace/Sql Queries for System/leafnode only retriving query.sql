select distinct rl.func_id as functionId , rl.role_function_name as functionName , rl.main_role_function  as parentId,
rr.func_id as leafId , rr.role_function_name as leafName from rolefunctions rl left join rolefunctions rr on rl.func_id=rr.main_role_function
where rr.func_id is null order by rl.main_role_function;