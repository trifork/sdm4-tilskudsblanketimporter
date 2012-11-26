CREATE TABLE IF NOT EXISTS Tilskudsblanket (
    TilskudsblanketPID BIGINT(15) AUTO_INCREMENT NOT NULL PRIMARY KEY,

    BlanketId BIGINT(15),
    BlanketTekst VARCHAR(21000),

    ModifiedDate DATETIME NOT NULL,
    ValidFrom DATETIME NOT NULL,
    ValidTo DATETIME,

    INDEX (BlanketId, ValidTo, ValidFrom)
) ENGINE=InnoDB COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS TilskudsblanketTerminal (
    TilskudsblanketTerminalPID BIGINT(15) AUTO_INCREMENT NOT NULL PRIMARY KEY,

    BlanketId BIGINT(15),

    ModifiedDate DATETIME NOT NULL,
    ValidFrom DATETIME NOT NULL,
    ValidTo DATETIME,

    INDEX (BlanketId, ValidTo, ValidFrom)
) ENGINE=InnoDB COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS TilskudsblanketKroniker (
    TilskudsblanketKronikerPID BIGINT(15) AUTO_INCREMENT NOT NULL PRIMARY KEY,

    BlanketId BIGINT(15),
    Genansoegning BIGINT(1),

    ModifiedDate DATETIME NOT NULL,
    ValidFrom DATETIME NOT NULL,
    ValidTo DATETIME,

    INDEX (BlanketId, ValidTo, ValidFrom)
) ENGINE=InnoDB COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS TilskudsblanketForhoejet (
    TilskudsblanketForhoejetPID BIGINT(15) AUTO_INCREMENT NOT NULL PRIMARY KEY,

    BlanketId BIGINT(15),
    DrugId BIGINT(12),

    ModifiedDate DATETIME NOT NULL,
    ValidFrom DATETIME NOT NULL,
    ValidTo DATETIME,

    INDEX (BlanketId, ValidTo, ValidFrom)
) ENGINE=InnoDB COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS TilskudsblanketEnkelt (
    TilskudsblanketEnkeltPID BIGINT(15) AUTO_INCREMENT NOT NULL PRIMARY KEY,

    BlanketId BIGINT(15),
    Genansoegning BIGINT(1),
    Navn VARCHAR(100) NULL,
    Form VARCHAR(100) NULL,

    ModifiedDate DATETIME NOT NULL,
    ValidFrom DATETIME NOT NULL,
    ValidTo DATETIME,

    INDEX (BlanketId, ValidTo, ValidFrom)
) ENGINE=InnoDB COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS TilskudsblanketForhoejet (
    TilskudsblanketForhoejetPID BIGINT(15) AUTO_INCREMENT NOT NULL PRIMARY KEY,

    BlanketId BIGINT(15),
    DrugId BIGINT(12),

    ModifiedDate DATETIME NOT NULL,
    ValidFrom DATETIME NOT NULL,
    ValidTo DATETIME,

    INDEX (BlanketId, ValidTo, ValidFrom)
) ENGINE=InnoDB COLLATE=utf8_bin;