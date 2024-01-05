select maincat.cat_name as maincat , sum(prd.on_h_qty) as onhQty, sum(prd.unit_average_cost*prd.on_h_qty) as stockvalue , count(prd.prd_id) as prdCount 
from products prd inner join category subcat on subcat.cat_id=prd.cat_id right join category
maincat on maincat.cat_id=subcat.main_category  where subcat.main_category is not null group by maincat.cat_id;