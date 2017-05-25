<?php
	session_start();
?>
	<script src="https://code.jquery.com/jquery.min.js"></script>
	<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
	<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
	<title>Диалоги</title>
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
<?php
	if(isset($_SESSION['login']) && isset($_SESSION['pass']))
	{
		require 'db_connect.php';
		$db = new DB_CONNECT();

		$login = $_SESSION['login'];
		
		$lb = mysql_query("SELECT login1
			FROM messages WHERE login2='$login'  AND message = '$mess' ");
		echo "<div class='vyrovnyat'>";
			echo "<div class = 'dias'>";
				while($result=mysql_fetch_array($lb)):
					if($result['login1']!='/')
					{
						echo "
						
							<form style='display: inline-block;' action='chat_pc.html?login=".$result['login1']."' method='POST'>
								<input class='btn btn-info'  type='submit' value=".$result['login1']." />
							</form>";

						echo "<br/><hr/>";	
					}
				endwhile;
				$mess = "";
				$lb = mysql_query("SELECT login2
					FROM messages WHERE login1='$login' AND message = '$mess' ");
				while($result=mysql_fetch_array($lb)):
					if($result['login2']!='/')
					{
						echo "
							<form style='display: inline-block;' action='chat_pc.html?login=".$result['login2']."' method='POST'>
								<input class='btn btn-info' type='submit' value=".$result['login2']." />
							</form>";

						echo "<br/><hr/>";	
					}
				endwhile;

			echo "</div>";
		echo "</div>";

	}
	
?> 

<style>

.vyrovnyat {
  margin-top: 1%;
  height: 3em;
  margin-right: 65%;
  margin-left: 20%;
}
.dias {
	margin-top: 10%;
}
.vyrovnyat img {
  vertical-align: middle;
}
h1{
	margin-top: 1%;
  height: 3em;
  margin-right: 65%;
  margin-left: 15%;
}
</style>



