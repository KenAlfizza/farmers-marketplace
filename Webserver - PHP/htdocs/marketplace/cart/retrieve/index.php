<?php
require "../../database/DbCarts.php";
$db = new Carts();

if ($db->dbConnect()) {
    $result = $db->retrieveCart($_POST['user_id']);
} else echo "Error: Database Connection";

header('Content-Type: application/json');
echo json_encode($result);

?>