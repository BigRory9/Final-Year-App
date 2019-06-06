<?php
require "conn.php";

$sql = "SELECT * FROM product;"; 

$result= mysqli_query($conn,$sql);

$response = array();

while($row = mysqli_fetch_array($result))
{
array_push($response,array("id"=>$row[0],"name"=>$row[1],"value"=>$row[2],"description"=>$row[3],"type"=>$row[4]));
}
echo json_encode(array("server_response"=>$response));

mysqli_close($conn);
?>