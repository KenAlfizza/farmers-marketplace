<?php
require "DbAccounts.php";
$db = new Accounts();

if (isset($_POST['id'])) {
    if ($db->dbConnect()) {
        $result = $db->userRetrieve("users", $_POST['id']);
        echo $result;
    } else echo "error_connection";
} else echo "error_fields";

?>