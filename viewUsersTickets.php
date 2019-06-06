<?php
require "conn.php";
$id = $_GET["id"];
//$amount = $_POST["amount"];
//$order_id = $_POST["order_id"];

$sql = "select * from ticket where user_id = '$id';";

$result= mysqli_query($conn,$sql);

$response = array();

while($row = mysqli_fetch_array($result))
{
array_push($response,array("id"=>$row[0],"arena"=>$row[1],"date"=>$row[2],"name"=>$row[3],"price"=>$row[4],"time"=>$row[5],"user_id"=>$row[6],"latitude"=>$row[7],"longitude"=>$row[8]));
}
echo json_encode(array("server_response"=>$response));

mysqli_close($conn);

?>