  create table text_question (
      id serial primary key,
      question_id integer references question(id) not null,
      max_chars integer default 100 not null,
      placeholder varchar(100) default 'please write your answer here'
  );