<?php
require '../../database/DbProducts.php';
$db = new Products();
if(isset($_GET['method'])) {

    switch($_GET['method']) {
       
        case 'type':
            if (isset($_POST['type'])) {
                if ($db->dbConnect()) {
                    $result = $db->productRetrieveListByType("products", $_POST['type']);
                    echo $result;
                } else echo "Error: Database connection";
            } else echo "Product Type required";
        break;

        case 'store':
            if (isset($_POST['store'])) {
                if ($db->dbConnect()) {
                    $result = $db->productRetrieveListByType("products", $_POST['store']);
                } else echo "Error: Database connection";
            } else echo "Product Type required";
        break;

        case 'user':
            if ($db->dbConnect()) {
                $result = $db->productRetrieveListByUserId($_POST['user_id']);
            } else echo "Error: Database connection";
        break;

        case 'user_limit':
            if ($db->dbConnect()) {
                $result = $db->productRetrieveListByUserIdLimit($_POST['user_id'], $_POST['start'], $_POST['end']);
            } else echo "Error: Database connection";
        break;
        
        case 'id':
            if ($db->dbConnect()) {
                $result = $db->productRetrieveListById($_POST['user_id']);
            } else echo "Error: Database connection";
        break;


        case 'all':
            if ($db->dbConnect()) {
                $result = $db->productRetrieveListAll("products");
            } else echo "Error: Database connection";
        break;
    }
}

header('Content-Type: application/json');
echo json_encode($result);

?>