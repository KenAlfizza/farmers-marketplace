<?php
require '../../../database/DbProducts.php';
$db = new Products();
if (isset($_POST['type'])){
    if ($db->dbConnect()) {
        $result = $db->productRetrieveByType($_POST['type'], $_POST['current_size'], $_POST['request_size']);
    } 
}
header('Content-Type: application/json');
echo json_encode($result);

?>