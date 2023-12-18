<?php
require '../../database/DbStores.php'; // Importing 'DbStores.php' file
$db = new Stores(); // Instantiation of Stores Class

if ($db->dbConnect()) {
    $result = $db->storeRegister($_POST['user_id'],
                                $_POST['store_id'],
                                $_FILES['store_image'],
                                $_POST['store_name'],
                                $_POST['store_email'],
                                $_POST['store_phone'],
                                $_POST['store_province'],
                                $_POST['store_city'],
                                $_POST['store_postcode'],
                                $_POST['store_address'],
                                $_POST['store_description']);
        // $db->storeRegister() is calling the function of 
        // instantiated $db object based on the Stores Class
} else $result =  "Error: Database Connection";

header('Content-Type: application/json');
echo json_encode($result);

?>



