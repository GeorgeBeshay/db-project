USE PASMS_DB;
GO


CREATE TABLE ADOPTION_APPLICATION (
	id INT,
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

CREATE TABLE APPLICATION_NOTIFICATION (
	adoption_application_id INT NOT NULL,
	adopter_id INT NOT NULL,
	status BIT NOT NULL,
	date DATE NOT NULL,
	PRIMARY KEY (adoption_application_id, adopter_id),
	FOREIGN KEY (adoption_application_id) REFERENCES ADOPTION_APPLICATION(id),
	FOREIGN KEY (adopter_id) REFERENCES ADOPTER(id)
);

CREATE TABLE PET_AVAILABILITY_NOTIFICATION (
	pet_id INT NOT NULL,
	adopter_id INT NOT NULL,
	status BIT NOT NULL,
	date DATE NOT NULL,
	PRIMARY KEY (pet_id, adopter_id),
	FOREIGN KEY (pet_id) REFERENCES PET(id),
	FOREIGN KEY (adopter_id) REFERENCES ADOPTER(id)
);
