<?php
require "conn.php";
$id = $_GET["id"];
$sql = "select * from product_order where order_id IN(select order_id from orders where user_id='$id')";

$result= mysqli_query($conn,$sql);

$response = array();

while($row = mysqli_fetch_array($result))
{
array_push($response,array("order_ID"=>$row[0],"product_ID"=>$row[1],"productQuantity"=>$row[2],"code"=>$row[3],"collected"=>$row[4]));
}
echo json_encode(array("server_response"=>$response));

mysqli_close($conn);
?>