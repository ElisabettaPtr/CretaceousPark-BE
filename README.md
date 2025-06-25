![Java](https://img.shields.io/badge/Java-21-blue.svg?logo=java&logoColor=white) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-green?logo=springboot) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17.2-blue?logo=postgresql&logoColor=white) ![Hibernate](https://img.shields.io/badge/Hibernate-ORM-59666C.svg?logo=hibernate&logoColor=white) ![Maven](https://img.shields.io/badge/Maven--C71A36.svg?logo=apachemaven&logoColor=white) ![JWT](https://img.shields.io/badge/JWT-Authentication-orange.svg?logo=jsonwebtokens&logoColor=white) ![Made with Love](https://img.shields.io/badge/Made%20with-%E2%9D%A4-red)

# Cretaceous Park Backend

This is the backend of **Cretaceous Park**, a full-stack web application simulating a dinosaur-themed amusement park. The backend is built using **Spring Boot**, **Java 21**, **Hibernate**, and **PostgreSQL**.

## 🛠️ Technologies

* Java 21
* Spring Boot
* Hibernate (JPA)
* PostgreSQL
* Maven
* JWT Authentication

## 📁 Project Structure

The project follows a standard layered architecture:

* `controller`: Handles HTTP requests
* `service`: Business logic
* `repository`: Data access layer using Spring Data JPA
* `entity`: JPA models
* `dto`: Data transfer objects

## 🔐 Authentication

* JWT-based authentication
* Two roles: `ADMIN` and `CUSTOMER`
* Role-based access for various endpoints

## 🗄️ Database

* PostgreSQL
* Schema is auto-generated from JPA entities
* Sample schema provided as image (see `/docs/modelloER.png`)

## 🚀 Getting Started

### Prerequisites

* Java 21
* PostgreSQL
* Maven

### Environment Configuration

1. **Rename file `.env.example` to `.env`**
2. **Set variables values** :

```env
DATABASE_USERNAME=your_db_user
DATABASE_PASSWORD=your_db_password
DATABASE_NAME=your_db_name
JWT_SECRET=your_secret_key

```


* **Create PostgreSQL empty database naming it the same as the value you used for `DATABASE_NAME`**
* **Run the App**

## 📌 Main Features

* Full CRUD for all entities plus some other actions for main entities.
* Role-based access (admin can manage all; customer only their own data)
* Authentication & registration endpoints
* Automated planner for park visit
* Booking tickets for attractions and shows

## 🧪 Testing

Coming soon: unit and integration tests with JUnit and Mockito
