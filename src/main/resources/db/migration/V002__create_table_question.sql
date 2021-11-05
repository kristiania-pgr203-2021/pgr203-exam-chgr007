create table question (
    id serial primary key ,
    question varchar(200) not null,
    correct_answer varchar(250) not null
)