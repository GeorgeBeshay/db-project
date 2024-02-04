CREATE TRIGGER trgBeforeDeletePet
ON dbo.PET
INSTEAD OF DELETE
AS
BEGIN
    SET NOCOUNT ON;

    -- Delete records from PET_AVAILABILITY_NOTIFICATION related to the pet being deleted
    DELETE FROM dbo.PET_AVAILABILITY_NOTIFICATION
    WHERE PET_AVAILABILITY_NOTIFICATION.pet_id IN (SELECT id FROM DELETED);

    -- Delete the pet record itself
    DELETE FROM dbo.PET
    WHERE id IN (SELECT id from DELETED)

END;
