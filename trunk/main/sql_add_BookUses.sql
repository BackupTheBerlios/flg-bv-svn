CREATE TABLE `BookUses` (
  `BUID` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `BCID` INT UNSIGNED NOT NULL COMMENT 'Gibt Äquivalenzklassen von ISBNs an',
  `ISBN` BIGINT UNSIGNED ,
  `Grades` SET('5','6','7','8','9','10','J1','J2','S8','S9','S10','N8','N9','N10')  ,
  `Subjects` SET('Sonst','Rev','Rrk','Eth','D','G','Ek','Gk','E','F','L','M','NP','Ph','Ch','B','Sp','Mu','Bk','NWT','F3','L3','Ru3')  ,
  PRIMARY KEY (`BUID`)
)
ENGINE = MyISAM
COMMENT = 'Welche Bücher für welchen Zug in welcher Klasse';
