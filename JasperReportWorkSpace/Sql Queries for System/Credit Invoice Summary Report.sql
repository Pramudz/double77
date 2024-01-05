select  crdMass.invoice_id, crdMass.invoice_date,u.user_name, crdMass.expired_date,crdMass.invoice_net_amount
,crdMass.invoice_discount,
case when crdMass.refund_status = true then "refunded" when crdMass.settled_status = false then 'Not Settled' else 'Settled' end as settled_status,
crdMass.settled_amount,crdMass.customer_name from credit_invoice crdMass inner join users u on u.user_id=crdMass.user_id 
where crdMass.invoice_date between "2020-01-01"   and   "2021-04-29"
group by crdMass.invoice_id, crdMass.user_id, crdMass.invoice_date order by crdMass.invoice_date, crdMass.invoice_id, crdMass.user_id;