create table radio_question (
    id serial primary key,
    question_id integer references question(id),
    choice varchar(250)
)