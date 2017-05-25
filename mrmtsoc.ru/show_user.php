<?php
	require 'coder.php';
	$response = array();
	
	//Считывание
    $login = $_GET['login'];
    $password = $_GET['pass'];   
 
 
    require 'db_connect.php';
	
	//Подключение
    $db = new DB_CONNECT();
	$lb = mysql_query("SELECT login, name, surname, city, country, age
								FROM users
								WHERE login = '$login' AND password = '$password' ");
	
	while($array = mysql_fetch_assoc($lb))
	{
		//Вывод
		$response['login'] = $array['login'];
		$response['name'] = $array['name'];
		$response['surname'] = $array['surname'];
		$response['city'] = $array['city'];
		$response['country'] = $array['country'];
		$response['age'] = $array['age'];
     //  echo $array['login']."**".$array['name']."**".$array['surname']."**".$array['city']."**".$array['country']."**".$array['age'];
		echo json_encode($response);
    }
		 
	//$array = mysql_fetch_assoc($lb);
	//echo $array['login']."<br />".$array['name']."<br />".$array['surname']."<br />".$array['city']."<br />".$array['country']."<br />".$array['age'];
?>