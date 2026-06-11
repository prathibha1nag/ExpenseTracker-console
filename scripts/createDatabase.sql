
-- Drop database ExpenseTracker_dev
CREATE DATABASE ExpenseTracker_dev;

USE ExpenseTracker_dev;

CREATE TABLE user (
  userid INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  phone VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE expense (
  id INT PRIMARY KEY AUTO_INCREMENT,
  userid INT NOT NULL,
  date DATE NOT NULL,
  amount DECIMAL(18,2) NOT NULL,
  expensecategory INT NOT NULL,
  description nvarchar(500) NOT NULL,
  FOREIGN KEY (userid) REFERENCES user(userid),
  INDEX idx_expenses_userid (userid),
  INDEX idx_expenses_date_userid_category (userid, date,expensecategory)
);

insert into user (username, password, email, phone)
 values ("Prathibha", "1234", "anagaprathibha@gmail.com", "9392451799")