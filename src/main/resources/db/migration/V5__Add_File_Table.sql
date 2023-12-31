create table if not exists files
(
    file_id bigint primary key generated by default as identity,
    name    varchar not null,
    file_path varchar not null
);

create table if not exists ads_files
(
    file_id bigint references files,
    ad_id   bigint references ads
)