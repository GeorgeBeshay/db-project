USE PASMS_DB;
GO


CREATE FUNCTION dbo.relation_exists(@tableName NVARCHAR(128))
RETURNS BIT
AS
BEGIN
    DECLARE @tableExists BIT

    IF EXISTS (
        SELECT 1
        FROM INFORMATION_SCHEMA.TABLES
        WHERE TABLE_SCHEMA = 'dbo'
        AND TABLE_NAME = @tableName
    )
        SET @tableExists = 1
    ELSE
        SET @tableExists = 0

    RETURN @tableExists
END;
GO