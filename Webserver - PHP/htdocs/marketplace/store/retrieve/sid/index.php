<?php
require "../../../database/DbStores.php";
$db = new Stores();

if (isset($_POST['store_id'])) {
    if ($db->dbConnect()) {
        $result = $db->storeRetrieveByStoreId($_POST['store_id']);
    }
}

header('Content-Type: application/json');
echo json_encode($result);

?>