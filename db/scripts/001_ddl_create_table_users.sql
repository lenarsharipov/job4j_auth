create table users (
    id          serial          primary key,
    login       varchar(2000)   not null unique,
    password    varchar(2000)   not null
);