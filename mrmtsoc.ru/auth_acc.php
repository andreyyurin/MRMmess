<?php
	$response = array();

    $login = $_GET['login'];
    $password = $_GET['pass'];   
 
    require 'db_connect.php';
	
	//����������� � ��, �������� ������
    $db = new DB_CONNECT();
	$lb = mysql_query("SELECT login
								FROM users
								WHERE login = '$login' AND password = '$password' ");
									
		if(mysql_num_rows($lb)) //���� ����� ����� � ������ ����
		{		
			echo "5";//����� ����� ����
		}else{
			echo "6";//������ ������ ��� ������ ���
		}
?>