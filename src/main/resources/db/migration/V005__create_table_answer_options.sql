create table answer_option (
    id serial primary key,
    answer_id integer references answer(id) not null,
    answer_option varchar(250),
    answer_range int,
    answer_range_min_name varchar(100),
    answer_range_max_name varchar(100)
)