<?php
require "DataBaseConfig.php";
require_once "DbConnect.php";

class Carts
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

    function updateCartOld($user_id, $stores, $products)
    {
        // Note: $stores and $products variable is already in Json array
        $user_id = $this->prepareData($user_id);
        $stores = $this->prepareData($stores);
        $products = $this->prepareData($products);
        $stmt = mysqli_stmt_init($this->connect);
        $stmt->prepare("SELECT * FROM carts WHERE " . "user_id" . " = '" . $user_id . "'");
        if ($stmt->execute()) {
            $stmt = mysqli_stmt_init($this->connect);
            $stmt->prepare("INSERT INTO carts (`user_id`, `cart_stores`, `cart_products`) VALUES(?, ?, ?);");
            $stmt->bind_param("sss", $user_id, $stores, $products);
            try {
                $stmt->execute() == true;
                return "workie";
            } catch (mysqli_sql_exception $e) {
                $stmt = mysqli_stmt_init($this->connect);
                $stmt->prepare("UPDATE carts SET cart_stores ='" . $stores . "', cart_products = '" . $products . "' WHERE user_id ='" . $user_id . "'");
                $stmt->execute();
                return "updatie";
            }
        }
    }

    function updateCartProductQuantity($user_id, $product_id, $product_quantity)
    {
        $result = array();
        $user_id = $this->prepareData($user_id);
        $product_id = $this->prepareData($product_id);
        $product_quantity = $this->prepareData($product_quantity);
        
        $stmt = mysqli_stmt_init($this->connect);
        $stmt->prepare("UPDATE cart_products SET product_quantity = '" . $product_quantity . "'" .
        " WHERE user_id = '" . $user_id . "'" . " AND product_id = '" . $product_id . "';");
        $stmt->execute();
        $result = "updated";
        return $result;
    }

    function removeCartProduct($user_id, $product_id) {
        $result = array();
        $user_id = $this->prepareData($user_id);
        $product_id = $this->prepareData($product_id);

        $stmt = mysqli_stmt_init($this->connect);
        $stmt->prepare("DELETE FROM cart_products WHERE product_id = '" . $product_id . "' AND user_id = '" . $user_id . "'");
        $stmt->execute();
    }

    function removeCartStore($user_id, $store_id) {
        $result = array();
        $user_id = $this->prepareData($user_id);
        $product_id = $this->prepareData($store_id);

        $stmt = mysqli_stmt_init($this->connect);
        $stmt->prepare("DELETE FROM cart_stores WHERE store_id = '" . $store_id . "' AND user_id = '" . $user_id . "'");
        $stmt->execute();
    }

    function registerCartProductOld($user_id, $store_id, $product_id, $product_quantity) {
        $result = array();
        $stmt = mysqli_stmt_init($this->connect);
        $user_id = $this->prepareData($user_id);
        $store_id = $this->prepareData($store_id);
        $product_id = $this->prepareData($product_id);
        $product_quantity = $this->prepareData($product_quantity);
        $product_availability = $this->checkCartProductAvailability($product_id);

        $stmt->prepare("INSERT INTO carts (`user_id`, `store_id`, `product_id`, `product_quantity`, `product_availability`) VALUES(?, ?, ?, ?, ?)");
        $stmt->bind_param("sssss", $user_id, $store_id, $product_id, $product_quantity, $product_availability);
        try {
            $stmt->execute();
            $result['status'] = "registered";
        } catch (mysqli_sql_exception $e) {
            $this->updateCartProductQuantity($user_id, $product_id, $product_quantity);
            $result['status'] = "updated";
        }
        return $result;
    }

    function registerCartProduct($user_id, $product_id, $product_quantity)
    {
        $user_id = $this->prepareData($user_id);
        $product_id = $this->prepareData($product_id);
        $product_quantity = $this->prepareData($product_quantity);

        $result = array();
        $stmt = mysqli_stmt_init($this->connect);
        
        $stmt->prepare("SELECT store_id,product_name, product_stock, product_price FROM products WHERE product_id = '" . $product_id . "'");
        $stmt->execute();
        $stmt->store_result();
        $stmt->bind_result($store_id, $product_name, $product_stock, $product_price);
        $stmt->fetch(); // Remember to fetch the result after binding the result
        if ($stmt->num_rows() > 0) {
            if ($product_quantity <= $product_stock) {
                $available = true;
                try {
                    $stmt->prepare("INSERT INTO cart_products 
                            (`user_id`, `store_id`, `product_id`, `product_name`, `product_price`, `product_quantity`, `product_stock`, `available`) 
                            VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
                    $stmt->bind_param("ssssssss", $user_id, $store_id, $product_id, $product_name, $product_price, $product_quantity, $product_stock, $available);
                    $stmt->execute();
                    $result['status'] = "success";
                } catch (mysqli_sql_exception $e) {
                    // Update the quantity when prodct is already in cart
                    $result['status'] = $this->updateCartProductQuantity($user_id, $product_id, $product_quantity);
                }
            } else {
                $result['status'] = "stock";
            }
        } else {
            $result['status'] = "product";
        }
        return $result;
    }

    function registerCartStore($user_id, $store_id)
    {
        $user_id = $this->prepareData($user_id);
        $store_id = $this->prepareData($store_id);

        $result = array();
        $stmt = mysqli_stmt_init($this->connect);
        
        $stmt->prepare("SELECT store_name, store_city FROM stores WHERE store_id = '" . $store_id . "'");
        $stmt->bind_result($store_name, $store_city);
        $stmt->execute();
        $stmt->store_result();
        $stmt->fetch(); // Remember to fetch the result after binding the result
        if ($stmt->num_rows() > 0) {
            $available = true;
            try {
                $stmt->prepare("INSERT INTO cart_stores 
                        (`user_id`, `store_id`, `store_name`, `store_city`, `available`) 
                        VALUES(?, ?, ?, ?, ?)");
                $stmt->bind_param("sssss", $user_id, $store_id, $store_name, $store_city, $available);
                $stmt->execute();
                $result['status'] = "success";
            } catch (mysqli_sql_exception $e) {
                echo $e;
            }
        }
        return $result;
    }

    function updateCart($user_id, $stores_json, $products_json) {
        $stores = json_decode($stores_json);
        $products = json_decode($products_json);
        $result = array();
        $dbproducts = $this->getCartProducts($user_id);
        $dbstores = $this->getCartStores($user_id);
        for ($j=0; $j<count($dbproducts); $j++) {
            extract($dbproducts[$j]); // Extract all variable and values of the array
            $this->removeCartProduct($user_id, $id);
        }
        for ($j=0; $j<count($dbstores); $j++) {
            extract($dbstores[$j]); // Extract all variable and values of the array
            $this->removeCartStore($user_id, $id);
        }
        for ($i=0; $i<count($products); $i++) {
            $this->registerCartProduct($user_id, $products[$i]->id, $products[$i]->quantity);
        }
        for ($i=0; $i<count($stores); $i++) {
            $this->registerCartStore($user_id, $stores[$i]->id);
        }
        $result['status'] = "updated";
        return $result;
    }


    function updateCartProductQuantityA($user_id, $product_id, $product_quantity)
    {
        $result = array();
        $user_id = $this->prepareData($user_id);
        $product_id = $this->prepareData($product_id);
        $product_quantity = $this->prepareData($product_quantity);
        
        $stmt = mysqli_stmt_init($this->connect);

        $stmt->prepare("SELECT product_stock FROM products WHERE product_id = '" . $product_id . "';");
        $stmt->execute();
        $stmt->bind_result($dbproduct_stock);
        $stmt->fetch();

        $stmt->prepare("SELECT product_quantity FROM cart_products 
                        WHERE user_id = '" . $user_id . "'" . " AND product_id = '" . $product_id . "';");
        $stmt->execute();
        $stmt->bind_result($dbproduct_quantity);
        $stmt->fetch();

        $newproduct_quantity = $dbproduct_quantity + $product_quantity;

        if ($newproduct_quantity <= $dbproduct_stock) {
            $stmt->prepare("UPDATE cart_products SET product_quantity = '" . $newproduct_quantity . "'" .
                " WHERE user_id = '" . $user_id . "'" . " AND product_id = '" . $product_id . "';");
            $stmt->execute();
            $result = "updated";
        } else {
            $result = "stock"; // Not enough stock
        }
        return $result;
    }


    function checkCartProductAvailability($product_id) {
        $product_id = $this->prepareData($product_id);
        $stmt = mysqli_stmt_init($this->connect);
        $stmt->prepare("SELECT * FROM products WHERE product_id = '" . $product_id . "'");
        if ($stmt->num_rows() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /*
    if (!empty($stmt->error)) {
    // There is error - the entry is not found
    $stmt = mysqli_stmt_init($this->connect);
    $stmt->prepare("INSERT INTO carts (`user_id`, `cart_stores`, `cart_products`) VALUES(?, ?, ?);");
    $stmt->bind_param("sss", $user_id, $stores, $products);
    } else {
    $stmt = mysqli_stmt_init($this->connect);
    $stmt->prepare("UPDATE carts SET cart_stores ='" . $stores . "', cart_products = '" . $products . "' WHERE user_id ='" . $user_id . "'");
    $stmt->execute();
    }
    return $this->retrieveCart($user_id);
    */

    function retrieveCartA($user_id)
    {
        $response = array();
        $user_id = $this->prepareData($user_id);
        $stmt = mysqli_stmt_init($this->connect);
        $stmt->prepare("SELECT * FROM carts WHERE " . "user_id" . " = '" . $user_id . "'");
        if ($stmt->execute()) {
            $stmt->bind_result($user_id, $stores, $products);
            while ($stmt->fetch()) {
                $cart['user_id'] = $user_id;
                $cart['stores'] = $stores;
                $cart['products'] = $products;
            }
            return $cart;
        }
        return "failed";
    }


    function retrieveCartOld($user_id)
    {
        $response = array();
        
        $user_id = $this->prepareData($user_id);
        $stmt = mysqli_stmt_init($this->connect);
        $stmt->prepare("SELECT * FROM carts WHERE " . "user_id" . " = '" . $user_id . "'");
        if ($stmt->execute()) {
            $stmt->bind_result($user_id, $stores, $product_id, $product_quantity, $product_availability);
            $cart['store'] = array();
            $cart['product'] = array();
            
            while ($stmt->fetch()) {
                $product['id'] = $product_id;
                $product['quantity'] = $product_quantity;
                $product['availability'] = $product_availability;
                array_push($cart['store'], $stores);
                array_push($cart['product'], $product);
            }
            $cart['id'] = $user_id;
            $response['store'] = $cart['store'];
            $response['product'] = $cart['product'];
        }
        return $response;
    }

    function retrieveCart($user_id)
    {
        $this->updateCartDb();     
        $user_id = $this->prepareData($user_id);  
        
        $response['store'] = $this->getCartStores($user_id);
        $response['product'] = $this->getCartProducts($user_id);

        return $response;
    }

    function getCartStores($user_id) {
        $cart['store'] = array();
        $stmt = mysqli_stmt_init($this->connect);
        $stmt->prepare("SELECT * FROM cart_stores WHERE " . "user_id" . " = '" . $user_id . "'");
        $stmt->execute();
        $stmt->bind_result($user_id, $store_id, $store_name, $store_city, $available);
        while ($stmt->fetch()) {
            $store['id'] = $store_id;
            $store['name'] = $store_name;
            $store['location'] = $store_city;
            if ($available) {
                $store['available'] = "true";
            } else {
                $store['available'] = "false";
            }
            array_push($cart['store'], $store);
        }
        return $cart['store'];
    }

    function getCartProducts($user_id) {
        $cart['product'] = array();
        $stmt = mysqli_stmt_init($this->connect);
        $stmt->prepare("SELECT * FROM cart_products WHERE " . "user_id" . " = '" . $user_id . "'");
        $stmt->execute();
        $stmt->bind_result($id, $user_id, $store_id, $product_id, $product_name, $product_price, $product_quantity, $product_stock, $available);
        while ($stmt->fetch()) {
            $product['store_id'] = $store_id;
            $product['id'] = $product_id;
            $product['name'] = $product_name;
            $product['price'] = $product_price;
            $product['quantity'] = $product_quantity;
            $product['stock'] = $product_stock;
            if ($available) {
                $product['available'] = "true";
            } else {
                $product['available'] = "false";
            }
            array_push($cart['product'], $product);
        }
        return $cart['product'];
    }

    function updateCartDb() {
        $stmt = mysqli_stmt_init($this->connect);

        // Check if product still exists in the products table
        $products = array();
        $stmt->prepare("SELECT product_id FROM cart_products");
        $stmt->bind_result($product_id);
        $stmt->execute();
        while ($stmt->fetch()) {
             array_push($products, $product_id);
        }
        for ($i = 0; $i < count($products); $i++) {
            try {
                $product_id = $products[$i];
                $stmt->prepare("SELECT product_name, product_stock, product_price FROM products WHERE " . "product_id" . " = '" . $product_id . "'");
                $stmt->execute();
                $stmt->bind_result(
                    $product_name,
                    $product_stock,
                    $product_price
                );
                $stmt->store_result();
                $stmt->fetch();
                if ($stmt->num_rows() < 1) {
                    $available = false;
                    $stmt->prepare("UPDATE cart_products SET available = '" . $available . "' WHERE product_id = '" . $product_id . "'");
                    $stmt->execute();
                } else {
                    $available = true;
                    $stmt->prepare("UPDATE cart_products 
                SET product_name = '" . $product_name . "', 
                    product_stock = '" . $product_stock . "', 
                    product_price = '" . $product_price . "', 
                    available = '" . $available . "' 
                WHERE product_id = '" . $product_id . "'");
                    $stmt->execute();
                }
            } catch (mysqli_sql_exception $e) {
                echo $e;
            }

        }

        // Check if product still exists in the stores table
        $stores = array();
        $stmt->prepare("SELECT store_id FROM cart_stores");
        $stmt->execute();
        $stmt->bind_result($store_id);
        while ($stmt->fetch()) {
             array_push($stores, $store_id);
        }
        for ($i = 0; $i < count($stores); $i++) {
            $store_id = $stores[$i];
            $stmt->prepare("SELECT store_name, store_city FROM stores WHERE store_id = '" . $store_id . "'");
            $stmt->bind_result($store_name, $store_city);
            $stmt->execute();
            $stmt->store_result();
            $stmt->fetch();
            if ($stmt->num_rows() < 1) {
                $available = false;
                $stmt->prepare("UPDATE cart_stores SET available = '" . $available . "' WHERE store_id = '" . $store_id . "'");
                $stmt->execute();
            } else {
                $available = true;
                $stmt->prepare("UPDATE cart_stores 
                SET store_name = '" . $store_name . "', 
                    store_city = '" . $store_city . "',  
                    available = '" . $available . "'  
                WHERE store_id = '" . $store_id . "'");
                $stmt->execute();
            }
            
        }
    }


/*
if (!empty($stmt->error)) {
return "not found";
} else {
$stmt->bind_result($user_id, $stores, $products);
$cart = array();
while ($stmt->fetch()) { 
$cart['user_id'] = $user_id;
$cart['stores'] = $stores;
$cart['products'] = $products;
}
return $cart;
}
*/
}


?>