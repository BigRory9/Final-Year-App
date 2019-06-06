<?php
require "conn.php";
$id = $_POST["id"];
$amount = $_POST["amount"];
$order_id = $_POST["order_id"];
$pin = $_POST["pin"];

$query = "INSERT INTO product_order(order_id,product_id,productQuantity,code,collected) values ('$order_id','$id','$amount','$pin',0);";
if($conn->query($query)=== TRUE){
	
}
else
{
	echo "ERROR: " .$query . "<br>" . $conn->error;
}
?>