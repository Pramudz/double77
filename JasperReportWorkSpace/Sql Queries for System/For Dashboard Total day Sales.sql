select sum(withoutTaxSales) as withouttaxsale from
((select sal.user_id as user, sal.sales_date as tdate,cat.cat_name as catname , prd.prd_id as prdid, prd.p_name as pname, round(sum(sal.sales_qty),1) as salesqty ,round(sum(sal.item_net_amount),2) as TotalSales ,
 round((sum(sal.discounted_price/ ((sal.item_vat+100)/100)*sal.item_vat/100 * sal.sales_qty)),2) as vat,
 round((sum(sal.item_net_amount/((100+sal.item_vat)/100))),2) as withoutTaxSales from sales_detail sal inner join products prd
on prd.prd_id=sal.product_id inner join category cat on cat.cat_id= prd.cat_id where sal.item_cancel_status=false  group by tdate,cat.cat_name ,prd.prd_id
having sum(sal.sales_qty) > 0  order by  sal.sales_date,cat.cat_name,(sal.sales_qty) desc)
union all
(select sal.user_id as user, sal.invoice_date as tdate,cat.cat_name as catname , prd.prd_id as prdid, prd.p_name as pname, round(sum(sal.sales_qty),1) as salesqty ,round(sum(sal.item_net_amount),2) as TotalSales , 
round((sum(sal.discounted_price/ ((sal.item_vat+100)/100)*sal.item_vat/100 * sal.sales_qty)),2) as vat,
round((sum(sal.item_net_amount/((100+sal.item_vat)/100))),2) as withoutTaxSales from credit_invoice_detail sal inner join products prd
on prd.prd_id=sal.product_id inner join category cat on cat.cat_id= prd.cat_id group by tdate,cat.cat_name ,prd.prd_id
having sum(sal.sales_qty) > 0  order by  sal.invoice_date,cat.cat_name,(sal.sales_qty) desc) 
union all
(select v.user_id as user,v.void_date as tdate,cat.cat_name as catname , prd.prd_id as prdid, prd.p_name as pname, round(sum(sal.sales_qty*(-1)),1) as salesqty ,round(sum(sal.item_net_amount*(-1)),2) as TotalSales , 
round((sum(sal.discounted_unit_price/ ((sal.item_vat+100)/100)*sal.item_vat/100 * sal.sales_qty)*(-1)),2) as vat,
round((sum(sal.item_net_amount/((100+sal.item_vat)/100))*(-1)),2) as withoutTaxSales from credit_invoice_void v inner join credit_invoice_void_detail sal
on v.void_id=sal.void_id inner join products prd
on prd.prd_id=sal.product_id inner join category cat on cat.cat_id= prd.cat_id  group by tdate,cat.cat_name ,prd.prd_id
having sum(sal.sales_qty) > 0  order by  v.void_date,cat.cat_name,(sal.sales_qty) desc)
union all
(select ref.user_id as user, ref.refund_date as tdate,cat.cat_name as catname , prd.prd_id as prdid, prd.p_name as pname, round(sum(sal.sales_qty*(-1)),1) as salesqty ,round(sum(sal.item_net_amount*(-1)),2) as TotalSales , 
round((sum(sal.discounted_unit_price/ ((sal.item_vat+100)/100)*sal.item_vat/100 * sal.sales_qty)*(-1)),2) as vat,
round((sum(sal.item_net_amount/((100+sal.item_vat)/100))*(-1)),2) as withoutTaxSales from customer_refunds ref inner join customer_refund_detail sal
on ref.refund_id=sal.refund_id inner join products prd
on prd.prd_id=sal.product_id inner join category cat on cat.cat_id= prd.cat_id group by tdate,cat.cat_name ,prd.prd_id
having sum(sal.sales_qty) > 0  order by  ref.refund_date,cat.cat_name,(sal.sales_qty) desc)
 ) as t where tdate between '2020-10-01' and '2020-10-31' having sum(salesqty) > 0 order by catname,salesqty ;