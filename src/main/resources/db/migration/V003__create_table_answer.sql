create table answer (
    id serial primary key,
    question_id integer references question(id),
    answer varchar(250) not null
)