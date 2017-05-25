<?php
	require 'coder.php';
	$response = array();
	
	//Считывание
    $login = $_GET['login'];
    $password = $_GET['pass'];
    $reppasword = $_GET['reppass'];
    $name = $_GET['name'];
    $surname = $_GET['surname'];
    $age = $_GET['age'];
    $city = $_GET['city'];
    $country = $_GET['country'];
    
 
    require 'db_connect.php';
	
	//Подключени
    $db = new DB_CONNECT();
	$lb = mysql_query("SELECT login
								FROM users
								WHERE login = '$login' ");
									
		if(mysql_num_rows($lb)) //Если такой логин есть
		{		
			echo "4";//Такой логин занят
		}else{
			//Занос данных в БД
			$result = mysql_query("INSERT INTO users(login, password, name, surname, age, city, country) VALUES('$login', '$password', '$name', '$surname', '$age', '$city', '$country')");
	 
			if ($result) {
					echo "1";
				} else {
					echo "0";
				}
			}
			
?>