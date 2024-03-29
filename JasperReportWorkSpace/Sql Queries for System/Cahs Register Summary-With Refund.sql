select p.pay_mode, p.sub_pay_mode, sum(cusref.net_refund_amount) as samedayposrefund, (select sum(alref.net_refund_amount) from customer_refunds alref where alref.refund_date='2024-01-09') as allrefund,
case when p.pay_mode = 'cash' then sum(p.amount-s.balance) else sum(p.amount) end as value, c.opening_cash_balance, c.closingCashBalance, c.user_name ,c.registry_date 
from pos_pay_detail p inner join sales s on 
s.sales_date=p.sales_date and s.user_id=p.user_id and s.bill_no=p.bill_no 
inner join cash_register c on c.user_id=s.user_id and c.registry_date = p.sales_date inner join customer_refunds cusref on cusref.bill_no=s.bill_no and cusref.user_id=s.user_id and cusref.sales_date=s.sales_date
where s.cancel_status=false and p.sales_date ='2024-01-09' and p.user_id=1 group by p.sub_pay_mode