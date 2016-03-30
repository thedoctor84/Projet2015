<?php

	$ip = $_GET['ip'];
	$port = $_GET['port'];	
	if(isset($ip) && isset($port))
	{

		$connexion = pg_connect("host=localhost port=5432 dbname=projet user=aurelien password=******");
		
		$sql = pg_query($connexion,"INSERT INTO current (nbjoueur,nbmax,ip,port) VALUES (0,6,'$ip','$port')" );		

	}







?>
