create table storage (
    id serial primary key,
    file_name varchar not null,
    file_content bytea not null,
    user_id integer REFERENCES users (id)
);