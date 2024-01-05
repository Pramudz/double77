select c.cat_id as catId, c.cat_name as categoryName, count(p.prd_id) as prdCount from products p inner join 
category c on c.cat_id=p.cat_id group by categoryName order by prdCount desc;