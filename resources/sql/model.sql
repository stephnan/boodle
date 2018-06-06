create user admin with password 'admin';

create table categories ( id serial primary key, name varchar(30) not null, monthly_budget numeric(10,2) );

create table expenses ( id serial primary key, date date, id_category integer references categories(id), item varchar(1000), amount numeric(10,2), from_savings BOOLEAN );

create table aims ( id serial primary key, name varchar(30) not null, target numeric(10,2) not null, achieved boolean default FALSE );

create table transactions ( id serial primary key, id_aim integer references aims(id), item varchar(1000), amount numeric(10,2), date DATE );

create table savings ( id serial primary key, item varchar(1000), amount numeric(10,2), date DATE );

grant all privileges on database "boodle" to admin;
grant all privileges on table categories to admin;
grant all privileges on table expenses to admin;
grant all privileges on table aims to admin;
grant all privileges on table transactions to admin;
grant all privileges on table savings to admin;
grant all privileges on table categories_id_seq to admin;
grant all privileges on table expenses_id_seq to admin;
grant all privileges on table aims_id_seq to admin;
grant all privileges on table transactions_id_seq to admin;
grant all privileges on table savings_id_seq to admin;
grant postgres to admin;
