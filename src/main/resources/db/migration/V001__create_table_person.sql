create table person(
    id serial primary key,
    email varchar(100) not null,
    first_name varchar(100) not null,
    last_name varchar(100) not null
);