select distinct
    innRaw as inn_raw,
    nameRaw as name_raw,
    max(innClean) as inn_clean,
    max(nameClean) as name_clean,
    max(udf_normalized(nameClean)) as name_clean_norm
from {dbSchema}.counterparty_dictionary_custom_sort_do
where
    innRaw is not null and
    nameRaw is not null and
    innClean is not null and
    nameClean is not null and
    trim(innRaw) !='' and
    trim(nameRaw)  !='' and
    trim(innClean)  !='' and
    trim(nameClean)  !=''
group by innRaw, nameRaw