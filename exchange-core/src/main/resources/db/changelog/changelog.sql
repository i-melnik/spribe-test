--liquibase formatted sql

--changeset currency-exchange:1
create table if not exists currency
(
    code       varchar(64) primary key,
    name       varchar(255),
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);
--rollback drop table currency;

--changeset currency-exchange:2
create table if not exists exchange_rate
(
    base       varchar        not null references currency,
    rates      jsonb        not null,
    date       timestamptz    not null,
    created_at timestamptz    not null default now(),
    updated_at timestamptz    not null default now(),
    primary key (base, date)
);
--rollback drop table currency;