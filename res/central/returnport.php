<?php

	$connexion = pg_connect("host=localhost port=5432 dbname=projet user=aurelien password=*****");

	$sql = pg_query($connexion,"select port from current FETCH FIRST 1 ROWS ONLY");

	$data = pg_fetch_array($sql);

	echo $data['port'];





?>
