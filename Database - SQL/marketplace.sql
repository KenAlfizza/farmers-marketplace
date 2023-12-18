-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 01, 2023 at 02:11 PM
-- Server version: 10.4.24-MariaDB
-- PHP Version: 8.1.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `marketplace`
--

-- --------------------------------------------------------

--
-- Table structure for table `cart_products`
--

CREATE TABLE `cart_products` (
  `id` int(11) NOT NULL,
  `user_id` varchar(16) NOT NULL,
  `store_id` varchar(16) NOT NULL,
  `product_id` varchar(16) NOT NULL,
  `product_name` text NOT NULL,
  `product_price` int(11) NOT NULL,
  `product_quantity` int(11) NOT NULL,
  `product_stock` int(11) NOT NULL,
  `available` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `cart_products`
--

INSERT INTO `cart_products` (`id`, `user_id`, `store_id`, `product_id`, `product_name`, `product_price`, `product_quantity`, `product_stock`, `available`) VALUES
(179, 'us78318423', 'st40356133', 'pr037844166350', 'Broccoli 500gr', 18000, 3, 100, 1),
(180, 'us78318423', 'st40356133', 'pr565820141805', 'Rice 1kg', 12500, 1, 100, 1);

-- --------------------------------------------------------

--
-- Table structure for table `cart_stores`
--

CREATE TABLE `cart_stores` (
  `user_id` varchar(16) NOT NULL,
  `store_id` varchar(16) NOT NULL,
  `store_name` text NOT NULL,
  `store_city` text NOT NULL,
  `available` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `cart_stores`
--

INSERT INTO `cart_stores` (`user_id`, `store_id`, `store_name`, `store_city`, `available`) VALUES
('us78318423', 'st40356133', 'Store J', 'Jakarta', 1);

-- --------------------------------------------------------

--
-- Table structure for table `communities`
--

CREATE TABLE `communities` (
  `user_id` varchar(16) NOT NULL,
  `community_id` varchar(16) NOT NULL,
  `community_password` text NOT NULL,
  `community_name` text NOT NULL,
  `community_province` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `community_members`
--

CREATE TABLE `community_members` (
  `community_id` text NOT NULL,
  `user_id` text NOT NULL,
  `user_role` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `order_id` varchar(15) NOT NULL,
  `user_id` varchar(15) NOT NULL,
  `product_id` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL CHECK (json_valid(`product_id`)),
  `product_json` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL CHECK (json_valid(`product_json`))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `orders_status`
--

CREATE TABLE `orders_status` (
  `order_id` varchar(16) NOT NULL,
  `order_status` int(11) NOT NULL COMMENT '0 = complete,\r\n1 = pending'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `column_id` int(11) NOT NULL,
  `user_id` varchar(16) NOT NULL,
  `store_id` varchar(16) NOT NULL,
  `store_name` text NOT NULL,
  `store_city` text NOT NULL,
  `product_id` varchar(16) NOT NULL,
  `product_image` text NOT NULL,
  `product_name` text NOT NULL,
  `product_stock` int(11) NOT NULL,
  `product_price` int(11) NOT NULL,
  `product_type` text NOT NULL,
  `product_description` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`column_id`, `user_id`, `store_id`, `store_name`, `store_city`, `product_id`, `product_image`, `product_name`, `product_stock`, `product_price`, `product_type`, `product_description`) VALUES
(1, 'us75267106', 'st40356133', 'Store J', 'Jakarta', 'pr037844166350', 'pr037844166350.png', 'Broccoli 500gr', 100, 18000, 'vegetable', 'Broccoli'),
(8, 'us75267106', 'st40356133', 'Store J', 'Jakarta', 'pr318818521002', 'pr318818521002.png', 'Rice 100gr', 100, 2500, 'seed', 'No description'),
(15, 'us75267106', 'st40356133', 'Store J', 'Jakarta', 'pr511674322583', 'pr511674322583.png', 'Broccoli 1KG', 100, 250000, 'vegetable', 'No description'),
(2, 'us75267106', 'st42261665', 'Store K', 'Jakarta', 'pr560456600528', 'pr560456600528.png', 'Broccoli 500gr', 100, 18000, 'vegetable', 'Broccoli 500gr\\nCheapest price. Order now.'),
(4, 'us75267106', 'st40356133', 'Store J', 'Jakarta', 'pr565820141805', 'pr565820141805.png', 'Rice 1kg', 100, 12500, 'seed', 'Rice description'),
(18, 'us78318423', 'st37130050', 'Store A', 'Jakarta', 'pr601673528118', 'pr601673528118.png', 'Tomato 10KG', 100, 75000, 'fruit', 'Tomato ready to order. Highest quality.'),
(5, 'us75267106', 'st40356133', 'Store J', 'Jakarta', 'pr632627470526', 'pr632627470526.png', 'Rice 2kg', 10, 22500, 'seed', 'Rice 2kg');

-- --------------------------------------------------------

--
-- Table structure for table `stores`
--

CREATE TABLE `stores` (
  `user_id` varchar(16) NOT NULL,
  `store_id` varchar(16) NOT NULL,
  `store_name` text NOT NULL,
  `store_image` text NOT NULL,
  `store_email` varchar(300) NOT NULL,
  `store_phone` varchar(16) NOT NULL,
  `store_province` text NOT NULL,
  `store_city` text NOT NULL,
  `store_postcode` int(11) NOT NULL,
  `store_address` text NOT NULL,
  `store_description` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `stores`
--

INSERT INTO `stores` (`user_id`, `store_id`, `store_name`, `store_image`, `store_email`, `store_phone`, `store_province`, `store_city`, `store_postcode`, `store_address`, `store_description`) VALUES
('us78318423', 'st37130050', 'Store A', 'st37130050.png', 'storea@mail.com', '+9258329527', 'Jakarta', 'Jakarta', 124267, '22, Dermawan Street', 'Find the best product with the cheapest price here.'),
('us75267106', 'st40356133', 'Store J', 'st40356133.png', 'Store J', '08123914', 'Jakarta', 'Jakarta', 12790, 'Mampang X Street', 'No description');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` varchar(10) NOT NULL,
  `name` text NOT NULL,
  `email` varchar(300) NOT NULL,
  `phone` varchar(15) NOT NULL,
  `password` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `email`, `phone`, `password`) VALUES
('ad04106437', 'admin', 'admin@example.com', '0878657432', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3'),
('ed84313643', 'adam', 'edy@mail.com', '123', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3'),
('us50317477', 'Ken', 'ken@mail.com', '09517047', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3'),
('us56077673', 'Adam Smith', 'adamsmith@mail.com', '+624637281631', '9a900403ac313ba27a1bc81f0932652b8020dac92c234d98fa0b06bf0040ecfd'),
('us64883123', 'Josh Williams', 'joshwilliams69@mail.com', '+92647383365729', '9a900403ac313ba27a1bc81f0932652b8020dac92c234d98fa0b06bf0040ecfd'),
('us75267106', 'Ken', 'adam@mail.com', '0896812', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3'),
('us78318423', 'Kenny Williams', 'kennywilliams69@mail.com', '+92513901247', 'ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `cart_products`
--
ALTER TABLE `cart_products`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `product_id` (`product_id`);

--
-- Indexes for table `cart_stores`
--
ALTER TABLE `cart_stores`
  ADD UNIQUE KEY `store_id` (`store_id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`order_id`);

--
-- Indexes for table `orders_status`
--
ALTER TABLE `orders_status`
  ADD PRIMARY KEY (`order_id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD UNIQUE KEY `product_id` (`product_id`),
  ADD UNIQUE KEY `product_id_2` (`product_id`),
  ADD KEY `product_index` (`column_id`);

--
-- Indexes for table `stores`
--
ALTER TABLE `stores`
  ADD PRIMARY KEY (`store_id`),
  ADD KEY `store_id` (`store_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `phone` (`phone`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `cart_products`
--
ALTER TABLE `cart_products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=181;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `column_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
