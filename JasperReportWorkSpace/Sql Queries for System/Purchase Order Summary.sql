select po.order_id,po.order_date,po.expire_date,po.order_amount,s.com_name,u.user_name,
case when po.delivery_status=true then "Delivered" when po.partialy_del_status=true then "Partially" else "Not Delivered" end
as delstatus , case when po.delivery_status=true then "-" when po.expire_date < "2021-04-25" then "Expired" else "Active" end as expiredstatus
 from purchase_order po inner join supplier s on po.sup_id=s.sup_id
inner join users u on u.user_id=po.user_id where po.order_date between "2021-04-01" and "2021-04-30";