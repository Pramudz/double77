select 'POSS' as salemode,s.sales_date as salesdate,u.user_name as username , s.bill_no as billno ,
sum(s.gross_bill_amount) as grossales,
sum(s.discount) as discountvalue,
sum(s.net_bill_amount)  as netsales,
case s.cancel_status when false then 'active' when true then'cancel' end as status
from sales s left join users u on u.user_id=s.user_id where s.sales_date 
between '2020-12-01' and '2020-12-30' and s.cancel_status=false group by salesdate,username,billno;