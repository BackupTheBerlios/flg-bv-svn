CREATE TABLE `Leases` (
  `LID` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'LeaseID Ausleihvorgang',
  `OutTime` DATETIME  NOT NULL COMMENT 'Leihbeginn',
  `BackTime` DATETIME  DEFAULT NULL COMMENT 'Leihende',
  `UserID` BIGINT UNSIGNED NOT NULL COMMENT 'Ausleiher',
  `LObjectID` BIGINT UNSIGNED NOT NULL COMMENT 'LeaseObjectID nach Status: nicht individualisiert:  -> ISBN/BKID; indiv. -> BookID',
  PRIMARY KEY (`LID`)
)
ENGINE = MyISAM
COMMENT = 'Ausleihvorgänge';
