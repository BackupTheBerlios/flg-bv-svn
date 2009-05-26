CREATE TABLE `Classes` (
  `ClassID` TINYTEXT  NOT NULL,
  `AbiYear` SMALLINT UNSIGNED NOT NULL,
  `Classname` Tinytext  NOT NULL,
  PRIMARY KEY (`ClassID`)
)
ENGINE = MyISAM
COMMENT = 'Jahrgang und Klassen ';

