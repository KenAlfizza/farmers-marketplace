<?php
require "DataBaseConfig.php";
require_once "DbConnect.php";

class DataBase
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

    function getProductLists($table, $store)
    {
        $store = $this->prepareData($store);
        $this->sql = "select * from " . $table . " where store = '" . $store . "'";
        $result = mysqli_query($this->connect, $this->sql);
        $row = mysqli_fetch_assoc($result);
    }

    
    function productRetrieve($table, $id)
    {     
        
        $id = $this->prepareData($id);
        $this->sql = "select * from " . $table . " where id = '" . $id . "'";
        $result = mysqli_query($this->connect, $this->sql);
        $row = mysqli_fetch_assoc($result);
        if (mysqli_num_rows($result) != 0) {
            $dbid = $row['id'];
            $dbname = $row['name'];
            $dbquantity = $row['quantity'];
            $dbprice = $row['price'];
            $dbstore = $row['store'];
            if ($dbid == $id) {
                $result = $dbid . ";" . $dbname . ";" . $dbquantity . ";" . $dbprice . ";" . $dbstore;
            } else $result = "No Product Found";
        } else $result = "No Data Found";
        return $result;
    }

    function productAdd($table, $id, $name, $quantity, $price, $store) 
    {
        $id = $this->prepareData($id);
        $name = $this->prepareData($name);
        $quantity = $this->prepareData($quantity);
        $price = $this->prepareData($price);
        $store = $this->prepareData($store);
        $this->sql =
            "INSERT INTO " . $table . " (id, name, quantity, price, store) VALUES ('" . $id . "','" . $name . "','" . $quantity . "','" . $price . "','" . $store . "')";
        if (mysqli_query($this->connect, $this->sql)) {
            return true;
        } else return false;
    }

    function userRegister($table, $id, $name, $email, $phone, $password) 
    {
        $id = $this->prepareData($id);
        $name = $this->prepareData($name);
        $email = $this->prepareData($email);
        $phone = $this->prepareData($phone);
        $password = $this->prepareData($password);
        $this->sql = "select * from " . $table . " where email = '" . $email . "'";
		$result = mysqli_query($this->connect, $this->sql);
        
        if (mysqli_num_rows($result) > 0) {  
            return "Email already exists";
        } else {
            $this->sql = 
            "INSERT INTO " . $table . " (id, name, email, phone, password) VALUES ('" . $id . "','" . $name . "','" . $email . "','" . $phone . "','" . $password . "')";
            if (mysqli_query($this->connect, $this->sql)) {
                return "Register success";
            } else return "Register failed";
        }		
    }

    function userRetrieve($table, $id)
    {
        $id = $this->prepareData($id);
        $table = $this->prepareData($table);
        $this->sql = "select * from " . $table . " where id = '" . $id . "'";
        $result = mysqli_query($this->connect, $this->sql);
        $row = mysqli_fetch_assoc($result);

        if (mysqli_num_rows($result) > 0) {
            $dbid = $row["id"];
            $dbname = $row["name"];
            $dbemail = $row["email"];
            $dbphone = $row["phone"];
            
            $result = $dbid . ";" . $dbname . ";" . $dbemail . ";" . $dbphone;
        } else {
            $result = "user not found";
        }
        return $result;
    }

    function userLogin($table, $email, $password) 
    {
        $email = $this->prepareData($email);
        $password = $this->prepareData($password);
        $this->sql = "select * from " . $table . " where email = '" . $email . "'";
        $result = mysqli_query($this->connect, $this->sql);
        $row = mysqli_fetch_assoc($result);

        if (mysqli_num_rows($result) > 0) {
            $dbid = $row["id"];
            $dbpassword = $row["password"];
			if ($dbpassword == $password) {
                $result = $this->userRetrieve("user", $dbid);
            } else $result = "Invalid Password";
        } else $result = "Invalid Email";
        return $result;
    }


    function userUpdate($table, $arrayfield, $arraydata)
    {   
        $field = explode(",", $arrayfield);
        $data = explode(",", $arraydata);

        $id_key = array_search("id", $field);
        $id = $data[$id_key];    
        
        $this->sql = "select * from " . $table . " where id = '" . $id . "'";
        $result = mysqli_query($this->connect, $this->sql);


        if (mysqli_num_rows($result) > 0) {
            $length = count($field);
            
            for ($i = 0; $i<$length; $i++) {
                $field_ = $field[$i];
                $data_ = $data[$i];

                $this->sql = "UPDATE " . $table . " SET " . $field_ . " = '" . $data_ . "' WHERE id = '" . $id . "'";
                $result = mysqli_query($this->connect, $this->sql);
            }
        } else {
            $result = "No account found";
        }
        return $result;
    }

    function storeUpdate($table, $arrayfield, $arraydata)
    {   
        # TODO: Add binding param or add prepareData function to prevent SQL Injections

        $field = explode(",", $arrayfield);
        $data = explode(",", $arraydata);

        $id_key = array_search("id", $field);
        $id = $data[$id_key];    
        
        $this->sql = "select * from " . $table . " where id = '" . $id . "'";
        $result = mysqli_query($this->connect, $this->sql);


        if (mysqli_num_rows($result) > 0) {
            $length = count($field);
            
            for ($i = 0; $i<$length; $i++) {
                $field_ = $field[$i];
                $data_ = $data[$i];

                $this->sql = "UPDATE " . $table . " SET " . $field_ . " = '" . $data_ . "' WHERE id = '" . $id . "'";
                mysqli_query($this->connect, $this->sql);
                $result = "success";
            }
        } else {
            $result = "not exists";
        }
        return $result;
    }


    function entryInsert($table, $fieldlist, $datalist)
    {   
        $field = explode(",", $fieldlist);
        $data = explode(",", $datalist);

        $id_key = array_search("id", $field);
        $id = $data[$id_key];    
        $this->sql = "select * from " . $table . " where id = " . $id . ";";
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

    function storeRegisterA($table, $id, $name, $phone, $location, $description) 
    {
        $id = $this->prepareData($id);
        $name = $this->prepareData($name);
        $phone = $this->prepareData($phone);
        $location = $this->prepareData($location);
        $description = $this->prepareData($description);
        $this->sql = 
            "INSERT INTO " . $table . " (id, name, phone, location, description) VALUES ('" . $id . "','" . $name . "','" . $phone . "','" . $location . "','" . $description . "')";
        if (mysqli_query($this->connect, $this->sql)) {
            return true;
        } else return false;
    }
    
    
    function storeRetrieve($id) 
    {
        $id = $this->prepareData($id);
        $this->sql = "SELECT * FROM  store  WHERE id = '" . $id . "'";
        $result = mysqli_query($this->connect, $this->sql);
        $row = mysqli_fetch_assoc($result);
        if (mysqli_num_rows($result) != 0) {
            $dbid = $row['id'];
            $dbname = $row['name'];
            $dbemail =  $row['email'];
            $dbphone = $row['phone'];
            $dbprovince = $row['province'];
            $dbcity = $row['city'];
            $dbpostcode = $row['postcode'];
            $dbaddress = $row['address'];
            $dbdescription = $row['description'];

            if ($dbid == $id) {
                $result = $dbid . ";" 
                . $dbname . ";" 
                . $dbemail . ";" 
                . $dbphone . ";" 
                . $dbprovince . ";" 
                . $dbcity . ";" 
                . $dbpostcode . ";" 
                . $dbaddress . ";" 
                . $dbdescription;

            }
        } else $result = "not_found";    
        return $result;
    }

    
    function imageUpload() {
        $response = array();
        $target_dir = "images/";
        $target_file = $target_dir . $_POST['id'] . '.' . pathinfo($_FILES['image']['name'], PATHINFO_EXTENSION);
                    
        if (move_uploaded_file($_FILES['image']['tmp_name'], $target_file)) {

            $stmt = mysqli_stmt_init($this->connect);
            $stmt->prepare("INSERT INTO images (`id`, `path`, `description`) VALUES(?, ?, ?);");
            $stmt->bind_param("sss", $_POST['id'], $target_file, $_POST['desc']);

            if ($stmt->execute()){
                $response['error'] = false;
                $response['message'] = "Image uploaded successfully";
                $response['image'] = $this->imageGetBaseURL() . $target_file;

            } else {
                $response['error'] = true;
                $response['message'] = "Try again later...";
            }
            
        } else {
            $response['error'] = true;
            $response['message'] = "Try again later...";
        }
        return $response;
        
    }

    function imageRetrieve() {
        $response = array();
        
        $stmt = mysqli_stmt_init($this->connect);
        $stmt->prepare("SELECT * FROM " . $_POST['table'] . " WHERE id = '" . $_POST['id'] . "'");
        $stmt->execute();
        $stmt->bind_result($id, $path, $desc);

        while($stmt->fetch()) {
            $image = array();
            $image['id'] = $id;
            $image['path'] = $this->imageGetBaseURL() . $path;
            $image['desc'] = $desc;
            array_push($response, $image);
        }
        return $response;

    }

    function imageGetBaseURL() {
        $url = isset($_SERVER['HTTPS']) ? 'https://' : 'http://';
        $url .= $_SERVER['SERVER_NAME'];
        $url .= $_SERVER['REQUEST_URI'];
        return dirname($url) . '/';
    }
}

?>
