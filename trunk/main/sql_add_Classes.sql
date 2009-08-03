CREATE TABLE `Classes` (
  `KID` int(10) unsigned NOT NULL,
  `KName` varchar(5) NOT NULL,
  `COY` smallint(6) NOT NULL default '2000' COMMENT 'Abijahrgang',
  `Year` smallint(5) unsigned NOT NULL default '2000',
  `LID` bigint(20) unsigned default NULL,
  `Subjects` set('Sonst','Rev','Rrk','Eth','D','G','Ek','Gk','E','F','L','M','NP','Ph','Ch','B','Sp','Mu','Bk','NWT','F3','L3','Ru3') NOT NULL,
  PRIMARY KEY  (`KID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COMMENT='Ergänzt Locations'
