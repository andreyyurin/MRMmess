<?php
		require 'db_connect.php';
		$db = new DB_CONNECT();
		//Подключение к БД, проверка данных
		$password = $_GET['pass'];
		$login1 = $_GET['login1']; 	
		$lb = mysql_query("SELECT login
									FROM users
									WHERE password = '$password' AND login = '$login1'  ");
									
		if(mysql_num_rows($lb)) //Если такой логин и пароль есть
		{		
					require 'coder.php';
					$response = array();
					
					$spr = "#$%";
					
					//Считывание сообщения
					$login2 = $_GET['login2']; 
					$text = $_GET['text'];
					
					$dialog1 = $login1.'^'.$login2;
					$dialog2 = $login2.'^'.$login1;
					
					$lb = mysql_query("SELECT id
							FROM messages
							WHERE login1 = '$login1' AND login2='$login2'");
							
					$result=mysql_fetch_array($lb);	
					
					if(!empty($result['id']))     ///////РАЗ
					{
						
						$lb2 = mysql_query("SELECT name
												FROM users
												WHERE login = '$login1'");
						$result2=mysql_fetch_array($lb2);	
						
						$array['message'] = $text;
						$array['name'] = $result2['name'];
						$array['login'] = $login1;
						
						$text=json_encode($array);
						$text = str_replace('\\', $spr, $text);
						
						//Заполнение
						if(!empty($array['message']))
						{
							$lb = mysql_query("UPDATE messages
													SET message=concat(message, '".$text.","."')
													WHERE login1 = '$login1' AND login2='$login2'");
						}
												
						$lb3 = mysql_query("SELECT message
							FROM messages
							WHERE login1 = '$login1' AND login2='$login2'");
							
						$result=mysql_fetch_array($lb3);
						//Вывод
						if(!empty($result['message']))
						{
							$result['message'] = str_replace($spr, '\\', $result['message']);
							$result['message'] = substr($result['message'], 0,-1);
							echo '{'.'"messes": '.'['.$result['message'].']'.'}';
						}else{
							echo '{'.'"messes":[{"message":"", "name":"", "login":""}]}';
						}
					}else{				
						$lb = mysql_query("SELECT id
							FROM messages
							WHERE login1 = '$login2' AND login2='$login1'");
							
						$result=mysql_fetch_array($lb);	
						if(!empty($result['id']))     ///////РАЗ
						{
							//Проверка, считывание
							$lb2 = mysql_query("SELECT name
													FROM users
													WHERE login = '$login1'");
							$result2=mysql_fetch_array($lb2);	
							
							$array['message'] = $text;
							$array['name'] = $result2['name'];
							$array['login'] = $login1;
							
							$text=json_encode($array);
							$text = str_replace('\\', $spr, $text);
							
							//Заполнение
							if(!empty($array['message']))
							{
								$lb = mysql_query("UPDATE messages
														SET message=concat(message, '".$text.","."')
														WHERE login1 = '$login2' AND login2='$login1'");
							}
													
							$lb = mysql_query("SELECT message
								FROM messages
								WHERE login1 = '$login2' AND login2='$login1'");
							$result=mysql_fetch_array($lb);
							//Вывод
							if(!empty($result['message']))
							{
								$result['message'] = str_replace($spr, '\\', $result['message']);
								$result['message'] = substr($result['message'], 0,-1);
								echo '{'.'"messes": '.'['.$result['message'].']'.'}';
							}else{
								echo '{'.'"messes":[{"message":"", "name":"", "login":""}]}';
							}
						}else{
							$result = mysql_query ("INSERT INTO messages(login1, login2, id)
																VALUES ('$login1', '$login2', '$dialog1')");
							echo '{'.'"messes":[{"message":"", "name":"", "login":""}]}';
							}
					}
		}
?>