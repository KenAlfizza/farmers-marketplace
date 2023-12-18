<?php
require "../../database/DbCarts.php";
$db = new Carts();

if ($db->dbConnect()) {
    $result = $db->updateCart($_POST['user_id'], $_POST['stores'], $_POST['products']);

} else $result = "Error: Database Connection";

header('Content-Type: application/json');
echo json_encode($result);
    
?>