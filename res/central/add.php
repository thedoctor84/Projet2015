<?php	
	$identifiant = $_GET['name'];
        $mdp = $_GET['mdp'];
        $email = $_GET['mail'];
        $country = $_GET['pays'];

		
        if(isset($identifiant) && isset($mdp) && isset($email) && isset($country))
        {
		
                $connexion = pg_connect("host=localhost port=5432 dbname=projet user=aurelien password=*******");

                $sql = pg_query($connexion,"INSERT INTO joueur (pseudo,elo,nbwin,nbloose,mdp,pays,mail) values ('$identifiant',0,0,0,'$mdp','$country','$email')");

        }




?>
              
