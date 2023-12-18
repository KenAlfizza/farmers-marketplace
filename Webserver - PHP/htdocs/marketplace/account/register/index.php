<?php
require "../../database/DbAccounts.php";
$db = new Accounts();

if (
    isset($_POST['id'])
    && isset($_POST['name'])
    && isset($_POST['email'])
    && isset($_POST['phone'])
    && isset($_POST['password'])
) 
{
    if ($db->dbConnect()) {
        $result = $db->userRegister($_POST['id'], $_POST['name'], $_POST['email'], $_POST['phone'], $_POST['password']);
    }
}


header('Content-Type: application/json');
echo json_encode($result);


?>