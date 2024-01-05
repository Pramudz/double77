select s.com_name,c.cat_name,p.prd_id,p.p_name,p.on_h_qty,p.re_or_level,p.unit_average_cost,(p.unit_average_cost*p.on_h_qty) as averageValue
from products p inner join category c on c.cat_id=p.cat_id inner join supplier s on s.sup_id=p.sup_id 
where p.on_h_qty <= 0
order by p.on_h_qty;