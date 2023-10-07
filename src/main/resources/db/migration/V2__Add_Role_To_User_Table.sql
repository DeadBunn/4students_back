alter table "user"
    add column if not exists "role" varchar not null default 'USER';

update "user" set role='ADMIN' where user_id = 2