<?php
	session_start();
	$response = array();

	if(isset($_POST['send_param']))	
	{

	    $login =  trim(strip_tags($_POST['user_login']));
		$password = trim(strip_tags($_POST['user_password']));
	    require 'db_connect.php';
		
		//Ïîäêëþ÷åíèå ê ÁÄ, ïðîâåðêà äàííûõ
	    $db = new DB_CONNECT();
		$lb = mysql_query("SELECT login
									FROM users
									WHERE login = '$login' AND password = '$password' ");
										
			if(mysql_num_rows($lb)) //Åñëè òàêîé ëîãèí è ïàðîëü åñòü
			{		
				$_SESSION['login'] = $login;
				$_SESSION['pass'] = $password;
				header ('Location: profile.php');  // ïåðåíàïðàâëåíèå íà íóæíóþ ñòðàíèöó
   				exit();

			}else{
				header ('Location: index.php?error=8');  // перенаправление на нужную страницу
   				exit();//Òàêîãî ëîãèíà èëè ïàðîëÿ íåò
			}
	}
?>