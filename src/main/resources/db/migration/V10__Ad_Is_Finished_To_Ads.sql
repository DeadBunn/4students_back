alter table if exists ads
    add column if not exists is_finished bool not null default false