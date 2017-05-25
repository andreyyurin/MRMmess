<?php
	require 'coder.php';
	$response = array();
	
	//Считывание
    $login = $_GET['login'];

    require 'db_connect.php';
	
	//Проверка
    $db = new DB_CONNECT();
	$lb = mysql_query("SELECT login
								FROM users
								WHERE login = '$login' ");
	//Вывод					
	if(mysql_num_rows($lb)) //Если такой логин есть
	{	
		echo "1";
	}else{
		echo "-1";
	}
?>