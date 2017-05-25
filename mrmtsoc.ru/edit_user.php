<?php
	require 'coder.php';
	$response = array($_GET['name'], $_GET['surname'], $_GET['country'], $_GET['city'], $_GET['age'], $_GET['pass']);

   /* $response[0] = $_GET['name'];
    $response[1] = $_GET['surname'];   
    $response[2] = $_GET['country'];   
    $response[3] = $_GET['city']; 
	$response[4] = $_GET['age']; 
	$response[5] = $_GET['pass'];*/ 
	require 'db_connect.php';
	$db = new DB_CONNECT();
	
	$login = $_GET['login']; 	
	$password = $_GET['password']; 	
//	echo $response[0];
/*	for(int i = 0; i<6; i++)
	{
		echo $response[i];
	}*/
	
	//Изменение данных пользователя
			if(!empty($response[0]))
			{
				$lb = mysql_query("UPDATE users 
						SET name='".$response[0]."'
								WHERE login = '$login' AND password = '$password' ");
			}
			if(!empty($response[1]))
			{
				$lb = mysql_query("UPDATE users 
						SET surname='".$response[1]."'
								WHERE login = '$login' AND password = '$password' ");
			}
			if(!empty($response[2]))
			{
				$lb = mysql_query("UPDATE users 
						SET country='".$response[2]."'
								WHERE login = '$login' AND password = '$password' ");
			}
			if(!empty($response[3]))
			{
				$lb = mysql_query("UPDATE users 
						SET city='".$response[3]."'
								WHERE login = '$login' AND password = '$password' ");
			}
			if(!empty($response[4]))
			{
				$lb = mysql_query("UPDATE users 
						SET age='".$response[4]."'
								WHERE login = '$login' AND password = '$password' ");
			}
			if(!empty($response[5]))
			{
				$lb = mysql_query("UPDATE users 
						SET password='".$response[5]."'
								WHERE login = '$login' AND password = '$password' ");
			}

	/*$lb = mysql_query("UPDATE users 
						SET name='$name', surname='$surname', country='$surname', city='$city', age='$age'
								WHERE login = '$login' AND password = '$password' ");*/
	echo "8";
?>