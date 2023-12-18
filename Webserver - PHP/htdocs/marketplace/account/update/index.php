<?php
require "../../database/DbAccounts.php";
$db = new Accounts();

if (isset($_POST['field'])
&& isset($_POST['data'])) {
    if ($db->dbConnect()) {
        $result = $db->userUpdate("users", $_POST['field'], $_POST['data']);
    }
}
header('Content-Type: application/json');
echo json_encode($result);





/*
if (isset($_POST['id'])
    && isset($_POST['name'])
    && isset($_POST['email'])
    && isset($_POST['phone'])
    && isset($_POST['password'])) {
    if ($db->dbConnect()) {
        $result = $db->updateUser("user", $_POST['id'], $_POST['name'], $_POST['email'], $_POST['phone'], $_POST['password']);
        echo $result;
    } else echo "Error: Database Connection";
} else echo "All fields required";
*/

?>