<?php
require __DIR__ . '/../DataBase.php';
$db = new DataBase();
if (isset($_POST['id'])) {
    if ($db->dbConnect()) {
        $result = $db->productRetrieve("products", $_POST['id']);
        echo $result;
    } else echo "Error: Database connection";
} else echo "Product ID required";

?>