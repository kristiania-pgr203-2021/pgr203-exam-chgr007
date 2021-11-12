create table person(
    id serial primary key,
    email varchar(100) unique not null,
    first_name varchar(100) not null,
    last_name varchar(100) not null,
    password varchar(100) not null
);