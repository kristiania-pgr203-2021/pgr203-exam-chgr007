create table range_question (
    id serial primary key,
    question_id integer references question(id) not null,
    min int not null,
    max int not null,
    min_label varchar(100),
    max_label varchar(100)
)