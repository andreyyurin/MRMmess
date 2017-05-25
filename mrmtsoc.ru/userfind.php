<?php
  session_start();
  require 'db_connect.php';
  $db = new DB_CONNECT();
?>

<head>
	<script src="https://code.jquery.com/jquery.min.js"></script>
	<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
	<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">


  <title><?php if(isset($_SESSION['login']) && isset($_SESSION['pass']))
  {
  	$login = $_POST['search'];
  	$lb = mysql_query("SELECT name, surname
								FROM users
								WHERE login = '$login'");
  	while($array = mysql_fetch_assoc($lb))
	{
		//Вывод
		echo $array['name']." ".$array['surname'];
    }

  	}else{
  		header ('Location: index.php');  // ïåðåíàïðàâëåíèå íà íóæíóþ ñòðàíèöó
   		exit();
  		} ?>
  	</title>
</head>
<body>
<div style="margin-left:37%">

	<div style="float: left; margin-top:3%;">
  	<form method = "post" style="margin-left: -32%; position: absolute;" action = "userfind.php">	
	  	<div class="form-group">
				<input type="text" name="search" class="form-control" placeholder="Search..." style="float: left;width:200px;">
				<button class="btn btn-info" name="search_btn" id="search_btn" style="width: 20%; height: 5%; margin-left: 60%; margin-top:1%;" onclick="">Find</button>
		</div>
	</form>
 </div>


 <div style="position: absolute; margin-left: -32%; margin-top: 10%;">
		<input type="button" class="btn btn-info" value="Profile" style="float: left; display: inline-block;" onclick="window.location='profile.php'" ></br>
		<input type="button" class="btn btn-info" value="Messages" style="float: left; display: inline-block; margin-top: 10px;" onclick="window.location='dias.php'">
	 </div>
   <div>
	 </div>
	 <div style="width: 500px;">
 		<div style=" position: absolute; margin-top: 2%; margin-left: 35%;">
	 		<button class="btn btn-info" name="settings_btn" id="settings_btn" style="width: 100%;" onclick="window.location='settings.php'">Settings</button>
      		<button class="btn btn-info" name="exit_btn" id="exit_btn" style="width: 100%; margin-top:2%;" onclick="window.location='exit.php'" >Exit</button>
	</div>


  <div style="width: 60%; height: 30%;display: inline-block;"><img style="width: 100%; height: 100%;" src=<?php 

  $login2=$_POST['search'];
	$lb = mysql_query("SELECT login
								FROM users
								WHERE login = '$login2' ");
	//Âûâîä					
	if(mysql_num_rows($lb)) //Åñëè òàêîé ëîãèí åñòü
	{	
		if(file_exists("upload/".$login2.".jpeg"))
		{
			echo "upload/".$login2.".jpeg"; 
		}else{
			echo "ava_add.png"; 
		}
	}else{
		echo "nouser.png"; 
	}

  ?> />
  </div>


  <div style="margin-right:55%; margin-top: 3%;text-align: center;">
    <font face="monospace" size="4">
      <div><?php
      	require 'coder.php';
		$response = array();
	
	//Считывание
 
	//Подключение
    
	$lb = mysql_query("SELECT name, surname
								FROM users
								WHERE login = '$login'");
	
	while($array = mysql_fetch_assoc($lb))
	{
		//Вывод
		echo $array['name']." ".$array['surname'];
    }
       ?></div>
      <div><?php
			$lb = mysql_query("SELECT age
										FROM users
										WHERE login = '$login'");
		
			while($array = mysql_fetch_assoc($lb))
			{
				//Вывод
				echo $array['age'];
		    }
	       ?></div>
      <div><?php
			$lb = mysql_query("SELECT city, country
										FROM users
										WHERE login = '$login'");
		
			while($array = mysql_fetch_assoc($lb))
			{
				//Вывод
				echo $array['country'].", ".$array['city'];
		    }
	       ?></div>
      <div style=""><?php
			$lb = mysql_query("SELECT login
										FROM users
										WHERE login = '$login'");
		
			while($array = mysql_fetch_assoc($lb))
			{
				//Вывод
				echo $array['login'];
		    }
	       ?></div>
    </font>
  </div>

</div>

</body>

		
	<form action='chat_pc.html?login=<?= $login2 ?>' style='margin-left: 7%; margin-top: 2%' method='POST'>
		<input type='submit' value="Send message" />
	</form>;	