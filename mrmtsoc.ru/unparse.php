<?php
	$spr = "#$%";
	require 'db_connect.php';
	$db = new DB_CONNECT();
		//Подключение к БД, проверка данных
		$login1 = "fastvepik";
		$login2 = "Sovest2";	
		$lb = mysql_query("SELECT message
							FROM messages
							WHERE login1 = '$login1' AND login2='$login2'");

		$result=mysql_fetch_array($lb);
						//Вывод
		if(!empty($result['message']))
		{
			$result['message'] = str_replace($spr, '\\', $result['message']);
			$result['message'] = substr($result['message'], 0,-1);
			//$jsonarr = '{'.'"messes": '.'['.$result['message'].']'.'}';
			$jsonarr = '['.$result['message'].']';
		}

		$resp = json_decode($jsonarr, true);
		//echo $jsonarr;
		
		$l = count($resp);
		$s = "";
		for($i=0; $i<$l; $i++)
		{
			echo $resp[$i]['name'].": <br/>".$resp[$i]['message']."<br/>";
		}
	


?>