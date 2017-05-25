<?php
	session_start();
	if(isset($_POST['reg_btn']))
	{
		require 'coder.php';
		$response = array();
		
		//Считывание
	    $login = $_POST['user_login_reg'];
	    $password = $_POST['user_password_reg'];
	    $reppasword = $_POST['user_password_repeat_reg'];
	    $name = $_POST['user_name_reg'];
	    $surname = $_POST['user_surname_reg'];
	    $age = $_POST['user_age_reg'];
	    $city = $_POST['user_city_reg'];
	    $country = $_POST['user_country_reg'];
	    
		require 'db_connect.php';
		if($password==$reppasword)
		{
			if(!empty($login) && !empty($password) && !empty($reppasword) && !empty($name) && !empty($surname) && !empty($age) && !empty($city) && !empty($country))
			{
				$bool = true;
				test_base_log($login);
				if($bool)
				{
					$bool = true;
					test_base($name);
					if($bool)
					{
						$bool = true;
						test_base($surname);
						if($bool)
						{
							test_base($city);
							if($bool)
							{
								$bool = true;
								test_base($country);
								if($bool)
								{
									//Подключени
								    $db = new DB_CONNECT();
									$lb = mysql_query("SELECT login
																FROM users
																WHERE login = '$login' ");
																	
										if(mysql_num_rows($lb)) //Если такой логин есть
										{		
											header ('Location: index.php?error=4');  // перенаправление на нужную страницу
   											exit();//Такой логин занят
										}else{
											//Занос данных в БД
											$result = mysql_query("INSERT INTO users(login, password, name, surname, age, city, country) VALUES('$login', '$password', '$name', '$surname', '$age', '$city', '$country')");
									 
											if ($result) {
												$_SESSION['login'] = $login;
												$_SESSION['pass'] = $password;
												header ('Location: profile.php');  // перенаправление на нужную страницу
   												exit();
												} else {
													header ('Location: index.php?error=0');  // перенаправление на нужную страницу
   													exit();
												}
										}
								}else{
									header ('Location: index.php?error=5country');  // перенаправление на нужную страницу
   									exit();
								}
							}else{
								header ('Location: index.php?error=5city');  // перенаправление на нужную страницу
   								exit();
							}
						}else{
							header ('Location: index.php?error=5surname');  // перенаправление на нужную страницу
   							exit();
						}
					}else{
						header ('Location: index.php?error=5name');  // перенаправление на нужную страницу
   						exit();
					}
				}else{
					header ('Location: index.php?error=5login');  // перенаправление на нужную страницу
   					exit();
				}
			}else{
				//Одно из полей не заполнено
				header ('Location: index.php?error=3');  // перенаправление на нужную страницу
   				exit();
			}
		}else{
			 //Пароли не совпадают
			header ('Location: index.php?error=2');  // перенаправление на нужную страницу
   			exit();
		}


	}

	function test_base($text)
    {
        for($i = 0; $i<strlen($text); $i++)
        {
            if((ord($text[$i])>=128 && ord($text[$i])<=175) || (ord($text[$i])>=65 && ord($text[$i])<=90) || (ord($text[$i])>=97 && ord($text[$i])<=122))
            {
            }else{
                $bool = false;
                break;
            }
        }
    }

    function test_base_log($text)
    {
    	//echo "<script> var log = ".strlen($text).";console.log(log);</script>";
        for($i = 0; $i<strlen($text); $i++)
        {
        	//echo "<script> var log = ".	$text[$i].";console.log(log);</script>";
            if((ord($text[$i])>=48 && ord($text[$i])<=57) || (ord($text[$i])>=65 && ord($text[$i])<=90) || (ord($text[$i])>=97 && ord($text[$i])<=122))
            {
            }else{
            	//echo "<script> var log = ".ord($text[$i]).";console.log(log);</script>";
                $bool = false;
                break;
            }
        }

    }
			
?>