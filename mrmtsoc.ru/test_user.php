<?php
	require 'coder.php';
	$response = array();
	
	//����������
    $login = $_GET['login'];

    require 'db_connect.php';
	
	//��������
    $db = new DB_CONNECT();
	$lb = mysql_query("SELECT login
								FROM users
								WHERE login = '$login' ");
	//�����					
	if(mysql_num_rows($lb)) //���� ����� ����� ����
	{	
		echo "1";
	}else{
		echo "-1";
	}
?>