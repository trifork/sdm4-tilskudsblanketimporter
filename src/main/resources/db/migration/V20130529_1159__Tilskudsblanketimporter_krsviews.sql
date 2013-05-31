
-- -----------------------------------------------------
-- Someone has to create the SKRS tables first time
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SKRSViewMapping` (
  `idSKRSViewMapping` BIGINT(15) NOT NULL AUTO_INCREMENT ,
  `register` VARCHAR(255) NOT NULL ,
  `datatype` VARCHAR(255) NOT NULL ,
  `version` INT NOT NULL ,
  `tableName` VARCHAR(255) NOT NULL ,
  `createdDate` TIMESTAMP NOT NULL ,
  PRIMARY KEY (`idSKRSViewMapping`) ,
  INDEX `idx` (`register` ASC, `datatype` ASC, `version` ASC) ,
  UNIQUE INDEX `unique` (`register` ASC, `datatype` ASC, `version` ASC) )
  ENGINE = InnoDB;
CREATE  TABLE IF NOT EXISTS `SKRSColumns` (
  `idSKRSColumns` BIGINT(15) NOT NULL AUTO_INCREMENT ,
  `viewMap` BIGINT(15) NOT NULL ,
  `isPID` TINYINT NOT NULL ,
  `tableColumnName` VARCHAR(255) NOT NULL ,
  `feedColumnName` VARCHAR(255) NULL ,
  `feedPosition` INT NOT NULL ,
  `dataType` INT NOT NULL ,
  `maxLength` INT NULL ,
  PRIMARY KEY (`idSKRSColumns`) ,
  INDEX `viewMap_idx` (`viewMap` ASC) ,
  UNIQUE INDEX `viewColumn` (`tableColumnName` ASC, `viewMap` ASC) ,
  CONSTRAINT `viewMap`
  FOREIGN KEY (`viewMap` )
  REFERENCES `SKRSViewMapping` (`idSKRSViewMapping` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

-- ---------------------------------------------------------------------------------------------------------------------
-- Tilskudsblanket
-- ---------------------------------------------------------------------------------------------------------------------

INSERT IGNORE INTO SKRSViewMapping (register, datatype, version, tableName, createdDate) VALUES ('tilskudsblanket', 'blanket', 1, 'Tilskudsblanket', NOW());
INSERT IGNORE INTO SKRSColumns (viewMap, isPID, tableColumnName, feedColumnName, feedPosition, dataType, maxLength) VALUES
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='blanket' AND version=1), 1, 'PID',                              NULL, 0, -5, NULL),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='blanket' AND version=1), 0, 'BlanketId',                 'BlanketId', 1, -5, NULL),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='blanket' AND version=1), 0, 'BlanketTekst',           'BlanketTekst', 2, 12, NULL),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='blanket' AND version=1), 0, 'ModifiedDate',           'ModifiedDate', 3, 93, 12),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='blanket' AND version=1), 0, 'ValidFrom',                 'ValidFrom', 4, 93, 12),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='blanket' AND version=1), 0, 'ValidTo',                     'ValidTo', 5, 93, 12);

INSERT IGNORE INTO SKRSViewMapping (register, datatype, version, tableName, createdDate) VALUES ('tilskudsblanket', 'forhoejettakst', 1, 'TilskudForhoejetTakst', NOW());
INSERT IGNORE INTO SKRSColumns (viewMap, isPID, tableColumnName, feedColumnName, feedPosition, dataType, maxLength) VALUES
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='forhoejettakst' AND version=1), 1, 'PID',                              NULL, 0, -5, NULL),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='forhoejettakst' AND version=1), 0, 'Varenummer',               'Varenummer', 1, 12, NULL),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='forhoejettakst' AND version=1), 0, 'Navn',                           'Navn', 1, 12, NULL),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='forhoejettakst' AND version=1), 0, 'Form',                           'Form', 2, 12, NULL),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='forhoejettakst' AND version=1), 0, 'FormTekst',                 'FormTekst', 3, 12, NULL),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='forhoejettakst' AND version=1), 0, 'ATCkode',                     'ATCkode', 4, 12, NULL),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='forhoejettakst' AND version=1), 0, 'Styrke',                       'Styrke', 5, 12, NULL),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='forhoejettakst' AND version=1), 0, 'DrugID',                       'DrugID', 6, -5, NULL),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='forhoejettakst' AND version=1), 0, 'PakningsTekst',         'PakningsTekst', 7, 12, NULL),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='forhoejettakst' AND version=1), 0, 'Udlevering',               'Udlevering', 8, 12, NULL),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='forhoejettakst' AND version=1), 0, 'Tilskudstype',           'Tilskudstype', 9, 12, NULL),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='forhoejettakst' AND version=1), 0, 'ModifiedDate',           'ModifiedDate',10, 93, 12),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='forhoejettakst' AND version=1), 0, 'ValidFrom',                 'ValidFrom',11, 93, 12),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='forhoejettakst' AND version=1), 0, 'ValidTo',                     'ValidTo',12, 93, 12);

INSERT IGNORE INTO SKRSViewMapping (register, datatype, version, tableName, createdDate) VALUES ('tilskudsblanket', 'blanketforhoejet', 1, 'TilskudsblanketForhoejet', NOW());
INSERT IGNORE INTO SKRSColumns (viewMap, isPID, tableColumnName, feedColumnName, feedPosition, dataType, maxLength) VALUES
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='blanketforhoejet' AND version=1), 1, 'PID',                              NULL, 0, -5, NULL),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='blanketforhoejet' AND version=1), 0, 'BlanketId',                 'BlanketId', 1, -5, NULL),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='blanketforhoejet' AND version=1), 0, 'DrugId',                       'DrugId', 2, 12, NULL),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='blanketforhoejet' AND version=1), 0, 'ModifiedDate',           'ModifiedDate', 3, 93, 12),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='blanketforhoejet' AND version=1), 0, 'ValidFrom',                 'ValidFrom', 4, 93, 12),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='blanketforhoejet' AND version=1), 0, 'ValidTo',                     'ValidTo', 5, 93, 12);

INSERT IGNORE INTO SKRSViewMapping (register, datatype, version, tableName, createdDate) VALUES ('tilskudsblanket', 'blanketenkelt', 1, 'TilskudsblanketEnkelt', NOW());
INSERT IGNORE INTO SKRSColumns (viewMap, isPID, tableColumnName, feedColumnName, feedPosition, dataType, maxLength) VALUES
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='blanketenkelt' AND version=1), 1, 'PID',                      NULL, 0, -5, NULL),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='blanketenkelt' AND version=1), 0, 'BlanketId',         'BlanketId', 1, -5, NULL),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='blanketenkelt' AND version=1), 0, 'Genansoegning', 'Genansoegning', 2, -5, NULL),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='blanketenkelt' AND version=1), 0, 'Navn',                   'Navn', 3, 12, NULL),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='blanketenkelt' AND version=1), 0, 'Form',                   'Form', 4, 12, NULL),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='blanketenkelt' AND version=1), 0, 'Id',                       'Id', 5, 12, NULL),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='blanketenkelt' AND version=1), 0, 'ModifiedDate',   'ModifiedDate', 6, 93, 12),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='blanketenkelt' AND version=1), 0, 'ValidFrom',         'ValidFrom', 7, 93, 12),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='blanketenkelt' AND version=1), 0, 'ValidTo',             'ValidTo', 8, 93, 12);

INSERT IGNORE INTO SKRSViewMapping (register, datatype, version, tableName, createdDate) VALUES ('tilskudsblanket', 'blanketterminal', 1, 'TilskudsblanketTerminal', NOW());
INSERT IGNORE INTO SKRSColumns (viewMap, isPID, tableColumnName, feedColumnName, feedPosition, dataType, maxLength) VALUES
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='blanketterminal' AND version=1), 1, 'PID',                      NULL, 0, -5, NULL),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='blanketterminal' AND version=1), 0, 'Id',                       'Id', 1, 12, NULL),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='blanketterminal' AND version=1), 0, 'BlanketId',         'BlanketId', 2, -5, NULL),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='blanketterminal' AND version=1), 0, 'ModifiedDate',   'ModifiedDate', 3, 93, 12),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='blanketterminal' AND version=1), 0, 'ValidFrom',         'ValidFrom', 4, 93, 12),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='blanketterminal' AND version=1), 0, 'ValidTo',             'ValidTo', 5, 93, 12);

INSERT IGNORE INTO SKRSViewMapping (register, datatype, version, tableName, createdDate) VALUES ('tilskudsblanket', 'blanketkroniker', 1, 'TilskudsblanketKroniker', NOW());
INSERT IGNORE INTO SKRSColumns (viewMap, isPID, tableColumnName, feedColumnName, feedPosition, dataType, maxLength) VALUES
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='blanketkroniker' AND version=1), 1, 'PID',                      NULL, 0, -5, NULL),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='blanketkroniker' AND version=1), 0, 'BlanketId',         'BlanketId', 1, -5, NULL),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='blanketkroniker' AND version=1), 0, 'Genansoegning', 'Genansoegning', 2, -5, NULL),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='blanketkroniker' AND version=1), 0, 'ModifiedDate',   'ModifiedDate', 3, 93, 12),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='blanketkroniker' AND version=1), 0, 'ValidFrom',         'ValidFrom', 4, 93, 12),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='tilskudsblanket' AND datatype='blanketkroniker' AND version=1), 0, 'ValidTo',             'ValidTo', 5, 93, 12);
