USE PASMS_DB;
GO

Create table ADMINISTRATOR(
admin_id int primary key,
fname varchar(20) NOT NULL,
lname varchar(20) not null,
email varchar(30) unique not null,
phone varchar(15),
password_hash varchar(256) not null)

Create table SHELTER(
shelter_id int primary key,
shelter_name varchar(50) not null,
shelter_location varchar(255) not null,
shelter_email varchar(255) not null,
shelter_phone varchar(15) not null,
shelter_manager int foreign key references STAFF(id));

