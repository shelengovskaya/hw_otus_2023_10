-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
/*
create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

 */

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create sequence client_SEQ start with 1 increment by 1;

create table client
(
    id   bigint not null primary key,
    name varchar(50)
);

create table address
(
    id     bigserial not null primary key,
    street varchar(50)
);

create table phone
(
    id        bigserial not null primary key,
    number    varchar(50),
    client_id bigint,
    constraint phones foreign key (client_id) references client(id)
);

alter table client
    add column address_id bigint,
    add constraint addresses foreign key (address_id) references address(id);
