create table diary
(
    id      int not null primary key auto_increment,
    weather varchar(50) not null,
    icon varchar(50) not null,
    temperature double not null,
    text varchar(500) not null,
    date DATE not null
);

select * from diary;

create table date_weather
(
    date DATE not null primary key,
    weather varchar(50) not null,
    icon varchar(50) not null,
    temperature double not null
);