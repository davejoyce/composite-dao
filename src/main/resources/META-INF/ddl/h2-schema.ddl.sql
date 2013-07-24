
    alter table APP_TWEET 
        drop constraint FK4A956A936F7D08EE;

    alter table APP_USER_SOCIAL_CONNECTION 
        drop constraint FKC8CE7F7A6F7D08EE;

    drop table APP_TWEET if exists;

    drop table APP_USER if exists;

    drop table APP_USER_SOCIAL_CONNECTION if exists;

    create table APP_TWEET (
        APP_TWEET_ID bigint not null,
        CREATED_DT timestamp not null,
        FROM_APP_USER_ID bigint,
        FROM_APP_USER_NAME varchar(15),
        TEXT varchar(140),
        TO_APP_USER_ID bigint,
        TO_APP_USER_NAME varchar(15),
        APP_USER_ID varchar(50) not null,
        primary key (APP_TWEET_ID)
    );

    create table APP_USER (
        APP_USER_ID varchar(50) not null,
        ACTIVE boolean not null,
        CREATED_DT date not null,
        primary key (APP_USER_ID)
    );

    create table APP_USER_SOCIAL_CONNECTION (
        PROVIDER_ID varchar(255) not null,
        PROVIDER_USER_ID varchar(255) not null,
        ACCESS_TOKEN varchar(255) not null,
        DISPLAY_NAME varchar(255),
        EXPIRE_TM bigint,
        IMAGE_URL varchar(512),
        PROFILE_URL varchar(512),
        RANK integer not null,
        REFRESH_TOKEN varchar(255),
        SECRET varchar(255),
        APP_USER_ID varchar(50) not null,
        primary key (APP_USER_ID, PROVIDER_ID, PROVIDER_USER_ID),
        unique (APP_USER_ID, PROVIDER_ID, RANK)
    );

    create index AT_APP_USER_CREATED_DT_IDX on APP_TWEET (APP_USER_ID, CREATED_DT);

    alter table APP_TWEET 
        add constraint FK4A956A936F7D08EE 
        foreign key (APP_USER_ID) 
        references APP_USER;

    alter table APP_USER_SOCIAL_CONNECTION 
        add constraint FKC8CE7F7A6F7D08EE 
        foreign key (APP_USER_ID) 
        references APP_USER;
