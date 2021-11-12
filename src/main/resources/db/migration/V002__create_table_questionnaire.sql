create table questionnaire
(
    id        serial primary key,
    name      varchar(100)                   not null,
    person_id integer references person(id)
);