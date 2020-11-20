SELECT md.code,
       do_list.name_do,
       ind.eventDate,
       ind.id,
       ind.close
FROM {dbSchema}.union_sectoral_indices AS ind
    INNER JOIN {dbSchema}.okved_sectoral_indices AS okved_ind
ON ind.id = okved_ind.sectoralIndexId
    INNER JOIN {dbSchema}.clean_affilation AS affilation
    ON instr(affilation.okved, okved_ind.okved) = 1
    INNER JOIN (SELECT DISTINCT code, inn_clean FROM {dbSchema}.md_contractor_dict WHERE inn_clean IS NOT NULL) AS md
    ON affilation.inn = md.inn_clean
    CROSS JOIN ( {union_replacer} ) do_list
