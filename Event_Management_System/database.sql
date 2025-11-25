-- Optional MySQL table (not required for running file-backed version)
CREATE DATABASE IF NOT EXISTS ems_db;
USE ems_db;
CREATE TABLE events(
  id INT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  date VARCHAR(50),
  venue VARCHAR(100),
  type VARCHAR(20)
);