<?php
require_once "DataBaseConfig.php";
require_once  "DbConnect.php";

class Stores
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
    
    function storeUpdate($table, $arrayfield, $arraydata)
    {   
        $field = explode(",", $arrayfield);
        $data = explode(",", $arraydata);

        $id_key = array_search("store_id", $field);
        $id = $data[$id_key];    
        
        $this->sql = "select * from " . $table . " where store_id = '" . $id . "'";
        $result = mysqli_query($this->connect, $this->sql);


        if (mysqli_num_rows($result) > 0) {
            $length = count($field);
            
            for ($i = 0; $i<$length; $i++) {
                $field_ = $field[$i];
                $data_ = $data[$i];

                $this->sql = "UPDATE " . $table . " SET " . $field_ . " = '" . $data_ . "' WHERE store_id = '" . $id . "'";
                mysqli_query($this->connect, $this->sql);
                $result = "success";
            }
        } else {
            $result = "not exists";
        }
        return $result;
    }

    function storeRegisterOld($table, $fieldlist, $datalist)
    {   
        $field = explode(",", $fieldlist);
        $data = explode(",", $datalist);

        $id_key = array_search("id", $field);
        $id = $data[$id_key];    
        $this->sql = "select * from " . $table . " where store_id = " . $id . ";";
        $result = mysqli_query($this->connect, $this->sql);

        if (mysqli_num_rows($result) <= 0 ) {
            $this->sql = "INSERT INTO " . $table . " (" . $fieldlist . ") VALUES (" . $datalist . ")";
            if (mysqli_query($this->connect, $this->sql)) {
                $result = "success";
            } else {
                $result = "fail";
            }
        } else {
            $result = "exists";
        }
        return $result;
    }

    function storeRegister($user_id, $store_id, $store_image, $store_name, $store_email, $store_phone, $store_province,
    $store_city, $store_postcode, $store_address,  $store_description) {
        $user_id = $this->prepareData($user_id);
        $store_id = $this->prepareData($store_id);
        $store_name = $this->prepareData($store_name);
        $store_email = $this->prepareData($store_email);
        $store_phone = $this->prepareData($store_phone);
        $store_province = $this->prepareData($store_province);
        $store_city = $this->prepareData($store_city);
        $store_postcode = $this->prepareData($store_postcode);
        $store_address = $this->prepareData($store_address);
        $store_description = $this->prepareData($store_description);

        $image_upload = $this->uploadStoreImage($store_id, $store_image);
        if ($image_upload != "success") {
            $result['status'] = "image/" . $image_upload;
        } else {
            $imagename = basename($store_image["name"]);
            try {
                $stmt = mysqli_stmt_init($this->connect);
                $stmt->prepare(
                    "INSERT INTO stores
                    (user_id,
                    store_id,
                    store_image,
                    store_name,
                    store_email,
                    store_phone,
                    store_province,
                    store_city,
                    store_postcode,
                    store_address,
                    store_description)
                    VALUES (?,?,?,?,?,?,?,?,?,?,?)"
                );
                $stmt->bind_param(
                    "sssssssssss",
                    $user_id,
                    $store_id,
                    $imagename,
                    $store_name,
                    $store_email,
                    $store_phone,
                    $store_province,
                    $store_city,
                    $store_postcode,
                    $store_address,
                    $store_description
                );
                $stmt->execute();
                $result['status'] = "success";
            } catch (mysqli_sql_exception $e) {
                $result['status'] = "fail";
            }
        }
        
        return $result;
    }

    function uploadStoreImage($store_id, $image) {
        $target_dir = "../res/";
        $uploaded_file = $target_dir . basename($image["name"]);
        $imageFileType = strtolower(pathinfo($uploaded_file, PATHINFO_EXTENSION));
        $uploadOk = 1;

        $target_file = $target_dir . $store_id . "." . $imageFileType;

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
    
    function storeRetrieveByUserId($user_id) 
    {
        $response = array();
        $user_id = $this->prepareData($user_id);
        $stmt = mysqli_stmt_init($this->connect);
        $stmt->prepare("SELECT * FROM stores WHERE " . "user_id". " = '" . $user_id . "'");
        $stmt->execute();
        $stmt->bind_result(
            $dbuser_id,
            $dbstore_id,
            $dbstore_name,
            $dbstore_image,
            $dbstore_email,
            $dbstore_phone,
            $dbstore_province,
            $dbstore_city,
            $dbstore_postcode,
            $dbstore_address,
            $dbstore_description
        );
        
        while ($stmt->fetch()) {
            $response["id"] = $dbstore_id;
            $response["name"] = $dbstore_name;
            $response["image"] = $dbstore_image;
            $response["email"] = $dbstore_email;
            $response["phone"] = $dbstore_phone;
            $response["province"] = $dbstore_province;
            $response["city"] = $dbstore_city;
            $response["address"] = $dbstore_address;
            $response["description"] = $dbstore_description;
        }
        return $response;
    }

    function storeRetrieveByStoreId($store_id) 
    {
        $response = array();
        $store_id = $this->prepareData($store_id);
        $stmt = mysqli_stmt_init($this->connect);
        $stmt->prepare("SELECT * FROM stores WHERE " . "store_id". " = '" . $store_id . "'");
        $stmt->execute();
        $stmt->bind_result(
            $dbuser_id,
            $dbstore_id,
            $dbstore_name,
            $dbstore_image,
            $dbstore_email,
            $dbstore_phone,
            $dbstore_province,
            $dbstore_city,
            $dbstore_postcode,
            $dbstore_address,
            $dbstore_description
        );
        
        while ($stmt->fetch()) {
            $response["id"] = $dbstore_id;
            $response["name"] = $dbstore_name;
            $response["image"] = $dbstore_image;
            $response["email"] = $dbstore_email;
            $response["phone"] = $dbstore_phone;
            $response["province"] = $dbstore_province;
            $response["city"] = $dbstore_city;
            $response["address"] = $dbstore_address;
            $response["description"] = $dbstore_description;
        }
        return $response;
    }
}

?>
