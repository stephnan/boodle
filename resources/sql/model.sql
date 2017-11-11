create user admin with password 'admin';
create table categories (
        id serial primary key,
        name varchar(30) not null,
        monthly_budget numeric(7,2)
        );
create table expenses (
        id serial primary key,
        date date,
        id_category integer references categories(id),
        item varchar(1000),
        amount numeric(7,2)
        );
create table aims (
        id serial primary key,
        name varchar(30) not null,
        target numeric(7,2) not null,
        archived boolean
        );
create table transactions (
        id serial primary key,
        id_aim integer references aims(id),
        type numeric,
        item varchar(1000),
        amount numeric(7,2),
        date date
        );
grant all privileges on database "boodle" to admin;
grant all privileges on table categories to admin;
grant all privileges on table expenses to admin;
grant all privileges on table aims to admin;
grant all privileges on table transactions to admin;
grant all privileges on table categories_id_seq to admin;
grant all privileges on table expenses_id_seq to admin;
grant all privileges on table aims_id_seq to admin;
grant all privileges on table transactions_id_seq to admin;
