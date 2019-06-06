<?php
$db_name = "user101";
$mysql_username = "root";
$mysql_password = "root";
$server_name="localhost";
$conn = mysqli_connect($server_name,$mysql_username,$mysql_password,$db_name);
if($conn){

}
else
{
	echo "connection faliure";
}
?>