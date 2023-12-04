alter table if exists ads
    add column if not exists executor_id bigint references "user"