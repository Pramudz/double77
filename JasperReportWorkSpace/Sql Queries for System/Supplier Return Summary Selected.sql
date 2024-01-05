select sr.sup_return_id,sr.return_date,u.user_name,sup.com_name,sr.sup_id,sr.return_amount,
case when sr.payment_utilized_status=true then "Utilized" else "Not Utilized" end as paymentUtilizedStatus
 from supplier_return_mass sr inner join supplier sup on sr.sup_id=sup.sup_id
inner join users u on u.user_id=sr.user_id where sr.return_date between "2021-04-01" and "2021-04-30"
and sup.com_name in ("Migara International") order by sr.sup_id,sr.return_date;