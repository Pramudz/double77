select refmass.refund_id as refundId, refmass.refund_date as refundDate, refusr.user_name as refundedBy ,
refmass.bill_no as posBillNo , refmass.sales_date as posSalesdate
, posuser.user_name as poscashier , refdet.seq_no as seqNo , prd.prd_id as prdId , prd.p_name as prdName ,
refdet.unit_price as unitprice , refdet.refund_qty as refundqty , refdet.gross_item_amount as grossItemAmount, 
refdet.discount_percentage as discountPercentage , refdet.item_net_amount as netitemAmount , refdet.discount as discountvalue
from 
customer_refunds refmass inner join customer_refund_detail refdet on refmass.refund_id=refdet.refund_id
inner join products prd on prd.prd_id=refdet.product_id inner join users refusr on 
refmass.refunded_by=refusr.user_id inner join users posuser on refmass.user_id=posuser.user_id where refmass.refund_id=4 order by refdet.seq_no;
