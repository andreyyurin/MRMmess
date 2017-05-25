<?php
	$response = array();

    $login = $_GET['login'];
    $password = $_GET['pass'];   
 
    require 'db_connect.php';
	
	//Подключение к БД, проверка данных
    $db = new DB_CONNECT();
	$lb = mysql_query("SELECT login
								FROM users
								WHERE login = '$login' AND password = '$password' ");
									
		if(mysql_num_rows($lb)) //Если такой логин и пароль есть
		{		
			echo "5";//Такой логин есть
		}else{
			echo "6";//Такого логина или пароля нет
		}
?>