CREATE  TRIGGER  after_update_application_status
ON ADOPTION_APPLICATION
FOR update AS
    BEGIN
        IF UPDATE (status)
        BEGIN
            INSERT INTO APPLICATION_NOTIFICATION (
                application_id
                ,adopter_id
                ,status
                ,date
                )
            SELECT id
                ,adopter_id
                ,0
                ,closing_date
            FROM INSERTED
        END
	END
GO
