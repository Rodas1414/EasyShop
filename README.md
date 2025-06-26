# 🛒 Easy Shop

Easy Shop is a Java Spring Boot-based e-commerce platform that allows customers to browse products, manage a shopping cart, and place orders. Admins can manage product listings and view order data. This project is built to demonstrate core backend development principles using Spring Boot, JWT authentication, and a MySQL database.

---

## 🚀 Features

- User registration and login (JWT authentication)
- Role-based access (User & Admin)
- Product listing and management
- Add-to-cart and cart item retrieval
- Order placement and tracking
- MySQL database integration

---

## 🧱 Tech Stack

- **Backend:** Java 17, Spring Boot
- **Database:** MySQL
- **Security:** Spring Security, JWT
- **Build Tool:** Maven
- **Other Tools:** Postman (for testing), IntelliJ or Eclipse

---

## 📁 Project Structure
src/
├── main/
│ ├── java/
│ │ └── org/yearup/
│ │ ├── controllers/
│ │ ├── dao/
│ │ ├── models/
│ │ ├── security/
│ │ └── services/
│ └── resources/
│ ├── application.properties
│ └── data.sql
└── test/