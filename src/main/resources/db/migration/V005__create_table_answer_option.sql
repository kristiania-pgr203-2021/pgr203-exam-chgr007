create type answer_type as enum ('radio', 'range', 'text');

create table answer_option (
    id serial primary key,
    question_id integer references question(id) not null,
    answer_type answer_type not null,
    value varchar(250),
    name varchar(250)
)