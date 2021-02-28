DROP TABLE IF EXISTS guitarists;

CREATE TABLE guitarists (
    id int IDENTITY (1,1) PRIMARY KEY,
    first_name varchar(255) not null,
    last_name varchar(255),
    nationality varchar(255) not null
);

insert into guitarists values (1,'Jimi','Hendrix','American');
insert into guitarists values (2,'Shawn','Lane','American');
insert into guitarists values (3,'Guthrie','Govan','British');