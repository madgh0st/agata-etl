WITH sha_union_step AS (
    SELECT DISTINCT id_sha2
    FROM {dbSchema}.union_counterparty_contract
    WHERE id_sha2 IS NOT NULL
),
sha_collision_step (
    SELECT id_sha2, max(CASE WHEN error_status != 'OK' THEN 'md_mapping_reason' ELSE NULL END) AS reason_filtered
    FROM {dbSchema}.collisions_report_counterparty_contract
    WHERE id_sha2 IS NOT NULL
    GROUP BY id_sha2
),
sha_clean_step (
    SELECT DISTINCT id_sha2
    FROM {dbSchema}.clean_counterparty_contract
    WHERE id_sha2 IS NOT NULL
),
sha_eventlog_step (
    SELECT DISTINCT id_sha2
    FROM {dbSchema}.clean_counterparty_contract
    WHERE code IS NOT NULL
        AND trim(code) != ''
        AND name_do IS NOT NULL
        AND trim(name_do) != ''
        AND eventdate IS NOT NULL
        AND trim(eventdate) != ''
        AND id_sha2 IS NOT NULL
)
SELECT u.id_sha2 AS union_step,
    isnotnull(collision.id_sha2) AS collision_step,
    isnotnull(clean.id_sha2) AS clean_step,
    isnotnull(el.id_sha2) AS eventlog_step,
    collision.reason_filtered
FROM sha_union_step u
    LEFT JOIN sha_collision_step collision ON u.id_sha2 = collision.id_sha2
    LEFT JOIN sha_clean_step clean ON u.id_sha2 = clean.id_sha2
    LEFT JOIN sha_eventlog_step el ON u.id_sha2 = el.id_sha2