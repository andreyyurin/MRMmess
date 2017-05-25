<?php
  session_start();
  if(isset($_SESSION['login']) && isset($_SESSION['pass']))
  {
      header ('Location: profile.php');  // ïåðåíàïðàâëåíèå íà íóæíóþ ñòðàíèöó
      exit();
  }

 ?>

<!DOCTYPE html>
<html>
<head>
  <script src=""></script>
  <script src="https://code.jquery.com/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
  <!--<script src="script.js"></script>-->

  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">

  <link href="style.css" rel="stylesheet">
  <!-- <link href="bootstrap-3.3.6-dist/css/bootstrap.css"> -->

  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <title>MRM</title>

</head>
<body>
  <div class="logo" style="text-align: center;"><h1>Logotype</h1></div>
  <div class="alert alert-warning hidden" id="error"></div>


  <div>
    <label><input type="checkbox" name="sigh_in" onchange="change_field();"> Вход</label>
    <!-- Sigh in -->
    <div id="field_log" style="display: none;">
      <form method="post" action="auth_acc_pc.php" class="form-horizontal" style="width: 500px; margin: auto; float: left; display: inline-block; margin-top: 50px; margin-left: 50px;">
        <div class="form-group">
          <label for="user_login" class="col-sm-2 control-label">Логин</label>
          <div class="col-sm-10">
            <input type="text" class="form-control" id="user_login" name="user_login" placeholder="Введите логин...">
          </div>
        </div>
        <div class="form-group">
          <label for="user_password" class="col-sm-2 control-label">Пароль</label>
          <div class="col-sm-10">
            <input type="password" class="form-control" id="user_password" name="user_password" placeholder="Введите пароль...">
          </div>
        </div>
        <div class="form-group">
          <div class="col-sm-offset-2 col-sm-10">
            <div class="checkbox">
              <label>
                <input type="checkbox"> Запомнить меня
              </label>
            </div>
          </div>
        </div>
        <div class="form-group">
          <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" name="send_param" class="btn btn-default">Вход</button>
          </div>
        </div>
      </form>
    </div>

    <label><input type="checkbox" name="sigh_up" onchange="change_field();"> Регистрация</label>
    <div id="field_reg" style="display: none;">
      <!-- Registration -->
      <form method="post" action="reg_acc_pc.php" class="form-horizontal" style="width: 500px; margin: auto; display: inline-block; margin-left: 50px; margin-top: 50px;">
        <div class="form-group">
          <label for="user_login_reg" class="col-sm-2 control-label">Логин</label>
          <div class="col-sm-10">
            <input type="text" class="form-control" id="user_login_reg" name="user_login_reg" placeholder="Введите логин...">
          </div>
        </div>
        <div class="form-group">
          <label for="user_password_reg" class="col-sm-2 control-label">Имя</label>
          <div class="col-sm-10">
            <input type="text" class="form-control" id="user_name_reg" name="user_name_reg" placeholder="Введите имя...">
          </div>
        </div>
        <div class="form-group">
          <label for="user_password_reg" class="col-sm-2 control-label">Фамилия</label>
          <div class="col-sm-10">
            <input type="text" class="form-control" id="user_surname_reg" name="user_surname_reg" placeholder="Введите фамилия...">
          </div>
        </div>
        <div class="form-group">
          <label for="user_password_reg" class="col-sm-2 control-label">Возраст</label>
          <div class="col-sm-10">
            <input type="number" class="form-control" id="user_age_reg" name="user_age_reg" placeholder="Введите Ваш возраст...">
          </div>
        </div>
        <div class="form-group">
          <label for="user_password_reg" class="col-sm-2 control-label">Город</label>
          <div class="col-sm-10">
            <input type="text" class="form-control" id="user_city_reg" name="user_city_reg" placeholder="Город проживания...">
          </div>
        </div>
        <div class="form-group">
          <label for="user_password_reg" class="col-sm-2 control-label">Страна</label>
          <div class="col-sm-10">
            <input type="text" class="form-control" id="user_country_reg" name="user_country_reg" placeholder="Страна проживания...">
          </div>
        </div>
        <div class="form-group">
          <label for="user_password_reg" class="col-sm-2 control-label">Пароль</label>
          <div class="col-sm-10">
            <input type="password" class="form-control" id="user_password_reg" name="user_password_reg" placeholder="Введите пароль...">
          </div>
        </div>
        <div class="form-group">
          <label for="user_password_repeat" class="col-sm-2 control-label">Повторите пароль</label>
          <div class="col-sm-10">
            <input type="password" class="form-control" id="user_password_repeat_reg" name="user_password_repeat_reg" placeholder="Введите пароль...">
          </div>
        </div>
        <div class="form-group">
          <div class="col-sm-offset-2 col-sm-10">
            <div class="checkbox">
              <label>
                <input type="checkbox"> Согласен с условиями пользования
              </label>
            </div>
          </div>
        </div>
        <div class="form-group">
          <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-default" name="reg_btn">Регистрация</button>
          </div>
        </div>
      </form>
    </div>
  </div>
<script>
function change_field(){
  var sigh_in = document.getElementsByName('sigh_in')[0];
  var sigh_up = document.getElementsByName('sigh_up')[0];

  var field_log = document.getElementById('field_log');
  var field_reg = document.getElementById('field_reg');

  if(sigh_in.checked)
    field_log.setAttribute('style', '');
  else
    field_log.setAttribute('style', 'display:none');

  if(sigh_up.checked)
    field_reg.setAttribute('style', '');
  else
    field_reg.setAttribute('style', 'display:none');
}
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
  switch(error){
    case '5country':
      log = 'Неправильно указана страна проживания!';
      break;
    case '5city':
      log = 'Неправильно указан город проживания!';
      break;
    case '5surname':
      log = 'Некорректно указана фамилия!';
      break;
    case '5name':
      log = 'Некорректно указано имя!';
      break;
    case '5login':
      log = 'Некорректно указан логин!';
      break;
    case '3':
      log = 'Одно или несколько полей не заполнено!';
      break;
    case '0':
      log = 'Неизвестная ошибка!';
      break;
    case '4':
      log = 'Данный логин уже существует!';
      break;
    case '2':
      log = "Пароли не совпадают!"
      break;
    case '8':
      log = "Пароль или логин введены неправильно"
      break;
    default:
      log = '';
      break;
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