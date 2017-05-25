<?php

				require 'db_connect.php';
				$db = new DB_CONNECT();
				//Считывание
				$password = $_GET['pass'];
				$login1 = $_GET['login1']; 
				
				$lb = mysql_query("SELECT login
											FROM users
											WHERE password = '$password' AND login = '$login1'");
												
					if(mysql_num_rows($lb)) //Если такой логин и пароль есть
					{							
						$array = array();
						//Проверка
						$lb = mysql_query("SELECT login2
								FROM messages
								WHERE login1 = '$login1'");
						$text = '{'.'"messes": [';
						while($result=mysql_fetch_array($lb)):
							$lb2 = mysql_query("SELECT name, surname
								FROM users
								WHERE login = '".$result['login2']."'");
							$result2=mysql_fetch_array($lb2);
							
							$text = $text.'{'.'"'."userlog".'"'.':'.'"'.$result['login2'].'","'."name".'"'.':"'.$result2['name'].'","'."surname".'":"'.$result2['surname'].'"'.'},';
						endwhile;
						
						$lb = mysql_query("SELECT login1
								FROM messages
								WHERE login2 = '$login1'");
						
						while($result=mysql_fetch_array($lb)):
							$lb2 = mysql_query("SELECT name, surname
									FROM users
									WHERE login = '".$result['login1']."'");
							$result2=mysql_fetch_array($lb2);
							$text = $text.'{'.'"'."userlog".'"'.':'.'"'.$result['login1'].'","'."name".'"'.':"'.$result2['name'].'","'."surname".'":"'.$result2['surname'].'"'.'},';
						endwhile;
								
						$text = substr($text, 0, -1);
						$text = $text.']'.'}';
						//Вывод
						echo $text;
					}
	
		
?>