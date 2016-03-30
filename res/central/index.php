<?php



	$connexion = pg_connect("host=localhost port=5432 dbname=projet user=aurelien password=*****");

	$sql = pg_query($connexion, "select * from joueur");
          
	while($row = pg_fetch_row($sql))
	{
		echo $row[0];
		echo " ";
		echo $row[1];
		echo " ";
		echo $row[2];
		echo " ";
		echo $row[3];
		echo " ";
		echo $row[4];
		echo " ";
		echo $row[5];
		echo " ";
		echo $row[6];
		echo ":";
		echo "\n";



	}	


?>
