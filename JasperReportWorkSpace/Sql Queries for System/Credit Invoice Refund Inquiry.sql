select refmass.void_id as refundId, refmass.void_date as refundDate, refusr.user_name as refundedBy ,
refmass.invoice_id as invoiceId , refmass.invoice_date as invoiceDate
, posuser.user_name as poscashier , refdet.seq_no as seqNo , prd.prd_id as prdId , prd.p_name as prdName ,
refdet.unit_price as unitprice , refdet.sales_qty as refundqty , refdet.gross_item_amount as grossItemAmount, 
refdet.discount_percentage as discountPercentage , refdet.item_net_amount as netitemAmount , refdet.discount as discountvalue
from 
credit_invoice_void refmass inner join credit_invoice_void_detail refdet on refmass.void_id=refdet.void_id
inner join products prd on prd.prd_id=refdet.product_id inner join users refusr on 
refmass.voided_by=refusr.user_id inner join users posuser on refmass.user_id=posuser.user_id where refmass.void_id=1 order by refdet.seq_no;
