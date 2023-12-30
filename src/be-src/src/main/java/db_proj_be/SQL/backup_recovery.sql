USE master
GO

-- Create a stored procedure to perform a database backup
CREATE PROCEDURE PerformDatabaseBackupPASMS_DB
AS
BEGIN
    BACKUP DATABASE PASMS_DB
    TO DISK = 'C:\Program Files\Microsoft SQL Server\MSSQL16.MSSQLSERVER\MSSQL\Backup\PASMS_DB.bak'
        WITH FORMAT,
        NAME = 'Full Backup of PASMS_DB ',
        MEDIANAME = 'PASMS_DB1',
        DESCRIPTION = 'This is a backup file of PASMS_DB'
END
GO

CREATE PROCEDURE RestoreDatabaseFromBackupPASMS_DB
AS
BEGIN
    RESTORE DATABASE PASMS_DB
    FROM DISK = 'C:\Program Files\Microsoft SQL Server\MSSQL16.MSSQLSERVER\MSSQL\Backup\PASMS_DB.bak'
    WITH REPLACE;
END;
GO

EXEC PerformDatabaseBackupPASMS_DB
GO

DROP DATABASE PASMS_DB;
GO

USE master;
GO

EXEC RestoreDatabaseFromBackupPASMS_DB
GO

USE PASMS_DB;
GO


