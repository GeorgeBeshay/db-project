USE PASMS_DB;
GO
DECLARE @inputTableName
VARCHAR(125) = 'ADMINISTRATOR';
IF
dbo.relation_exists(@inputTableName) = 0
BEGIN
Create table ADMINISTRATOR(
id int primary key IDENTITY(1, 1),
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
id int primary key IDENTITY(1, 1),
name varchar(50) not null,
location varchar(255) not null,
email varchar(255) not null,
phone varchar(15) not null,
manager int foreign key references STAFF(id));
END
ELSE
BEGIN
    PRINT 'Shelter relation already exists.';


DECLARE @inputTableName VARCHAR(128) = 'ADOPTER';
IF dbo.relation_exists(@inputTableName) = 0
BEGIN
    CREATE TABLE ADOPTER (
        id INT PRIMARY KEY,
        email VARCHAR(255) UNIQUE NOT NULL,         -- candidate key
        password_hash VARCHAR(256) NOT NULL,
        first_name VARCHAR(50) NOT NULL,
        last_name VARCHAR(50) NOT NULL,
        phone VARCHAR(15),
        birth_date DATE,
        gender BIT, -- equivalent to isMale boolean variable
        address VARCHAR(255) NOT NULL -- Adjust length based on address length requirement
                         );
END
ELSE
BEGIN
    PRINT 'ADOPTER relation already exists.';
END
GO


-- Guard condition to check for relation absence before creating it.
DECLARE @inputTableName VARCHAR(128) = 'STAFF';
IF dbo.relation_exists(@inputTableName) = 0
BEGIN
    CREATE TABLE STAFF (
        id INT PRIMARY KEY,
        first_name VARCHAR(50) NOT NULL,
        last_name VARCHAR(50) NOT NULL,
        role VARCHAR(50) NOT NULL CHECK (role IN ('Manager', 'Member')),
        phone VARCHAR(15),
        email VARCHAR(255) NOT NULL UNIQUE,        -- added not null constraint.
        password_hash VARCHAR(256) NOT NULL,
        shelter_id INT,
        FOREIGN KEY (shelter_id) REFERENCES SHELTER(id),
        -- The SHELTER relation must be defined before creating this relation.
                        );
END
ELSE
BEGIN
    PRINT 'STAFF relation already exists.';
END
GO


DECLARE @inputTableName VARCHAR(128) = 'PET';
IF dbo.relation_exists(@inputTableName) = 0
BEGIN

CREATE TABLE PET (
     id INT PRIMARY KEY,
     name VARCHAR(50) NOT NULL,
     specie VARCHAR(50) NOT NULL,
     breed VARCHAR(50) NOT NULL,
     birthdate DATE,
     gender BIT NOT NULL,
     health_status VARCHAR(50) NOT NULL,
     behaviour VARCHAR(100),
     description VARCHAR(100),
     shelter_id INT,
     neutering BIT,
     house_training BIT,
     vaccination BIT
);

END
ELSE
BEGIN
    PRINT 'PET relation already exists.'
end;


DECLARE @inputTableName VARCHAR(128) = 'PET_DOCUMENT';
IF dbo.relation_exists(@inputTableName) = 0
BEGIN

CREATE TABLE PET_DOCUMENT (
    id INT IDENTITY(1, 1) PRIMARY KEY,
    pet_id INT NOT NULL FOREIGN KEY REFERENCES PET(id),
    document_type VARCHAR(50) NOT NULL,
    document VARBINARY(MAX) NOT NULL
);

END
ELSE
BEGIN
    PRINT 'PET_DOCUMENT relation already exists.'
end;


DECLARE @inputTableName VARCHAR(128) = 'ADOPTION';
IF dbo.relation_exists(@inputTableName) = 0
BEGIN

CREATE TABLE ADOPTION (
    pet_id INT NOT NULL FOREIGN KEY REFERENCES PET(id),
    adopter_id INT NOT NULL,
    PRIMARY KEY (pet_id, adopter_id)
);

END
ELSE
BEGIN
    PRINT 'ADOPTION relation already exists.'
end;


ALTER TABLE PET
ADD CONSTRAINT fk_pet_shelter
FOREIGN KEY (shelter_id)
REFERENCES SHELTER(id);

ALTER TABLE ADOPTION
ADD CONSTRAINT fk_adoption_adopter
FOREIGN KEY (adopter_id)
REFERENCES ADOPTER(id);

-- Guard condition to check for relation absence before creating it.
DECLARE @inputTableName VARCHAR(128) = 'ADOPTION_APPLICATION';
IF dbo.relation_exists(@inputTableName) = 0
BEGIN
    CREATE TABLE ADOPTION_APPLICATION (
        id INT IDENTITY (1, 1),
        adopter_id INT NOT NULL,
        pet_id INT NOT NULL,
        status VARCHAR(10) NOT NULL CHECK (status IN ('Pending', 'Approved', 'Rejected')),
        experience BIT NOT NULL,
        creation_date DATE NOT NULL,
        closing_date DATE,
        description VARCHAR(MAX) NOT NULL,
        PRIMARY KEY (id),
        FOREIGN KEY (adopter_id) REFERENCES ADOPTER(id),
        FOREIGN KEY (pet_id) REFERENCES PET(id)
    );
END
ELSE
BEGIN
   PRINT 'ADOPTION_APPLICATION relation already exists.';
END
GO

-- Guard condition to check for relation absence before creating it.
DECLARE @inputTableName VARCHAR(128) = 'APPLICATION_NOTIFICATION';
IF dbo.relation_exists(@inputTableName) = 0
BEGIN
    CREATE TABLE APPLICATION_NOTIFICATION (
        adoption_application_id INT NOT NULL,
        adopter_id INT NOT NULL,
        status BIT NOT NULL, -- equivalent to isRead
        date DATE NOT NULL,
        PRIMARY KEY (adoption_application_id, adopter_id),
        FOREIGN KEY (adoption_application_id) REFERENCES ADOPTION_APPLICATION(id),
        FOREIGN KEY (adopter_id) REFERENCES ADOPTER(id)
    );
END
ELSE
BEGIN
    PRINT 'APPLICATION_NOTIFICATION relation already exists.';
END
GO

-- Guard condition to check for relation absence before creating it.
DECLARE @inputTableName VARCHAR(128) = 'PET_AVAILABILITY_NOTIFICATION';
IF dbo.relation_exists(@inputTableName) = 0
    CREATE TABLE PET_AVAILABILITY_NOTIFICATION (
        pet_id INT NOT NULL,
        adopter_id INT NOT NULL,
        status BIT NOT NULL, -- equivalent to isRead
        date DATE NOT NULL,
        PRIMARY KEY (pet_id, adopter_id),
        FOREIGN KEY (pet_id) REFERENCES PET(id),
        FOREIGN KEY (adopter_id) REFERENCES ADOPTER(id)
    );
END
ELSE
BEGIN
    PRINT 'PET_AVAILABILITY_NOTIFICATION relation already exists.';
END
GO