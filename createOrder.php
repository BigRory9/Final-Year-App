<?php
require "conn.php";

$choice = $_GET['email'];
$my_sql = "SELECT id FROM user WHERE email LIKE '$choice';";
$sql_my = "SET @@SESSION.information_schema_stats_expiry = 0";
$sql = "select auto_increment from information_schema.TABLES where TABLE_NAME ='orders' and TABLE_SCHEMA='user101'"; 

$response = array(); 
mysqli_query($conn,$sql_my);
$result= mysqli_query($conn,$sql);

while($row = mysqli_fetch_array($result))
{
array_push($response,array("order_id"=>$row[0]));
 }

$res= mysqli_query($conn,$my_sql);
while($row = mysqli_fetch_array($res))
{
array_push($response,array("user_id"=>$row[0]));
}
echo json_encode(array("server_response"=>$response));

mysqli_close($conn);
?>