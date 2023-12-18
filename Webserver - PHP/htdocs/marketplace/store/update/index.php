<?php
require "DbStores.php";
$db = new Stores();

if (isset($_POST['field'])
&& isset($_POST['data'])) {
    if ($db->dbConnect()) {
        $result = $db->storeUpdate("stores", $_POST['field'], $_POST['data']);
        if ($result == true) {
            echo "success";
        } else {
            echo "failed";
        }
    } else echo "con_err";
} else echo "field_req";



?>