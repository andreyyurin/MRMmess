<?php
	require 'coder.php';
	$response = array();

    $login = $_GET['login'];  
 
    require 'db_connect.php';
	
	//Проверка данных
    $db = new DB_CONNECT();
	$lb = mysql_query("SELECT login, name, surname, city, country, age
								FROM users
								WHERE login = '$login'");
	
	if(mysql_num_rows($lb)) //Если такой логин есть
	{		
		//Вывод данных
		while($array = mysql_fetch_assoc($lb))
		{
			$response['login'] = $array['login'];
			$response['name'] = $array['name'];
			$response['surname'] = $array['surname'];
			$response['city'] = $array['city'];
			$response['country'] = $array['country'];
			$response['age'] = $array['age'];
			echo json_encode($response);
		}
	}else{
		echo "7"; //Такого логина нет
	}
?>