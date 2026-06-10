CREATE TABLE IF NOT EXISTS post_labels
(
    post_id  BIGINT NOT NULL REFERENCES posts (id),
    label_id BIGINT NOT NULL REFERENCES labels (id),
    PRIMARY KEY (post_id, label_id)
);
