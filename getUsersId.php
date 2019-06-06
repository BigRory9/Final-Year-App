<?php
require "conn.php";
//$id = $_POST["id"];
//$amount = $_POST["amount"];
//$order_id = $_POST["order_id"];

$choice = $_GET['email'];
$sql = "select id from user where email = '$choice';";

$result= mysqli_query($conn,$sql);

$response = array();

while($row = mysqli_fetch_array($result))
{
array_push($response,array("id"=>$row[0]));
}
echo json_encode(array("server_response"=>$response));

mysqli_close($conn);
?>