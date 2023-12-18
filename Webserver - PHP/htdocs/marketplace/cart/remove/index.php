<?php
require "../../database/DbCarts.php";
$db = new Carts();

if ($db->dbConnect()) {
    $result = $db->removeCartProduct($_POST['user_id'], $_POST['product_id']);
} else echo "Error: Database Connection";

header('Content-Type: application/json');
echo json_encode($result);

?>