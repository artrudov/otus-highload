create type Role as enum ('USER', 'ADMIN');

create table if not exists users
(
    id          bigint generated always as identity (start with 1) unique primary key,
    username    varchar(50)  not null unique,
    password    varchar(64)  not null,
    role        Role default 'USER',
    enabled     boolean default false,
    first_name  varchar(250) not null,
    second_name varchar(250) not null,
    birthdate   date         not null,
    biography   varchar(1500),
    city        varchar(100) not null
);