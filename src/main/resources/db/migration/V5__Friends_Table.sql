create table friends
(
    user_id    int not null,
    friend_id  int not null,

    primary key (user_id, friend_id),
    foreign key (user_id) references users (id),
    foreign key (friend_id) references users (id)
);