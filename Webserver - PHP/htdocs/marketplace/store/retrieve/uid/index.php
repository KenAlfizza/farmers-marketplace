<?php
require "../../../database/DbStores.php";
$db = new Stores();

if (isset($_POST['user_id'])) {
    if ($db->dbConnect()) {
        $result = $db->storeRetrieveByUserId($_POST['user_id']);
    }
}

header('Content-Type: application/json');
echo json_encode($result);

?>