CREATE PROCEDURE GetAdoptersIDs
    AS
BEGIN
SELECT id
FROM ADOPTER
END;

EXEC GetAdoptersIDs;
