<?php


	$identifiant = $_GET['name'];

	if(isset($identifiant))
	{
		$connexion = pg_connect("host=localhost port=5432 dbname=projet user=aurelien password=*****");
		
		$sql1 = pg_query($connexion,"SELECT * FROM joueur WHERE pseudo='$identifiant'");

		$row = pg_fetch_row($sql1);
		
		$pseudo = $row[1];
		$elo = $row[2];
		$nbwin = $row[3];
		$nbloose = $row[4];
		$mdp = $row[5];
		$pays = $row[6];

		$sql2 = pg_query($connexion,"INSERT INTO history (temps,pseudo,elo,nbwin,nbloose,mdp,pays) VALUES (NOW(),'$pseudo','$elo','$nbwin','$nbloose','$mdp','$pays')"); 
			
		$sql = pg_query($connexion,"UPDATE joueur SET nbwin=nbwin+1 WHERE pseudo='$identifiant'");

	}









?>
