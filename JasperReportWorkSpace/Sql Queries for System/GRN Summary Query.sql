select g.grn_id, g.grn_amount, g.grn_date,g.invoice_no
,g.order_id, s.com_name, u.user_name ,
case when g.paid_status=true then "Paid" else "Not Paid" end as paidstatus from good_received_mass g inner join supplier s on g.sup_id=s.sup_id
inner join users u on u.user_id=g.user_id where g.grn_date between "2021-04-03" and "2021-04-30";