USE PASMS_DB;
GO


-- Guard condition to check for relation absence before creating it.
DECLARE @inputTableName VARCHAR(128) = 'ADOPTION_APPLICATION';
IF dbo.relation_exists(@inputTableName) = 0
BEGIN
    CREATE TABLE ADOPTION_APPLICATION (
        id INT IDENTITY (1, 1),
        adopter_id INT NOT NULL,
        pet_id INT NOT NULL,
        status TINYINT NOT NULL,
        marital_status TINYINT NOT NULL,
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
        status BIT NOT NULL,
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
        status BIT NOT NULL,
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
