select  crdDet.seq_no, crdMass.invoice_id, crdMass.invoice_date,u.user_name,crdDet.product_id,b.p_name,crdDet.unit_price,crdDet.sales_qty,
crdDet.discount_percentage,crdDet.item_net_amount,crdDet.gross_item_amount,crdMass.settled_amount, crdMass.customer_id, crdMass.customer_name,
case when crdMass.refund_status = false then "N/A" else 'refunded' end as refundStatus,
case when crdMass.refund_status = true then "N/A" when crdMass.settled_status=false then "Not settled" else "Settled" end as settled_status,
crdMass.if_advance_payment,
case when crdMass.refund_status = true then "N/A" when crdMass.settled_date is null then "To be Settled" else crdMass.settled_date end as settledDate ,
crdMass.credit_period , crdMass.expired_date , crdDet.discount
from credit_invoice crdMass 
inner join credit_invoice_detail crdDet on crdMass.invoice_id=crdDet.invoice_id and crdMass.invoice_date=crdDet.invoice_date
and crdMass.user_id=crdDet.user_id 
inner join users u on u.user_id=crdMass.user_id 
inner join products b on crdDet.product_id = b.prd_id
where u.user_name = "chathuri"  and crdMass.invoice_date = "2021-04-05"  and crdMass.invoice_id = 1 order by crdDet.seq_no ;