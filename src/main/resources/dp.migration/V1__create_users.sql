create table users(
    id serial primary key,
    username varchar(20) not null,
    password varchar not null
);

insert into users (username, password)
values ('admin', '$2a$10$K8pAKm6JCkj9SYY.NVbgV.g7sbAmtrY9nz2lwxd4U8ATNRwixHIQu"');