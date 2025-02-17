-- UserDetails Table
CREATE TABLE User_Details (
    UserUID NVARCHAR(255) PRIMARY KEY,
    Email NVARCHAR(255),
    Name NVARCHAR(255),
    Phone NVARCHAR(20)
);

-- UserCredentials Table
CREATE TABLE User_Credentials (
    Username NVARCHAR(255) PRIMARY KEY,
    Password NVARCHAR(255),
    UserUID NVARCHAR(255) NULL,
    FOREIGN KEY (UserUID) REFERENCES User_Details(UserUID)
);

-- Template
CREATE TABLE Template (
    TemplateID INT IDENTITY(1,1) PRIMARY KEY,
    UserUID NVARCHAR(255) NULL,
    TemplateName NVARCHAR(255),
****Description NVARCHAR(255),
    DateCreated DATETIME,
    FOREIGN KEY (UserUID) REFERENCES User_Details(UserUID)
);

-- TemplateTables Table
CREATE TABLE Template_Tables (
    TableID INT IDENTITY(1,1) PRIMARY KEY,
    TemplateID INT NULL,
    TableName NVARCHAR(255),
    FOREIGN KEY (TemplateID) REFERENCES Template(TemplateID)
);

-- HeaderDetails Table
CREATE TABLE Header_Details (
    HeaderID INT IDENTITY(1,1) PRIMARY KEY,
    TableID INT NULL,
    HeaderName NVARCHAR(255),
    HeaderFillColour NVARCHAR(20),
    HeaderTextColour NVARCHAR(20),
****TextBold BIT,
    FOREIGN KEY (TableID) REFERENCES Template_Tables(TableID)
);

-- TableDetails Table
CREATE TABLE Table_Details (
    TableDetailsID INT IDENTITY(1,1) PRIMARY KEY,
    TableID INT NULL,
    JsonData NVARCHAR(MAX),
    FOREIGN KEY (TableID) REFERENCES Template_Tables(TableID)
);

-- Notification Table
CREATE TABLE Notifications (
    NotificationID INT IDENTITY(1,1) PRIMARY KEY,
    TemplateID INT NULL,
    UserUID NVARCHAR(255) NULL,
    NotificationFlag BIT,
    SMSFlag BIT,
    WhatsAppFlag BIT,
    EmailFlag BIT,
    FOREIGN KEY (TemplateID) REFERENCES Template(TemplateID),
    FOREIGN KEY (UserUID) REFERENCES User_Details(UserUID)
);

-- RepeatStatus Table
CREATE TABLE Repeat_Status (
    RepeatID INT IDENTITY(1,1) PRIMARY KEY,
    NotificationID INT NULL,
    RepeatInterval NVARCHAR(255),
    RepeatStartDate DATE,
    RepeatStartTime TIME,
	RepeatIntervalType NVARCHAR(64),
    FOREIGN KEY (NotificationID) REFERENCES Notifications(NotificationID)
);

-- ShareTable Table
CREATE TABLE Share_Table (
    SharingCode NVARCHAR(255) PRIMARY KEY,
    TemplateID INT NOT NULL,
    TemplateDetails NVARCHAR(MAX),
    ExpirationDate DATETIME,
    FOREIGN KEY (TemplateID) REFERENCES Template(TemplateID)
);





DROP TABLE IF EXISTS Repeat_Status;
DROP TABLE IF EXISTS Notifications;
DROP TABLE IF EXISTS Share_Table;
DROP TABLE IF EXISTS Table_Details;
DROP TABLE IF EXISTS Header_Details;
DROP TABLE IF EXISTS Template_Tables;
DROP TABLE IF EXISTS Template;
DROP TABLE IF EXISTS User_Credentials;
DROP TABLE IF EXISTS User_Details;



-- RepeatStatus Table
DELETE FROM Repeat_Status WHERE NotificationID IS NOT NULL;

-- ShareTable Table
DELETE FROM Share_Table WHERE TemplateID IS NOT NULL;

-- TableDetails Table
DELETE FROM Table_Details WHERE TableID IS NOT NULL;

-- HeaderDetails Table
DELETE FROM Header_Details WHERE TableID IS NOT NULL;

-- TemplateTables Table
DELETE FROM Template_Tables WHERE TemplateID IS NOT NULL;

-- Notifications Table
DELETE FROM Notifications WHERE TemplateID IS NOT NULL OR UserUID IS NOT NULL;

-- Template Table
DELETE FROM Template WHERE UserUID IS NOT NULL;

-- UserCredentials Table
DELETE FROM User_Credentials WHERE UserUID IS NOT NULL;

-- UserDetails Table
DELETE FROM User_Details WHERE UserUID IS NOT NULL;

