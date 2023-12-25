USE PASMS_DB;
GO
DECLARE @inputTableName
VARCHAR(125) = 'ADMINISTRATOR';
IF
dbo.relation_exists(@inputTableName) = 0
BEGIN
Create table ADMINISTRATOR(
id int primary key,
first_name varchar(50) NOT NULL,
last_name varchar(50) not null,
email varchar(30) unique not null,
phone varchar(15),
password_hash varchar(256) not null);
END
ELSE
BEGIN
    PRINT 'Administrator relation already exists.';
END
GO

DECLARE @inputTableName
VARCHAR(125) = 'SHELTER';
IF
dbo.relation_exists(@inputTableName) = 0
BEGIN
Create table SHELTER(
id int primary key,
name varchar(50) not null,
location varchar(255) not null,
email varchar(255) not null,
phone varchar(15) not null,
manager int foreign key references STAFF(id));
END
ELSE
BEGIN
    PRINT 'Shelter relation already exists.';
END
GO