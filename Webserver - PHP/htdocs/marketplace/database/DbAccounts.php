<?php
require_once "DataBaseConfig.php";
require_once "DbConnect.php";

class Accounts
{
    public $connect;
    public $data;
    private $sql;
    protected $servername;
    protected $username;
    protected $password;
    protected $databasename;

    public function __construct() {
        $this->connect = null;
        $this->data = null;
        $this->sql = null;
        $dbc = new DataBaseConfig();
        $this->servername = $dbc->servername;
        $this->username = $dbc->username;
        $this->password = $dbc->password;
        $this->databasename = $dbc->databasename;       
    }

    function dbConnect() {
        $this->connect = mysqli_connect($this->servername, $this->username, $this->password, $this->databasename);
        return $this->connect;
    }

    function prepareData($data) {
        return mysqli_real_escape_string($this->connect, stripslashes(htmlspecialchars($data)));
    }

    function userRegister($id, $name, $email, $phone, $password) {
        $respose = array();
        $respose["status"] = "failure";

        $id = $this->prepareData($id);
        $name = $this->prepareData($name);
        $email = $this->prepareData($email);
        $phone = $this->prepareData($phone);
        $password = $this->prepareData($password);
        $this->sql = "select * from users where email = '" . $email . "'";
		$result = mysqli_query($this->connect, $this->sql);
        
        if (mysqli_num_rows($result) > 0) {
        } else {
            $this->sql = 
            "INSERT INTO users (id, name, email, phone, password) VALUES ('" . $id . "','" . $name . "','" . $email . "','" . $phone . "','" . $password . "')";
            if (mysqli_query($this->connect, $this->sql)) {
                $respose["status"] = "success";
            }
        }
        return $respose;
    }

    function userLoginJson($email, $password) {
        $email = $this->prepareData($email);

        $respose = array();
        $respose["status"] = "Email Not Found";

        $stmt = mysqli_stmt_init($this->connect);
        $stmt->prepare("SELECT * FROM users WHERE " . "email". " = '" . $email . "'");
        $stmt->execute();
        $stmt->bind_result($dbid, $dbname, $dbemail, $dbphone, $dbpassword);
        while ($stmt->fetch()) {
            if ($dbpassword == $password) {
                $respose["status"] = "Authorized";
                $respose["id"] = $dbid;
                $respose["name"] = $dbname;
                $respose["email"] = $dbemail;
                $respose["phone"] = $dbphone;
                
            } else {
                $respose["status"] = "Invalid Password";
            }
        }
        return $respose;
    }

    function userLogin($table, $email, $password) {
        $email = $this->prepareData($email);
        $password = $this->prepareData($password);
        $this->sql = "select * from " . $table . " where email = '" . $email . "'";
        $result = mysqli_query($this->connect, $this->sql);
        $row = mysqli_fetch_assoc($result);

        if (mysqli_num_rows($result) > 0) {
            $dbid = $row["id"];
            $dbpassword = $row["password"];
			if ($dbpassword == $password) {
                $result = $this->userRetrieve("users", $dbid);
            } else $result = "error_password";
        } else $result = "error_email";
        return $result;
    }

    function userAuth($user_id, $user_password) {
        $auth = false;
        $user_id = $this->prepareData($user_id);
        $user_password = $this->prepareData($user_password);
        $this->sql = "SELECT * FROM users WHERE id = '" . $user_id . "'";
        $result = mysqli_query($this->connect, $this->sql);
        $row = mysqli_fetch_assoc($result);

        if (mysqli_num_rows($result) > 0) {
            $dbpassword = $row["password"];
			if ($dbpassword == $user_password) {
                $auth = true;
            }
        }
        return $auth;
    }

    function userRetrieve($table, $id) {
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
            $result = "error_missing";
        }
        return $result;
    }

    function userUpdate($table, $arrayfield, $arraydata) {   
        $result = array();
        $field = explode(",", $arrayfield);
        $data = explode(",", $arraydata);

        $id_key = array_search("id", $field);
        $id = $data[$id_key];    
        
        $this->sql = "select * from " . $table . " where id = '" . $id . "'";
        $stmt = mysqli_query($this->connect, $this->sql);


        if (mysqli_num_rows($stmt) > 0) {
            $length = count($field);
            
            for ($i = 0; $i<$length; $i++) {
                $field_ = $field[$i];
                $data_ = $data[$i];

                $this->sql = "UPDATE users SET " . $field_ . " = '" . $data_ . "' WHERE id = '" . $id . "'";
                mysqli_query($this->connect, $this->sql);
                $result['status'] = "success";
            }
        } else {
            $result['status'] = "error_missing";
        }
        return $result;
    }
}
?>
