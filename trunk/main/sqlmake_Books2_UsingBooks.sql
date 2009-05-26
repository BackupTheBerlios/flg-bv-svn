CREATE TABLE `UsingBooks` (
`ISBN` BIGINT UNSIGNED NOT NULL,
`BCID` INT  DEFAULT NULL COMMENT 'BookClassID	gibt Klassen von äquivalenten ISBNs an, ohne NULL.',
`Grades` SET NOT NULL COMMENT 'Klassenstufen in denen das Buch verwendet wird',
`Subject` SET NOT NULL COMMENT 'Fächer / Funktionen in denen das Buch verwendet wird',
 PRIMARY KEY (`ISBN`)
)
ENGINE = MyISAM
COMMENT = 'Definiert Äqivalenzklassen von Büchern und deren Verwendung in Fach und Klasse';
