<?php
require "../../database/DbProducts.php";
$db = new Products();

if ($db->dbConnect()) {
    $result = $db->productUpdate(
        $_POST['user_id'],
        $_POST['store_id'], 
        $_POST['id'], 
        $_FILES['image'], 
        $_POST['name'], 
        $_POST['type'],
        $_POST['price'],
        $_POST['stock'],    
        $_POST['description']);
} else $result['status'] =  "Error: Database Connection";

header('Content-Type: application/json');
echo json_encode($result);

?>