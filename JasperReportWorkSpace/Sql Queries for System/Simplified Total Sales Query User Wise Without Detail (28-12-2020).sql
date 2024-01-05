select sum(withoutTaxSales) , tdate from 
((select sum(s.net_bill_amount) as withoutTaxSales, s.sales_date as tdate , s.user_id as userId from sales s where s.cancel_status=false
group by tdate, userId)
union all
(select sum(c.invoice_net_amount) as withoutTaxSales , c.invoice_date as tdate , c.user_id as userId from credit_invoice c group by tdate)
 union all
 (select sum(cref.net_refund_amount*-1) as withoutTaxSales , cref.refund_date as tdate , cref.user_id as userId from customer_refunds cref group by tdate)
union all
(select sum(crvoid.net_void_amount*-1) as withoutTaxSales, crvoid.void_date as tdate , crvoid.user_id as userId from credit_invoice_void crvoid
group by tdate)
) as t where tdate between '2020-04-01' and '2020-12-28' group by month(tdate);