create table posts
(
    post_id int not null generated always as identity (start with 1) unique,
    user_id int not null,
    content varchar(5000) not null,
    create_date timestamp default now(),

    primary key (user_id, post_id),
    foreign key (user_id) references users (id)
);