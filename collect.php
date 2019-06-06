<?php
require "conn.php";
$id = $_POST["order_id"];
$query = "update product_order set collected=1 where order_id='$id';";

if($conn->query($query)=== TRUE){
	
}
mysqli_close($conn);
?>