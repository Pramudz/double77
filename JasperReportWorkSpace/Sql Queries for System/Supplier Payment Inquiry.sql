select sp.payment_id, sp.date_of_payment,u.user_name,sup.sup_id,sup.com_name , sd.document_type , sd.grn_ron_id ,sd.doc_amount
 from supplier_payments sp inner join supplier_payment_detail sd on sp.payment_id=sd.payment_id
inner join supplier sup on sup.sup_id=sp.supplier_id inner join users u on u.user_id=sp.user_id where sp.payment_id=2;