<?php

	$pseudo = $_GET['pseudo'];

	$connexion = pg_connect("host=localhost port=5432 dbname=projet user=aurelien password=******");
	
	$sql = pg_query($connexion,"select elo from joueur where pseudo = '$pseudo'");
	
	$data = pg_fetch_array($sql);

	echo $data['elo'];





?>
