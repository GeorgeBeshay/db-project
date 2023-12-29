CREATE PROCEDURE GetUnAdoptedPets
    AS
BEGIN
SELECT P.*
FROM PET P
         LEFT JOIN ADOPTION A ON P.id = A.pet_id
WHERE A.pet_id IS NULL;
END;

EXEC GetUnAdoptedPets;
