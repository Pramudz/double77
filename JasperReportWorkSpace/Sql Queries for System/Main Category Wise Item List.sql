select maincat.cat_name as maincat , subcat.cat_name ,prd.p_name,prd.unit_average_cost, prd.on_h_qty
from products prd inner join category subcat on subcat.cat_id=prd.cat_id right join category
maincat on maincat.cat_id=subcat.main_category  where subcat.main_category is not null;