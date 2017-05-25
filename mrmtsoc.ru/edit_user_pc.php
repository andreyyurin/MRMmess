<?php
	session_start();
	if(isset($_POST['btn_set']) && isset($_SESSION['login']) && isset($_SESSION['pass']))
	{
			require 'coder.php';
			$response = array($_POST['name'], $_POST['surname'], $_POST['country'], $_POST['city'], $_POST['age'], $_POST['pass'], $_POST['reppass']);

				require 'db_connect.php';
				$db = new DB_CONNECT();
				
				$login = $_SESSION['login']; 	
				$password = $_SESSION['pass']; 	
				
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
							if($response[5]==$response[6])
							{	
								$lb = mysql_query("UPDATE users 
										SET password='".$response[5]."'
												WHERE login = '$login' AND password = '$password' ");
							}else{
								echo "<script> window.location='settings.php?error=7'; </script>";
								exit();
							}
						}

				echo "<script> window.location='profile.php'; </script>";
	}
?>