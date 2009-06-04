LOAD DATA LOCAL INFILE "~/workspace/buecherv.v0.2/SCHUELER09.csv"  REPLACE
        INTO TABLE Customers  
        FIELDS TERMINATED BY ','
	OPTIONALLY ENCLOSED BY '"'
	LINES TERMINATED BY '\n'
	( FL1,FL2,FL3,@ignore,@ignore,SID,@ignore,CID,@ignore,Name,Vorname )
