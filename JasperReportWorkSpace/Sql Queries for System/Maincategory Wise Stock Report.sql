select maincat.cat_name , 
case when  sum(p.on_h_qty*p.unit_average_cost) is null 
then 0 else sum(p.on_h_qty*p.unit_average_cost) end as stockvalue , 
case when sum(p.on_h_qty) is null then 0 else sum(p.on_h_qty) end as stockqty , count(p.prd_id) as prdCount
from products p right join category subcat on subcat.cat_id=p.cat_id inner join category maincat on maincat.cat_id=subcat.main_category group by maincat.cat_id;
