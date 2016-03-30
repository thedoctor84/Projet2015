<?php



	$ip = $_GET['ip'];

	if(isset($ip))
	{
		
		
		$connexion = pg_connect("host=localhost port=5432 dbname=projet user=aurelien password=******");

		$sql = pg_query($connexion,"DELETE FROM current WHERE ip='$ip'");
	
	}


?>
