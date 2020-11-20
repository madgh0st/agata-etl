SELECT r.blockkbe,r.comments,r.contractname,r.counterpartyinn,r.counterpartyname,r.currentdebt,r.filename,
       r.groupdo,r.loaddate,r.modificationdate,r.num,r.ondate,r.overduedebt,r.overduedebtbetween5and30days,
       r.overduedebtless5days,r.overduedebtmore30days,null as daysOverdue,r.sheetname,r.totaldebtwithoutreserve
FROM (
{union_replacer}
) r
UNION
SELECT blockkbe,comments,null as contractname,counterpartyinn,counterpartyname,null as currentdebt,filename,groupdo,loaddate,modificationdate,num,ondate, printf('%.7f',cast((coalesce(cast(overduedebtless5days as double),0) + coalesce(cast(overduedebtbetween5and30days as double),0)) as double)) as overduedebt,overduedebtbetween5and30days,overduedebtless5days,null as overduedebtmore30days,daysOverdue,sheetname,totaldebtwithoutreserve
FROM {dbSchema}.weekly_status_pdz_mb_mb
