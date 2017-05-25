<?php
  session_start();
  

?>

<head>
	<script src="https://code.jquery.com/jquery.min.js"></script>
	<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
	<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
	<script> function getName (str){
    if (str.lastIndexOf('\\')){
        var i = str.lastIndexOf('\\')+1;
    }
    else{
        var i = str.lastIndexOf('/')+1;
    }						
    var filename = str.slice(i);			
    var uploaded = document.getElementById('fileformlabel');
    uploaded.innerHTML = filename;
}</script>
  <title>Моя страница</title>
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

	<form style=" position: absolute; margin-top: 14%; margin-left: 25%;" enctype="multipart/form-data" action="upload_pc.php" method="POST">
	    <input type="hidden" name="MAX_FILE_SIZE" value="3000000" />
		<div class="fileform " >
			<div id="fileformlabel"></div>
			<div class = "btnupl">
			<div class="btn btn-info">
				<div  class="selectbutton">Choose new photo</div>
					<input  id="upload" type="file" name="userfile" onchange="getName(this.value);" />
				</div>
			</div>
		</div>		
		<input type="submit" class="btn btn-info" style="margin-top:5%;" value="Upload photo" />
	</form>



  <div style="width: 60%; height: 30%; "><img style="width: 100%; height: 100%;" src=<?php if(file_exists("upload/".$_SESSION['login'].".jpeg"))
		{
			echo "upload/".$_SESSION['login'].".jpeg"; 
		}else{
			echo "ava_add.png"; 
		} ?> />
  </div>


  <div style="margin-right:55%; margin-top: 3%;text-align: center;">
    <font face="monospace" size="4"><div><?php
      	require 'coder.php';
		$response = array();
	
	//Считывание

	if(!isset($_SESSION['login']) || !isset($_SESSION['login']))
	{
		echo "<script> window.location='index.php'; </script>";
		exit();
	}
	    $login = $_SESSION['login'];
	    $password = $_SESSION['pass'];   
 
 
    require 'db_connect.php';
	
	//Подключение
    $db = new DB_CONNECT();
	$lb = mysql_query("SELECT name, surname
								FROM users
								WHERE login = '$login' AND password = '$password' ");
	
	while($array = mysql_fetch_assoc($lb))
	{
		//Вывод
		echo $array['name']." ".$array['surname'];
    }
       ?></div>
      <div><?php
			$lb = mysql_query("SELECT age
										FROM users
										WHERE login = '$login' AND password = '$password' ");
		
			while($array = mysql_fetch_assoc($lb))
			{
				//Вывод
				echo $array['age'];
		    }
	       ?></div>
      <div><?php
			$lb = mysql_query("SELECT city, country
										FROM users
										WHERE login = '$login' AND password = '$password' ");
		
			while($array = mysql_fetch_assoc($lb))
			{
				//Вывод
				echo $array['country'].", ".$array['city'];
		    }
	       ?></div>
      <div style=""><?php
			echo $login;
			
	       ?></div>
    </font>
  </div>

</div>
</body>
<style type="text/css">
.btnupl {
	margin-top: 0%;
	text-align: left;
}
.fileform { 
    background-color: #FFFFFF;
    cursor: pointer;
    height: 50%;
    overflow: hidden;
    position: relative;
    text-align: left;
    vertical-align: middle;
    width: 300%;
}
 .fileform #fileformlabel { 
	background-color: #FFFFFF;
	line-height: 4%;
	overflow: hidden;
	padding-top: 3%;
	padding-bottom: 3%;
	float: left;
	width:300%;
}
.fileform .selectbutton { 
    height: 1%;
    width: 30%;
}
 
.fileform #upload{
    position:absolute; 
    top:0; 
    left:0; 
    width:100%; 
    -moz-opacity: 0; 
    filter: alpha(opacity=0); 
    opacity: 0; 
    font-size: 150px; 
    height: 50%; 
    z-index:20;
} 
 </style>