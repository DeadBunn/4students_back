create table if not exists tags
(
    tag_id    bigint primary key generated by default as identity,
    name      varchar not null,
    use_count int default 0 check ( use_count >= 0 )
)