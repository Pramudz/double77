select sm.sup_return_id,u.user_name,sm.return_date,sm.sup_id,sup.com_name, sd.seqNo, sd.prd_id,prd.p_name,sd.prd_cost,
sd.prd_amount,sd.reason, sd.return_qty, case when sm.payment_utilized_status = true then "Utilized" else "to be Utilized" end as paymentutilizedstat
from supplier_return_mass sm inner join supplier_return_det sd on sm.sup_return_id=sd.sup_return_id
inner join users u on u.user_id=sm.user_id inner join supplier sup on sup.sup_id=sm.sup_id inner join products prd on prd.prd_id=sd.prd_id
where sm.sup_return_id=1 order by sd.seqNo;