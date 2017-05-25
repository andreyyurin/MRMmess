<?php
	session_start();

	require 'db_connect.php';
	$db = new DB_CONNECT();
		//Подключение к БД, проверка данных
	$password = $_SESSION['pass'];
	$login1 = $_SESSION['login']; 	
	$login2 = $_GET['login'];
	$text = $_POST['key'];
	$lb = mysql_query("SELECT message
			FROM messages WHERE login2='$login1' AND login1 = '$login2' ");
	if(mysql_num_rows($lb)) //Åñëè òàêîé ëîãèí è ïàðîëü åñòü
	{	
		$g = 0;
		while($result=mysql_fetch_array($lb)):
			//	echo "<p>".base64_decode(unparse($result['message']))."</p>";
			//	echo "<br/><hr/>";
			$data[$g] = array($g => $result['message']);
			$g++;
		endwhile;
		echo json_encode($data, 1);
		$dialog1 = $login2."^".$login1;
		if(!empty($text))
		{
			$result = mysql_query ("INSERT INTO messages(login1, login2, id, message)
							VALUES ('$login2', '$login1', '$dialog1', '$text')");
		}
	}else{
		$lb = mysql_query("SELECT message
			FROM messages WHERE login1='$login1' AND login2 = '$login2' ");
		if(mysql_num_rows($lb)) //Åñëè òàêîé ëîãèí è ïàðîëü åñòü
		{	
			$g = 0;
			while($result=mysql_fetch_array($lb)):
			//	echo "<p>".base64_decode(unparse($result['message']))."</p>";
			//	echo "<br/><hr/>";
				$data[$g] = array($g => $result['message']);
				$g++;
			endwhile;
			echo json_encode($data,1);
			$dialog1 = $login1."^".$login2;
			if(!empty($text))
			{
				$result = mysql_query ("INSERT INTO messages(login1, login2, id, message)
							VALUES ('$login1', '$login2', '$dialog1', '$text')");
			}

		}else{
			$dialog1 = $login1."^".$login2;
			$result = mysql_query ("INSERT INTO messages(login1, login2, id)
							VALUES ('$login1', '$login2', '$dialog1')");
		}
	}

?>