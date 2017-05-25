<?php
  session_start();
	
	 $blacklist = array(".php", ".phtml", ".php3", ".php4");
	 foreach ($blacklist as $item) {
	  if(preg_match("/$item\$/i", $_FILES['userfile']['name'])) {
	   echo "We do not allow uploading PHP files\n";
	   exit;
	   }
	  }

	   /*$uploadfile = "images/".$_FILES['userfile']['name'];
  	   move_uploaded_file($_FILES['userfile']['tmp_name'], $uploadfile);
  	   echo "<script> var c = 0; setInterval(function(){ c++; if(c==2){ window.location='profile.php';}  }, 1000); </script>";*/
  	   $path = 'upload/';
  	   if ($_SERVER['REQUEST_METHOD'] == 'POST')
		{
		//if (!@copy($_FILES['userfile']['tmp_name'], $path . $_FILES['userfile']['name']))
		if (!@copy($_FILES['userfile']['tmp_name'], $path . $_SESSION['login'].".jpeg"))
			echo 'Что-то пошло не так';
		else
			echo "<script> var c = 0; setInterval(function(){ c++; if(c==1){ window.location='profile.php';}  }, 1000); </script>";
		}
?>