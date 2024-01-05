select pr.revision_date as revisedDate, pr.prd_id as prdId,
prd.p_name as productName, pr.old_cost_price as wascostprice , 
case when pr.new_cost_price=pr.old_cost_price then "-" else pr.new_cost_price end as newcostprice,
pr.old_unit_price as wasunitprice,
case when pr.new_unit_price=pr.old_unit_price then "-"  else pr.new_unit_price end as newunitprice ,
u.user_name as modifiedby
from price_update pr inner join
products prd on prd.prd_id=pr.prd_id inner join users u on u.user_id=pr.user_id
where pr.revision_date between '2021-04-01' and '2021-04-30';