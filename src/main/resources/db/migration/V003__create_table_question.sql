create type answer_type as enum ('radio', 'range', 'text');

create table question (
    id serial primary key ,
    question varchar(500) not null,
    questionnaire_id integer references questionnaire(id),
    answer_type answer_type not null
)