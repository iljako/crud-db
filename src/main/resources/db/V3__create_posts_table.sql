DO
$$
    BEGIN
        CREATE TYPE post_status AS ENUM ('ACTIVE', 'UNDER_REVIEW', 'DELETED');
    EXCEPTION
        WHEN duplicate_object THEN NULL;
    END
$$;

CREATE TABLE IF NOT EXISTS posts
(
    id        BIGSERIAL   PRIMARY KEY,
    content   TEXT        NOT NULL,
    created   TIMESTAMP   NOT NULL,
    updated   TIMESTAMP   NOT NULL,
    status    post_status NOT NULL,
    writer_id BIGINT      NOT NULL REFERENCES writers (id)
);
