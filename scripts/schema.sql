create database cooks_corner;

create table image
(
    id         bigint generated always as identity
        constraint image_pk
            primary key,
    image_url  varchar(255) not null,
    image_name varchar(255) not null
);

create table app_user
(
    id        bigint generated always as identity
        constraint app_user_pk
            primary key,
    name      varchar(255)          not null,
    email     varchar(255)          not null
        constraint app_user_email_pk
            unique,
    password  varchar(255)          not null,
    image_id  bigint
        constraint app_user_image_fk
            references image,
    is_enable boolean default false not null,
    bio       varchar(255)
);

create table role
(
    id        bigint generated always as identity
        constraint role_pk
            primary key,
    authority integer not null
);

INSERT INTO role (authority)
VALUES
    (0),
    (1);

create table user_role_junction
(
    user_id bigint not null,
    role_id bigint not null,
    constraint user_role_junction_pk
        primary key (user_id, role_id)
);

create table follower
(
    user_id     bigint  not null
        constraint user_fk
            references app_user,
    follower_id integer not null
        constraint follower_fk
            references app_user,
    constraint follower_pk
        primary key (user_id, follower_id)
);

create table following
(
    user_id      bigint not null
        constraint user_fk
            references app_user,
    following_id bigint not null
        constraint following_fk
            references app_user,
    constraint following_pk
        primary key (user_id, following_id)
);

create table refresh_token
(
    id          bigint generated always as identity
        constraint refresh_token_pk
            primary key,
    token       varchar(255) not null,
    user_id     bigint       not null
        constraint refresh_token_app_user_id_fk
            references app_user
            on delete cascade,
    expiry_date timestamp    not null
);

create table ingredient
(
    id   bigint generated always as identity
        constraint ingredient_pk
            primary key,
    name varchar(255) not null
);

create table recipe
(
    id                   bigint generated always as identity
        constraint recipe_pk
            primary key,
    title                varchar(255) not null,
    description          varchar(255) not null,
    image_id             bigint
        constraint image_fk
            references image,
    category             integer      not null,
    difficulty           integer      not null,
    cooking_time_minutes integer      not null,
    user_id              bigint       not null
        constraint user___fk
            references app_user
);

create table recipe_ingredient_junction
(
    id            bigint generated always as identity
        constraint recipe_ingredient_junction_pk
            primary key,
    recipe_id     bigint       not null
        constraint recipe___fk
            references recipe,
    ingredient_id bigint       not null
        constraint ingredient___fk
            references ingredient,
    amount        numeric      not null,
    measure_unit  varchar(255) not null
);

create table user_recipe_bookmark
(
    user_id   bigint not null
        constraint user___fk
            references app_user,
    recipe_id bigint not null
        constraint recipe___fk
            references recipe,
    constraint user_recipe_bookmark_pk
        primary key (user_id, recipe_id)
);

create table user_recipe_like
(
    user_id   bigint not null
        constraint user___fk
            references app_user,
    recipe_id bigint not null
        constraint recipe___fk
            references recipe,
    constraint user_recipe_like_pk
        primary key (user_id, recipe_id)
);


