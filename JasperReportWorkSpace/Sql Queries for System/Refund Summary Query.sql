select refmass.refund_id as refundId, refmass.refund_date as refundDate, refusr.user_name as refundedBy ,
refmass.bill_no as posBillNo , refmass.sales_date as posSalesdate
, posuser.user_name as poscashier , refmass.gross_refund_amount as grossrefAmount , refmass.net_refund_amount as netrefundamount ,
refmass.refund_discount_from_bill as discountfrompossbill
from 
customer_refunds refmass inner join users refusr on 
refmass.refunded_by=refusr.user_id inner join users posuser on refmass.user_id=posuser.user_id;
