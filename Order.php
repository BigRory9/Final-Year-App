<?php
class Order {
    private $db;

    public function __construct(){
        $this->db = new database;
    }

    // public function getId(){

    public function addOrder($data){
        //Prepare Query
        $this->db->query('INSERT INTO orders (user_id) VALUES (:user_id)');
        //Bind Values
        $this->db->bind(':user_id', $data['user_id']);

        //Execute
        if($this->db->execute()){
            return true;
        }
        else{
            return false;
        }
    }
}
?>