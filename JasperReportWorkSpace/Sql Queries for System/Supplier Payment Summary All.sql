select sp.payment_id, u.user_name ,sp.date_of_payment, sp.supplier_id , sup.com_name , sp.amount_paid
from supplier_payments sp inner join supplier sup  on sup.sup_id=sp.supplier_id
inner join users u on u.user_id=sp.user_id where sp.date_of_payment between "2021-04-01" and "2021-04-30"
order by  sup.sup_id , sp.date_of_payment;