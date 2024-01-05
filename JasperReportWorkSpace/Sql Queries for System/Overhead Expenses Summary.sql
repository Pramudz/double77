select monthname(str_to_date(mo.month+1 ,'%m')) as monthNames , mo.year , mo.amount , oc.overhead_category from monthly_overheads mo inner join overhead_category oc on
oc.overhead_category_id=mo.overhead_category_id where mo.year = "2021" and monthname(str_to_date(mo.month+1 ,"%m")) in ("MARCH") and oc.overhead_category not in ("")
order by mo.year , mo.month;