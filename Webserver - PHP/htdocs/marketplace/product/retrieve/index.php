<?php
require '../../database/DbProducts.php';
$db = new Products();
if (isset($_POST['product_id'])) {
    if ($db->dbConnect()) {
        $result = $db->productRetrieveById($_POST['product_id']);
    }
}

header('Content-Type: application/json');
echo json_encode($result);

?>