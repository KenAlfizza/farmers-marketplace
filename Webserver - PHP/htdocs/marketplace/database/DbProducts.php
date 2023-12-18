<?php
require_once "DataBaseConfig.php";
require_once "DbConnect.php";

class Products 
{
    public $connect;
    public $data;
    private $sql;
    protected $servername;
    protected $username;
    protected $password;
    protected $databasename;

    public function __construct()
    {
        $this->connect = null;
        $this->data = null;
        $this->sql = null;
        $dbc = new DataBaseConfig();
        $this->servername = $dbc->servername;
        $this->username = $dbc->username;
        $this->password = $dbc->password;
        $this->databasename = $dbc->databasename;
    }

    function dbConnect()
    {
        $this->connect = mysqli_connect($this->servername, $this->username, $this->password, $this->databasename);
        return $this->connect;
    }

    function prepareData($data)
    {
        return mysqli_real_escape_string($this->connect, stripslashes(htmlspecialchars($data)));
    }
   
    function productRetrieve($table, $id)
    {     
        
        $id = $this->prepareData($id);
        $this->sql = "select * from " . $table . " where id = '" . $id . "'";
        $result = mysqli_query($this->connect, $this->sql);
        $row = mysqli_fetch_assoc($result);
        if (mysqli_num_rows($result) != 0) {
                                   
            $dbcolumn_id = $row['column_id'];
            $dbuser_id = $row['user_id'];
            $dbstore_id = $row['store_id'];
            $dbstore_location = $row['store_location'];
            $dbproduct_id = $row['product_id'];
            $dbproduct_name = $row['product_name'];
            $dbproduct_quantity = $row['product_quantity'];
            $dbproduct_price = $row['product_price'];
            $dbproduct_type	= $row['product_type'];
            $dbproduct_description = $row['product_description'];

            //$dbimage = imageRetrieve($id);
            if ($dbproduct_id == $id) {
                $result = $dbcolumn_id . ";" 
                . $dbuser_id . ";" 
                . $dbstore_id . ";" 
                . $dbstore_location . ";" 
                . $dbproduct_id . ";" 
                . $dbproduct_name . ";" 
                . $dbproduct_quantity . ";" 
                . $dbproduct_price . ";" 
                . $dbproduct_type . ";" 
                . $dbproduct_description;
                //. $dbimage;

            } else $result = "No Product Found";
        } else $result = "No Data Found";
        return $result;
    }

    function productRetrieveListStore($store_id) {
        $store_id = $this->prepareData($store_id);
        $response['products'] = array();
        $product = array();
        $stmt = mysqli_stmt_init($this->connect);
        $stmt->prepare("SELECT * FROM products WHERE " . "store_id = '" . $store_id . "'");
        $stmt->execute();
        try {
            $stmt->bind_result($column_id,
                                $user_id,
                                $store_id,
                                $store_name,
                                $store_city,
                                $product_id,
                                $product_image,
                                $product_name,
                                $product_stock,
                                $product_price,
                                $product_type,
                                $product_description);
                                
            while ($stmt->fetch()) {
                    $product['id'] = $product_id;
                    $product['name'] = $product_name;
                    $product['image'] = $product_image;
                    $product['stock'] = $product_stock;
                    $product['price'] = $product_price;
                    $product['city'] = $store_city;
                    array_push($response['products'], $product);
            }
        } catch (mysqli_sql_exception $e) {
            $response = $e;
        }
        return $response;
    }
    function productRetrieveById($product_id) {
        $product_id = $this->prepareData($product_id);
        
        $product = array();

        $stmt = mysqli_stmt_init($this->connect);
        $stmt->prepare("SELECT * FROM products WHERE " . "product_id". " = '" . $product_id . "'");
        $stmt->execute();
        $stmt->bind_result($column_id,
                            $user_id,
                            $store_id,
                            $store_name,
                            $store_city,
                            $product_id,
                            $product_image,
                            $product_name,
                            $product_stock,
                            $product_price,
                            $product_type,
                            $product_description);
                            
        while ($stmt->fetch()) {
            $product['id'] = $product_id;
            $product['name'] = $product_name;
            $product['image'] = $product_image;
            $product['stock'] = $product_stock;
            $product['price'] = $product_price;
            $product['type'] = $product_type;
            $product['description'] = $product_description;

            $product['store_id'] = $store_id;
            $product['store_name'] = $store_name;
            $product['store_city'] = $store_city;
        }
        return $product;
    }

    function productRetrieveByType($product_type, $current_size, $request_size) {
        $product_type = $this->prepareData($product_type);
        $request_size = $this->prepareData($request_size); // length required default 5
        $current_size = $this->prepareData($current_size); // size of the list
        $response['products'] = array();
        $product = array();

        $stmt = mysqli_stmt_init($this->connect);
        $stmt->prepare("SELECT * FROM products WHERE " . "product_type". " = '" . $product_type . "'");
        $stmt->execute();
        $stmt->bind_result($column_id,
                            $user_id,
                            $store_id,
                            $store_name,
                            $store_city,
                            $product_id,
                            $product_image,
                            $product_name,
                            $product_stock,
                            $product_price,
                            $product_type,
                            $product_description);
    
        $count_request = 0; // number of product list item pushed to the response array
        $count_size = 0; // the number of rows fetched increase size
        while ($stmt->fetch()) {
            $product['id'] = $product_id;
            $product['name'] = $product_name;
            $product['image'] = $product_image;
            $product['stock'] = $product_stock;
            $product['price'] = $product_price;
            $product['type'] = $product_type;
            $product['description'] = $product_description;
            $product['city'] = $store_city;

            $count_size++;
            if ($count_size > $current_size) {
                if ($count_request < $request_size) {
                    array_push($response['products'], $product);
                    $count_request++;
                }
            }
        }
        return $response;
    }

    function productRetrieveLatest() {
        $product = array();
        $stmt = mysqli_stmt_init($this->connect);
        $stmt->prepare("SELECT * FROM products");
        $stmt->execute();
        $stmt->store_result();
        $rows_max = $stmt->num_rows;
        $rows_rand = rand($rows_max-2, $rows_max);

        $stmt->prepare("SELECT * FROM products WHERE column_id = '". $rows_rand ."'");
        $stmt->execute();
        $stmt->bind_result($column_id,
                            $user_id,
                            $store_id,
                            $store_name,
                            $store_city,
                            $product_id,
                            $product_image,
                            $product_name,
                            $product_stock,
                            $product_price,
                            $product_type,
                            $product_description);
                            
        while ($stmt->fetch()) {
            $product['id'] = $product_id;
            $product['name'] = $product_name;
            $product['price'] = $product_price;
            $product['type'] = $product_type;
            $product['city'] = $store_city;
        }
        return $product;
    }

    function productRetrieveByStoreLatest() {
        $product = array();
        $stmt = mysqli_stmt_init($this->connect);
        $stmt->prepare("SELECT * FROM products");
        $stmt->execute();
        $stmt->store_result();
        $rows_max = $stmt->num_rows;
        $rows_rand = rand($rows_max-2, $rows_max);

        $stmt->prepare("SELECT * FROM products WHERE column_id = '". $rows_rand ."'");
        $stmt->execute();
        $stmt->bind_result($column_id,
                            $user_id,
                            $store_id,
                            $store_name,
                            $store_city,
                            $product_id,
                            $product_image,
                            $product_name,
                            $product_stock,
                            $product_price,
                            $product_type,
                            $product_description);
                            
        while ($stmt->fetch()) {
            $product['id'] = $product_id;
            $product['name'] = $product_name;
            $product['price'] = $product_price;
            $product['type'] = $product_type;
            $product['city'] = $store_city;
        }
        return $product;
    }

    function productRetrieveByUserId($user_id) {
        $user_id = $this->prepareData($user_id);
        
        $response = array();

        $stmt = mysqli_stmt_init($this->connect);
        $stmt->prepare("SELECT * FROM products WHERE " . "user_id". " = '" . $user_id . "'");
        $stmt->execute();
        $stmt->bind_result($column_id,
                            $user_id,
                            $store_id,
                            $product_id,
                            $product_name,
                            $product_quantity,
                            $product_price,
                            $product_type,
                            $product_description);
                            
        while ($stmt->fetch()) {
            $product = array();
            $product['column_id'] = $column_id;
            $product['user_id'] = $user_id;
            $product['store_id'] = $store_id;
            $product['product_id'] = $product_id;
            $product['product_name'] = $product_name;
            $product['product_quantity'] = $product_quantity;
            $product['product_price'] = $product_price;
            $product['product_type'] = $product_type;
            $product['product_description'] = $product_description;
            array_push($response, $product);
        }
        return $response;
    }

    function productUpdate($user_id, $store_id, $id, $image, $name, $type, $price, $stock, $desc) {
        $user_id = $this->prepareData($user_id); 
        $store_id = $this->prepareData($store_id); 
        $id = $this->prepareData($id);  
        $name = $this->prepareData($name); 
        $type = $this->prepareData($type); 
        $price = $this->prepareData($price); 
        $stock = $this->prepareData($stock); 
        $desc = $this->prepareData($desc);
        $this->productRemove($id);
        $result = $this->productRegister($user_id, $store_id, $id, $image, $name, $type, $price, $stock, $desc);
        return $result;
    } 

    function productRegister($user_id, $store_id, $id, $image, $name, $type, $price, $stock, $desc) {
        $result['status'] = array();
        $user_id = $this->prepareData($user_id); 
        $store_id = $this->prepareData($store_id); 
        $id = $this->prepareData($id);  
        $name = $this->prepareData($name); 
        $type = $this->prepareData($type); 
        $price = $this->prepareData($price); 
        $stock = $this->prepareData($stock); 
        $desc = $this->prepareData($desc);
        $stmt = mysqli_stmt_init($this->connect);
    
        // Image handling     
        $image_upload = $this->uploadProductImage($id, $image);
        if ($image_upload != "success") {
            $result['status'] = "image/" . $image_upload;
        } else {
            $imagename = basename($image["name"]);
            try {
                $stmt->prepare("SELECT store_name, store_city FROM stores WHERE " . "store_id" . " = '" . $store_id . "'");
                $stmt->execute();
                $stmt->bind_result($store_name, $store_location);
                $stmt->fetch();

                $stmt->prepare(
                    "INSERT INTO products 
                    (user_id, 
                    store_id, 	
                    store_name,
                    store_city,
                    product_id,	
                    product_image,	
                    product_name, 	
                    product_type,	
                    product_price, 	
                    product_stock, 	
                    product_description)
                    VALUES (?,?,?,?,?,?,?,?,?,?,?)"
                    );
                $stmt->bind_param("sssssssssss", $user_id, $store_id, $store_name, $store_location, $id, $imagename, $name, $type, $price, $stock, $desc);
                $stmt->execute();
                $result['status'] = "success";
            } catch (mysqli_sql_exception $e) {
                echo $e;
            }
        }
        return $result;
    }

    function productRemove($product_id) {
        $product_id = $this->prepareData($product_id);

        $stmt = mysqli_stmt_init($this->connect);
        $stmt->prepare("SELECT product_image FROM products WHERE product_id" . " = '" . $product_id . "'");
        $stmt->execute();
        $stmt->bind_result($product_image);
        $stmt->fetch();
        $this->removeProductImage($product_image);

        $stmt->prepare("DELETE FROM products WHERE product_id = '" . $product_id . "'");
        $stmt->execute();
        return true;
    }

    function removeProductImage($product_image) {
        $target_dir = "../res/";
        $target_file = $target_dir . $product_image;
        unlink($target_file);
        return true;
    }
    
    function uploadProductImage($product_id, $image) {
        $target_dir = "../res/";
        $uploaded_file = $target_dir . basename($image["name"]);
        $imageFileType = strtolower(pathinfo($uploaded_file, PATHINFO_EXTENSION));
        $uploadOk = 1;

        $target_file = $target_dir . $product_id . "." . $imageFileType;

        // Check if image file is a actual image or fake image
        $check = getimagesize($image["tmp_name"]);
        if($check !== false) {
          $uploadOk = 1;
        } else {
          $uploadOk = 0;
            return "type";
        }

        // Check if file already exists
        if (file_exists($target_file)) {
            $uploadOk = 0;
            return "exist";
        }
        
        // Check file size
        if ($image["size"] > 2000000) {
            $uploadOk = 0;
            return "size";
        }

        // Allow certain file formats
        if($imageFileType != "jpg" 
        && $imageFileType != "png" 
        && $imageFileType != "jpeg"
        && $imageFileType != "gif" ) {
            $uploadOk = 0;
            return "format";
        }

        if ($uploadOk == 0) {
            return "upload";
            // if everything is ok, try to upload file
        } else {
            if (move_uploaded_file($image["tmp_name"], $target_file)) {
                return "success";
            } else {
                return "upload";
            }
        }
        
    }
}

?>