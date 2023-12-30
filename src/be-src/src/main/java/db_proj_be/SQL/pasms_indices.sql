CREATE INDEX adopter_id_app_ind
ON ADOPTION_APPLICATION (adopter_id);

CREATE INDEX adopter_id_not_ind
ON APPLICATION_NOTIFICATION (adopter_id);

CREATE INDEX pet_id_doc_ind
ON PET_DOCUMENT (pet_id);

CREATE INDEX shelter_id_staff_ind
ON STAFF (shelter_id);

CREATE INDEX shelter_name_ind
ON SHELTER (name);
