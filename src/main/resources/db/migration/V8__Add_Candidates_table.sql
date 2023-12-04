create table if not exists ads_candidates
(
    ad_id        bigint references ads,
    candidate_id bigint references "user"
)