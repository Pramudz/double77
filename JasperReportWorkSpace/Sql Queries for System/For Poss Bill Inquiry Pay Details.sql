select paydet.pay_mode , paydet.sub_pay_mode , paydet.amount from sales s_mass inner join
pos_pay_detail paydet on paydet.bill_no=s_mass.bill_no and paydet.sales_date=s_mass.sales_date and paydet.user_id=s_mass.user_id 
inner join users u on u.user_id=s_mass.user_id where s_mass.cancel_status=false and u.user_name="root"
and s_mass.bill_no=8 and s_mass.sales_date='2020-11-15';