<?php
error_reporting(E_ERROR | E_PARSE);
require '../../database/DbAccounts.php';
require '../../database/DbStores.php';
require '../../database/DbProducts.php';
$dbaccount = new Accounts();
$dbstores = new Stores();
$dbproduct = new Products();


$auth_account = false;
$auth_store = false;
if (isset($_POST['user_id'])
    && isset($_POST['user_password'])
    && isset($_POST['store_id'])
    && isset($_POST['product_id'])) {
    if ($dbaccount->dbConnect()) {
        $auth_account = $dbaccount->userAuth($_POST['user_id'],$_POST['user_password']);
    }
    if ($dbstores->dbConnect()) {
        if ($auth_account) {
            $user_store = $dbstores->storeRetrieveByUserId($_POST['user_id']);
            if ($user_store["id"] == $_POST['store_id']) {
                $auth_store = true;
            }
        }
    }
    if ($auth_account && $auth_store) {
        if ($dbproduct->dbConnect()) {
            $remove = $dbproduct->productRemove($_POST['product_id']);
            if ($remove) {
                $result['status'] = "success";
            } else {
                $result['status'] = "failed";
            }   
        } $result['status'] = "failed";
    } else {
        $result['status'] = "unauthorized";
    }
} 
header('Content-Type: application/json');
echo json_encode($result);

?>