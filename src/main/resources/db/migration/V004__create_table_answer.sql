create table answer (
    id serial primary key,
    question_id integer references question(id),
    answer varchar(1000) not null,
    person_id integer references person(id)
)