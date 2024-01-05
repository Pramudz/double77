select maincatname, sum(withoutTaxsale)  , sum(costOfSales) , sum(withoutTaxSale-costOfSales) as grossprofit , sum(withoutTaxSale-costOfSales)/sum(withoutTaxSale)
 as gp from
((select sal.sales_date as tdate, cat.cat_name as subcatname , mcat.cat_name as maincatname,
round((sum(sal.item_net_amount/((100+sal.item_vat)/100))),2) as withoutTaxSale,
round(sum(sal.sales_qty * sal.cost_price)/((100+sal.item_vat)/100),2) as costOfSales 
from sales_detail sal inner join 
products prd on prd.prd_id=sal.product_id
inner join category cat on cat.cat_id=prd.cat_id right join 
category mcat on mcat.cat_id=cat.main_category where cat.main_category is not null and sal.item_cancel_status=false group by tdate, maincatname, subcatname)
union all
(select sal.invoice_date as tdate , cat.cat_name as subcatname , mcat.cat_name as maincatname,
round((sum(sal.item_net_amount/((100+sal.item_vat)/100))),2) as withoutTaxsale,
round(sum(sal.sales_qty * sal.cost_price)/((100+sal.item_vat)/100),2) as costOfSales from 
credit_invoice_detail sal inner join 
products prd on prd.prd_id=sal.product_id
inner join category cat on cat.cat_id=prd.cat_id right join 
category mcat on mcat.cat_id=cat.main_category where cat.main_category is not null group by tdate,maincatname, subcatname)
union all
(select crMain.refund_date as tdate,cat.cat_name as subcatname , mcat.cat_name as maincatname,
round((sum(crDet.item_net_amount/((100+crDet.item_vat)/100))*-1),2) as withoutTaxsale ,
round(sum(crDet.sales_qty * crDet.cost_price)/((100+crDet.item_vat)/100)*-1,2) as costOfSales
from customer_refunds crMain inner join customer_refund_detail crDet
on crMain.refund_id=crDet.refund_id inner join products prd on crDet.product_id=prd.prd_id 
inner join category cat on cat.cat_id=prd.cat_id right join 
category mcat on mcat.cat_id=cat.main_category where cat.main_category is not null group by tdate,maincatname, subcatname)
union all
(select crMain.void_date as tdate,cat.cat_name as subcatname , mcat.cat_name as maincatname,
round((sum(crDet.item_net_amount/((100+crDet.item_vat)/100))*-1),2) as withoutTaxsale ,
round(sum(crDet.sales_qty * crDet.cost_price)/((100+crDet.item_vat)/100)*-1,2) as costOfSales
from credit_invoice_void crMain inner join credit_invoice_void_detail crDet
on crMain.void_id=crDet.void_id inner join products prd on crDet.product_id=prd.prd_id 
inner join category cat on cat.cat_id=prd.cat_id right join 
category mcat on mcat.cat_id=cat.main_category where cat.main_category is not null group by tdate,maincatname, subcatname)
) as t where tdate between "2021-04-01"  and  "2021-04-30" group by maincatname,subcatname;