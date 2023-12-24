USE PASMS_DB;
GO















































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
                     shelter_id INT, -- To add FK
                     neutering BIT,
                     house_training BIT,
                     vaccination BIT
);

CREATE TABLE PET_DOCUMENT (
                              id INT IDENTITY(1, 1) PRIMARY KEY,
                              pet_id INT FOREIGN KEY REFERENCES PET(id),
                              document_type VARCHAR(50) NOT NULL,
                              document VARBINARY(MAX) NOT NULL
);

CREATE TABLE ADOPTION (
                          pet_id INT FOREIGN KEY REFERENCES PET(id),
                          adopter_id INT, -- To add FK
                          PRIMARY KEY (pet_id, adopter_id)
);

-- ALTER TABLE PET
-- ADD CONSTRAINT fk_pet_shelter
-- FOREIGN KEY (shelter_id)
-- REFERENCES SHELTER(id);

-- ALTER TABLE ADOPTION
-- ADD CONSTRAINT fk_adoption_adopter
-- FOREIGN KEY (adopter_id)
-- REFERENCES ADOPTER(id);
