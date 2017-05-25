<?php
  session_start();
?>


<!DOCTYPE html>
<html>
<head>
	<script src="https://code.jquery.com/jquery.min.js"></script>
	<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
	<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
	<link rel="stylesheet" href="style_settings.css">
	<title>Настройки</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>

 <div style="float: left; margin-top:3%;">
  	<form style="margin-left: 5%; position:absolute; " action = "userfind.php" method = "post">	
	  	<div class="form-group">
				<input type="text" name="search" class="form-control" placeholder="Search..." style="float: left;width:200px;">
				<button name="search_btn" class="btn btn-info" id="search_btn" style="width: 20%; height: 5%; margin-left: 60%; margin-top:1%;" onclick="">Find</button>
		</div>
	</form>
 </div>
<div style="position: absolute; margin-left: 5%; margin-top: 10%;">
	<input type="button" class="btn btn-info" value="Profile" style="float: left; display: inline-block;" onclick="window.location='profile.php'"></br>
	<input type="button" class="btn btn-info" value="Messages" style="float: left; display: inline-block; margin-top: 10px;" onclick="window.location='dias.php'"">
 </div>
<form method="post" action="edit_user_pc.php" class="form-horizontal" style="width: 500px; display: inline-block; margin-left:35%; margin-top: 3%;position: absolute;">
		  <div class="alert alert-warning hidden" id="error" style="width:290px"></div>
		  <div class="form-group">
		    <label for="user_password_reg" class="col-sm-2 control-label">Имя</label>
		    <div class="col-sm-10">
		      <input type="text" class="form-control" id="user_name_reg" name="name" placeholder="Введите имя...">
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="user_password_reg" class="col-sm-2 control-label">Фамилия</label>
		    <div class="col-sm-10">
		      <input type="text" class="form-control" id="user_surname_reg" name="surname" placeholder="Введите фамилия...">
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="user_password_reg" class="col-sm-2 control-label">Возраст</label>
		    <div class="col-sm-10">
		      <input type="number" class="form-control" id="user_age_reg" name="age" placeholder="Введите Ваш возраст...">
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="user_password_reg" class="col-sm-2 control-label">Город</label>
		    <div class="col-sm-10">
		      <input type="text" class="form-control" id="user_city_reg" name="city" placeholder="Город проживания...">
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="user_password_reg" class="col-sm-2 control-label">Страна</label>
		    <div class="col-sm-10">
		      <input type="text" class="form-control" id="user_country_reg" name="country" placeholder="Страна проживания...">
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="user_password_reg" class="col-sm-2 control-label">Пароль</label>
		    <div class="col-sm-10">
		      <input type="password" class="form-control" id="user_password_reg" name="pass" placeholder="Введите пароль...">
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="user_password_repeat" class="col-sm-2 control-label">Повторите пароль</label>
		    <div class="col-sm-10">
		      <input type="password" class="form-control" id="user_password_repeat_reg" name="reppass" placeholder="Введите пароль...">
		    </div>
		  </div>
		  <div class="form-group">
		    <div class="col-sm-offset-2 col-sm-10">
		      <button type="submit" class="btn btn-default" name="btn_set">Изменить</button>
		    </div>
		  </div>
	</form>
	<div style=" position: absolute; margin-top: 3%; margin-left: 75%;">
		    <button class="btn btn-info" name="settings_btn" id="settings_btn" style="width: 100%;" onclick="window.location='settings.php'">Settings</button>
      		<button class="btn btn-info" name="exit_btn" id="exit_btn" style="width: 100%; margin-top:2%;" onclick="window.location='exit.php'" >Exit</button>
	</div>

<script>
function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    return vars;
}
	window.onload = function () {
  var error = getUrlVars()['error'];
  var log = '';
  if(error == 7){
  	log = "Пароли не совпадают!"
  }
  if(log != ''){
  	var field = document.getElementById('error');
  	field.innerHTML = log;
	field.setAttribute('class', 'alert alert-warning');
  }
}
</script>
</body>
</html>