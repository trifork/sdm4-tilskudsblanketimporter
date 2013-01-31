
ALTER TABLE TilskudsblanketTerminal ADD COLUMN Id VARCHAR(1) NOT NULL AFTER PID
, ADD INDEX Id (Id ASC, ValidFrom ASC, ValidTo ASC)
, DROP INDEX BlanketId ;

ALTER TABLE TilskudsblanketEnkelt ADD COLUMN Id VARCHAR(32) NOT NULL AFTER PID
, ADD INDEX Id (Id ASC, ValidTo ASC, ValidFrom ASC) ;

ALTER TABLE TilskudsblanketForhoejet
ADD INDEX DrugId (DrugId ASC, ValidFrom ASC, ValidTo ASC)
, DROP INDEX BlanketId ;

ALTER TABLE TilskudsblanketKroniker
ADD INDEX Genansoegning (Genansoegning ASC, ValidFrom ASC, ValidTo ASC)
, DROP INDEX BlanketId ;

