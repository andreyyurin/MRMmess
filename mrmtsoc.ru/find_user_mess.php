<?php
	require 'coder.php';
	$response = array();

    $login = $_GET['login'];  
 
    require 'db_connect.php';
	
	//��������
    $db = new DB_CONNECT();
	$lb = mysql_query("SELECT login, name, surname, city, country, age
								FROM users
								WHERE login = '$login'");
	
	if(mysql_num_rows($lb)) //���� ����� ����� ����
	{		
		//�����
		while($array = mysql_fetch_assoc($lb))
		{
			$response['name'] = $array['name'];
			$response['surname'] = $array['surname'];
			echo json_encode($response);
		}
	}else{
		echo "7"; //������ ������ ���
	}
?>