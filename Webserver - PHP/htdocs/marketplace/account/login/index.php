<?php
require "../../database/DbAccounts.php";
$db = new Accounts();

if (isset($_POST['email'])
    && isset($_POST['password'])) {
        if ($db->dbConnect()) {
            $result = $db->userLoginJson($_POST['email'], $_POST['password']);
        } else $result = "error_connection";
} else $result = "error_fields";

header('Content-Type: application/json');
echo json_encode($result);

?>