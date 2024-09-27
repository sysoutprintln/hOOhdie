-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 17, 2023 at 10:21 AM
-- Server version: 10.4.19-MariaDB
-- PHP Version: 8.0.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ho-ohdie`
--

-- --------------------------------------------------------

--
-- Table structure for table `cart`
--

CREATE TABLE `cart` (
  `UserID` char(5) NOT NULL,
  `HoodieID` char(5) NOT NULL,
  `Quantity` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `cart`
--

INSERT INTO `cart` (`UserID`, `HoodieID`, `Quantity`) VALUES
('US003', 'HO001', 1),
('US003', 'HO002', 2),
('US003', 'HO003', 3),
('US003', 'HO004', 2),
('US004', 'HO005', 1),
('US004', 'HO006', 4),
('US004', 'HO007', 3),
('US004', 'HO008', 2),
('US005', 'HO009', 1),
('US005', 'HO010', 3),
('US006', 'HO001', 2),
('US006', 'HO002', 1),
('US006', 'HO003', 3),
('US006', 'HO004', 2),
('US007', 'HO005', 1),
('US007', 'HO006', 4),
('US007', 'HO007', 3),
('US007', 'HO008', 2),
('US008', 'HO009', 1),
('US008', 'HO010', 3);

-- --------------------------------------------------------

--
-- Table structure for table `hoodie`
--

CREATE TABLE `hoodie` (
  `HoodieID` char(5) NOT NULL,
  `HoodieName` varchar(255) DEFAULT NULL,
  `HoodiePrice` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `hoodie`
--

INSERT INTO `hoodie` (`HoodieID`, `HoodieName`, `HoodiePrice`) VALUES
('HO001', 'Black Hoodie', '29.99'),
('HO002', 'Blue Hoodie', '39.99'),
('HO003', 'Red Hoodie', '49.99'),
('HO004', 'Green Hoodie', '34.99'),
('HO005', 'Yellow Hoodie', '44.99'),
('HO006', 'Purple Hoodie', '54.99'),
('HO007', 'Orange Hoodie', '37.99'),
('HO008', 'Pink Hoodie', '47.99'),
('HO009', 'Gray Hoodie', '32.99'),
('HO010', 'White Hoodie', '42.99');

-- --------------------------------------------------------

--
-- Table structure for table `transactiondetail`
--

CREATE TABLE `transactiondetail` (
  `TransactionID` char(5) NOT NULL,
  `HoodieID` char(5) NOT NULL,
  `Quantity` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `transactiondetail`
--

INSERT INTO `transactiondetail` (`TransactionID`, `HoodieID`, `Quantity`) VALUES
('TR001', 'HO001', 2),
('TR001', 'HO002', 1),
('TR001', 'HO003', 3),
('TR001', 'HO004', 2),
('TR002', 'HO005', 1),
('TR002', 'HO006', 4),
('TR002', 'HO007', 3),
('TR002', 'HO008', 2),
('TR003', 'HO009', 1),
('TR003', 'HO010', 3),
('TR004', 'HO001', 2),
('TR004', 'HO002', 1),
('TR004', 'HO003', 3),
('TR004', 'HO004', 2),
('TR005', 'HO005', 1),
('TR005', 'HO006', 4),
('TR005', 'HO007', 3),
('TR005', 'HO008', 2),
('TR006', 'HO009', 1),
('TR006', 'HO010', 3);

-- --------------------------------------------------------

--
-- Table structure for table `transactionheader`
--

CREATE TABLE `transactionheader` (
  `TransactionID` char(5) NOT NULL,
  `UserID` char(5) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `transactionheader`
--

INSERT INTO `transactionheader` (`TransactionID`, `UserID`) VALUES
('TR001', 'US003'),
('TR002', 'US004'),
('TR003', 'US005'),
('TR004', 'US006'),
('TR005', 'US007'),
('TR006', 'US008'),
('TR007', 'US009'),
('TR008', 'US010'),
('TR009', 'US011'),
('TR010', 'US012');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `UserID` char(5) NOT NULL,
  `Email` varchar(50) NOT NULL,
  `Username` varchar(50) NOT NULL,
  `Password` varchar(50) NOT NULL,
  `PhoneNumber` char(14) NOT NULL,
  `Address` varchar(100) NOT NULL,
  `Gender` varchar(25) NOT NULL,
  `Role` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`UserID`, `Email`, `Username`, `Password`, `PhoneNumber`, `Address`, `Gender`, `Role`) VALUES
('US001', 'admin@hoohdie.com', 'admin', 'admin', '+6212345678901', 'Jl. Jalan', 'Male', 'Admin'),
('US002', 'dummy@hoohdie.com', 'dummy', 'dummy', '+6212345678901', 'Jl. Dummy', 'Male', 'User'),
('US003', 'user003@hoohdie.com', 'user003', 'password003', '+6212345678901', 'Address 003', 'Male', 'User'),
('US004', 'user004@hoohdie.com', 'user004', 'password004', '+6212345678902', 'Address 004', 'Female', 'User'),
('US005', 'user005@hoohdie.com', 'user005', 'password005', '+6212345678903', 'Address 005', 'Male', 'User'),
('US006', 'user006@hoohdie.com', 'user006', 'password006', '+6212345678904', 'Address 006', 'Female', 'User'),
('US007', 'user007@hoohdie.com', 'user007', 'password007', '+6212345678905', 'Address 007', 'Male', 'User'),
('US008', 'user008@hoohdie.com', 'user008', 'password008', '+6212345678906', 'Address 008', 'Female', 'User'),
('US009', 'user009@hoohdie.com', 'user009', 'password009', '+6212345678907', 'Address 009', 'Male', 'User'),
('US010', 'user010@hoohdie.com', 'user010', 'password010', '+6212345678908', 'Address 010', 'Female', 'User'),
('US011', 'user011@hoohdie.com', 'user011', 'password011', '+6212345678909', 'Address 011', 'Male', 'User'),
('US012', 'user012@hoohdie.com', 'user012', 'password012', '+6212345678910', 'Address 012', 'Female', 'User');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `cart`
--
ALTER TABLE `cart`
  ADD PRIMARY KEY (`UserID`,`HoodieID`),
  ADD KEY `fkCart_HoodieIDtoHoodie` (`HoodieID`);

--
-- Indexes for table `hoodie`
--
ALTER TABLE `hoodie`
  ADD PRIMARY KEY (`HoodieID`);

--
-- Indexes for table `transactiondetail`
--
ALTER TABLE `transactiondetail`
  ADD PRIMARY KEY (`TransactionID`,`HoodieID`),
  ADD KEY `fk_HoodieIDtoHoodie` (`HoodieID`);

--
-- Indexes for table `transactionheader`
--
ALTER TABLE `transactionheader`
  ADD PRIMARY KEY (`TransactionID`),
  ADD KEY `fk_UserIDtoUser` (`UserID`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`UserID`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `cart`
--
ALTER TABLE `cart`
  ADD CONSTRAINT `fkCart_HoodieIDtoHoodie` FOREIGN KEY (`HoodieID`) REFERENCES `hoodie` (`HoodieID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fkCart_UserIDtoUser` FOREIGN KEY (`UserID`) REFERENCES `user` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `transactiondetail`
--
ALTER TABLE `transactiondetail`
  ADD CONSTRAINT `fk_HoodieIDtoHoodie` FOREIGN KEY (`HoodieID`) REFERENCES `hoodie` (`HoodieID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_TransactionIDtoHeader` FOREIGN KEY (`TransactionID`) REFERENCES `transactionheader` (`TransactionID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `transactionheader`
--
ALTER TABLE `transactionheader`
  ADD CONSTRAINT `fk_UserIDtoUser` FOREIGN KEY (`UserID`) REFERENCES `user` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
