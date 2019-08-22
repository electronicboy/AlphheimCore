create table player_data
(
    PLAYER_ID        int auto_increment
        primary key,
    PLAYER_UUID      char(36)    not null,
    PLAYER_LAST_NAME varchar(16) null,
    constraint player_data_uuid_index
        unique (PLAYER_UUID)
)
    charset = latin1;

create table cooldowns
(
    PLAYER_ID int         not null,
    NAME      varchar(32) not null,
    EXPIRY    mediumtext  not null,
    primary key (PLAYER_ID, NAME),
    constraint COOLDOWN_FK_PLAYER_ID
        foreign key (PLAYER_ID) references player_data (PLAYER_ID)
            on delete cascade
)
    charset = latin1;

create table player_chests
(
    PLAYER_ID  int                  not null,
    CHEST_NAME varchar(32)          not null,
    CONTENTS   text                 not null,
    OPEN       tinyint(1) default 0 not null,
    primary key (PLAYER_ID, CHEST_NAME),
    constraint FK_PLAYER_CHESTS_PLAYER_ID
        foreign key (PLAYER_ID) references player_data (PLAYER_ID)
)
    charset = latin1;

create table player_credits
(
    PLAYER_ID int           not null
        primary key,
    CREDITS   int default 0 null,
    constraint player_credits_ibfk_1
        foreign key (PLAYER_ID) references player_data (PLAYER_ID)
)
    charset = latin1;

create table player_nicks
(
    PLAYER_ID int               not null
        primary key,
    NICKNAME  varchar(64)       null,
    STATUS    tinyint default 0 null,
    REQUESTED varchar(64)       null,
    constraint player_nicks_ibfk_1
        foreign key (PLAYER_ID) references player_data (PLAYER_ID)
)
    charset = latin1;

create table player_votes
(
    VOTE_ID   int auto_increment
        primary key,
    PLAYER_ID int                                    not null,
    SERVICE   varchar(255)                           not null,
    TIMESTAMP timestamp  default current_timestamp() not null,
    REDEEMED  tinyint(1) default 0                   null,
    constraint player_votes_ibfk_1
        foreign key (PLAYER_ID) references player_data (PLAYER_ID)
)
    charset = latin1;

create index FK_PLAYER_VOTE_PLAYER_ID
    on player_votes (PLAYER_ID);


