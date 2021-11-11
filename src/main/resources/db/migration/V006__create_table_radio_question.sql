create table radio_question (
    id serial primary key,
    question_id integer references question(id) not null,
    choice varchar(250)
)