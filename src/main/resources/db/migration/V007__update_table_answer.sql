create type answer_type as enum ('multiple_choice', 'range', 'normal');
alter table answer
add column answer_type answer_type;