create table range_question (
    id serial primary key,
    question_id integer references question(id) not null,
    range int not null,
    name_min_val varchar(100),
    name_max_val varchar(100)
)