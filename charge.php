<?php
require_once('vendor/autoload.php');
require_once('config/db.php');
require_once('lib/pdo_db.php');
require_once('models/Order.php');
require_once('models/Transaction.php');


\Stripe\Stripe::setApiKey('sk_test_UIcZ6w9lltQi6Vn5VlDCtRk5');

//Sanitize POST Array
$POST = filter_var_array($_POST, FILTER_SANITIZE_STRING);

$first_name =$POST['first'];
$last_name =$POST['last'];
$email =$POST['email'];
$description=$POST['description'];
$order_id=$POST['order_id'];
$user_id=$POST['user_id'];
$price=$POST['price'];
$token =$POST['stripeToken'];

//create customer in stripe
$customer = \Stripe\Customer::create(array(
    "email" =>$email,
    "source" => $token
));

//Charge Customer
$charge = \Stripe\Charge::create(array(
    "amount" => $price,
    "currency" => "EUR",
    "description" => $description,
    "customer" => $customer->id
));

//Order Data
$orderData = [
    "user_id" => intval($user_id)
];

// Instantiate Customer
$order = new Order();

//connection
$conn = mysqli_connect("localhost","root","root","user101");


//Add Order to DB
$order->addOrder($orderData);

//Transaction Data
$transactionData = [
    'id' => $charge->id, 
    'order_id' =>intval($order_id),
    'product'=> $charge->description,
    'amount' => $charge->amount,
    'currency' => $charge->currency,
    'status' => $charge->status
];

// Instantiate Transaction
$transaction = new Transaction();


//Add Transaction to DB
$transaction->addTransaction($transactionData);



// redirect to success
header('Location: success.php?tid='.$charge->id.'&product='.$charge->description);
?>