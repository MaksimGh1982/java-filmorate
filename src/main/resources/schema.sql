CREATE SCHEMA IF NOT EXISTS FILMORATE;

CREATE TABLE IF NOT EXISTS FILMORATE.МРА
(id INTEGER NOT NULL auto_increment,
Name VARCHAR(255) NOT NULL,
description VARCHAR(255),
PRIMARY KEY (Id));

CREATE TABLE IF NOT EXISTS FILMORATE.FILM
(id INTEGER NOT NULL auto_increment,
Name VARCHAR(255) NOT NULL,
description VARCHAR(255),
releaseDate DATE,
Duration INTEGER,
MPA_Id   INTEGER,
PRIMARY KEY (Id)
);

CREATE TABLE IF NOT EXISTS FILMORATE.USERS
(id INTEGER NOT NULL auto_increment,
email VARCHAR(255) NOT NULL,
Login VARCHAR(255) NOT NULL,
Name  VARCHAR(255),
birthday DATE,
PRIMARY KEY (Id));

CREATE TABLE IF NOT EXISTS FILMORATE.LIKES
(id INTEGER NOT NULL auto_increment,
FilmId INTEGER NOT NULL,
UserId INTEGER NOT NULL,
PRIMARY KEY (Id),
FOREIGN KEY (FilmId)  REFERENCES FILMORATE.FILM(Id),
FOREIGN KEY (UserId)  REFERENCES FILMORATE.USERS(Id));

CREATE TABLE IF NOT EXISTS FILMORATE.FRIENDS
(id INTEGER NOT NULL auto_increment,
UserId1 INTEGER NOT NULL,
UserId2 INTEGER NOT NULL,
Confirm BOOLEAN NOT NULL DEFAULT FALSE,
PRIMARY KEY (Id),
FOREIGN KEY (UserId1)  REFERENCES FILMORATE.USERS (Id),
FOREIGN KEY (UserId1)  REFERENCES FILMORATE.USERS (Id));


CREATE TABLE IF NOT EXISTS FILMORATE.GENRE
(id INTEGER NOT NULL auto_increment,
Name VARCHAR(255) NOT NULL,
PRIMARY KEY (Id));

CREATE TABLE IF NOT EXISTS FILMORATE.FILMGENRE
(id INTEGER NOT NULL auto_increment,
FilmId INTEGER NOT NULL,
GenreId INTEGER NOT NULL,
PRIMARY KEY (Id),
FOREIGN KEY (FilmId)  REFERENCES FILMORATE.FILM (Id),
FOREIGN KEY (GenreId)  REFERENCES FILMORATE.GENRE (Id));

insert into FILMORATE.МРА (id,name,description) select * from (
select 1,'G','у фильма нет возрастных ограничений' union
select 2,'PG','детям рекомендуется смотреть фильм с родителями' union
select 3,'PG-13','детям до 13 лет просмотр не желателен' union
select 4,'R','лицам до 17 лет просматривать фильм можно только в присутствии взрослого' union
select 5,'NC-17','17'
) x where not exists(select * from FILMORATE.МРА);

insert into FILMORATE.GENRE (id,name) select * from (
select 1,'Комедия' union
select 2,'Драма' union
select 3,'Мультфильм' union
select 4,'Триллер' union
select 5,'Документальный' union
select 6,'Боевик'
) x where not exists(select * from FILMORATE.GENRE);



