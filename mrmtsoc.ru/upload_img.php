<?php
		//Проверка на изображение
			if(isset($_POST['image']))
			{
				require 'db_connect.php';
				$db = new DB_CONNECT();
				$password = $_GET['pass'];
				$id = $_GET['login'];
				//Проверка пароля и логина
				$lb = mysql_query("SELECT login
											FROM users
											WHERE password = '$password' AND login = '$id'");
												
					if(mysql_num_rows($lb)) //Если такой логин и пароль есть
					{		
					//Загрузка
						$upload_folder = "upload";
						$path = "$upload_folder/$id.jpeg";
						$image = $_POST['image'];
						if(file_put_contents($path, base64_decode($image)) != false)
						{
							echo "success";
							exit();
						}else{
							echo "fail";
							exit();
						}
					}
			}else{
				 echo "image_not";
				 exit();
			}
?>