<?php
require "conn.php";
$name = $_POST["username"];
$email = $_POST["email"];
$user_pass = $_POST["password"];
$mysql_qry = "INSERT INTO user(username,email,password) values ('$name','$email','$user_pass')";

if($conn->query($mysql_qry)=== TRUE){
  echo "Insert Successful";
}
else{
	echo "ERROR: " . $mysql_qry . "<br>" . $conn->error;
}
?>
