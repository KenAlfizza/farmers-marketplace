<?php
require "../../../database/DbProducts.php";
$db = new Products();

if (isset($_POST['store_id'])) {
    if ($db->dbConnect()) {
        $result = $db->productRetrieveListStore($_POST['store_id']);
    }
}

header('Content-Type: application/json');
echo json_encode($result);

?>