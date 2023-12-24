USE PASMS_DB;
GO


-- Guard condition to check for relation absence before creating it.
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

