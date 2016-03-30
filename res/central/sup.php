<?php


	$identifiant = $_GET['name'];

	if(isset($identifiant))
	{
		$connexion = pg_connect("host=localhost port=5432 dbname=projet user=aurelien password=******");
		
		$sql1 = pg_query($connexion,"DELETE FROM history WHERE pseudo='$identifiant'");		
		$sql = pg_query($connexion,"DELETE FROM joueur WHERE pseudo='$identifiant'");
		
  	

	}







?>
