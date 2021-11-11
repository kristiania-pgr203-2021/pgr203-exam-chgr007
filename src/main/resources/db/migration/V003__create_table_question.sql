create type answer_type as enum ('radio', 'range', 'text');

create table question (
    id serial primary key ,
    question varchar(200) not null,
    questionnaire_id integer references questionnaire(id) not null
)