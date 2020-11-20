WITH distinct_normalized_fns_disq AS (
    SELECT DISTINCT name_do,
                    fileName,
                    sheetName,
                    modificationDate,
                    loadDate,
                    organizationFullName,
                    udf_normalized(organizationFullName) AS normOrganizationFullName,
                    from_unixtime((unix_timestamp(regexp_extract(fileName, ".*data-(\\d{8})-structure", 1), 'ddMMyyyy')),'yyyy-MM-dd') as eventdate,
                    ogrn,
                    inn,
                    kpp,
                    address
    FROM {dbSchema}.fns_disq_sort_do
)
-- todo: create udf function of generating "id_sha2" using list of required fields (or all fields)
SELECT distinct_normalized_fns_disq.*,
       sha2(concat(
                    coalesce(name_do, 'null'),
                    coalesce(fileName, 'null'),
                    coalesce(sheetName, 'null'),
                    coalesce(modificationDate, 'null'),
                    coalesce(loadDate, 'null'),
                    coalesce(organizationFullName, 'null'),
                    coalesce(normOrganizationFullName, 'null'),
                    coalesce(ogrn, 'null'),
                    coalesce(inn, 'null'),
                    coalesce(kpp, 'null'),
                    coalesce(address, 'null')
                ), 256) AS id_sha2
FROM distinct_normalized_fns_disq
