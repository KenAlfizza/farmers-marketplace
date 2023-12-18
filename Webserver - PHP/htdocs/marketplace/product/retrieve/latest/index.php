<?php
require '../../../database/DbProducts.php';
$db = new Products();
if ($db->dbConnect()) {
    $result = $db->productRetrieveLatest();
} 

header('Content-Type: application/json');
echo json_encode($result);

?>