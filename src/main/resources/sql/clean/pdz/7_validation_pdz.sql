with sha_union_step as (
	SELECT distinct id_sha2 FROM {dbSchema}.union_pdz where id_sha2 is not null
), sha_collision_step (
      SELECT id_sha2,
        max(case
          when error_status != 'OK' then 'md_mapping_reason'
          else null
       end) as reason_filtered
      FROM {dbSchema}.collisions_report_pdz
      where id_sha2 is not null
      group by id_sha2
), sha_clean_step (
     SELECT distinct id_sha2 FROM {dbSchema}.clean_pdz where id_sha2 is not null
), sha_eventlog_step (
      SELECT distinct id_sha2 FROM {dbSchema}.clean_pdz
      where
      code is not null and trim(code)!='' and
      name_do is not null and trim(name_do)!='' and
      eventdate is not null and trim(eventdate)!='' and
      id_sha2 is not null
)
select
    u.id_sha2 as union_step,
    isnotnull(collision.id_sha2) as collision_step,
    isnotnull(clean.id_sha2) as clean_step,
    isnotnull(el.id_sha2) as eventlog_step,
    collision.reason_filtered
from sha_union_step u
left join sha_collision_step collision
on u.id_sha2=collision.id_sha2
left join sha_clean_step clean
on u.id_sha2=clean.id_sha2
left join sha_eventlog_step el
on u.id_sha2=el.id_sha2
