select s_mass.bill_no as billNo, s_mass.sales_date as salesDate , u.user_name as userName, cus.first_name as customerFname , cus.telephone as customerTel , cus.company_name as customerCompanyName,
s_det.seq_no as seqNo , prd.prd_id as prdId, prd.p_name as prdName , s_det.unit_price as unitPrice, s_det.sales_qty as salesQty , s_det.gross_item_amount as itemGrossAmount,
s_det.discount_percentage as discountPercentage , s_det.item_net_amount as itemNetAmount, s_det.item_cancel_status as itemCancelStatus , s_mass.balance as balance
from sales s_mass inner join sales_detail s_det on s_det.bill_no=s_mass.bill_no and s_det.sales_date=s_mass.sales_date and s_det.user_id=s_mass.user_id
inner join users u on u.user_id=s_mass.user_id inner join products prd on prd.prd_id=s_det.product_id inner join customer cus on cus.customer_id=s_mass.customerId where s_mass.cancel_status=false and u.user_name=$P{userName}
and s_mass.bill_no=$P{billNo} and s_mass.sales_date=$P{date} order by seqNo;