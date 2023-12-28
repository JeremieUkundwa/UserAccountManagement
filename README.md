**User Account Management System**

This repository contains a Spring MVC application showcasing key features and integration with PostgreSQL as the database management system. Follow the steps below to run the application locally.

**Prerequisites**

Java Development Kit (JDK) 11 or later

PostgreSQL database management system(DBMS) server running on port 5432

Apache Maven

**Setup Database**

Create a PostgreSQL database named irembo_user_account on port 5432.

Update the application.properties file in the src/main/resources directory with your database credentials:

  _spring.datasource.url=jdbc:postgresql://localhost:5332/irembo_user_account
  
  spring.datasource.username=your_username
  
  spring.datasource.password=your_password_
  

**Change Port Number**

Update the application.properties file in the src/main/resources directory with your port preferable configuration:
  _server.port=7000_

**Build and Run**

Open a terminal and navigate to the project root directory.

Build the project using Maven:
  _mvn clean install_
  
Run the application:

  _java -jar target/UserAccountManagment-0.0.1-SNAPSHOT.jar_
  
Access the application in your web browser at http://localhost:7000.

**Usage**

Navigate to the registration page at http://localhost:7000/user/signup to create a new user account.
Access the login page at http://localhost:7000/user/login to log in with your credentials.
Explore and test other features of the Spring MVC application.

**Contributing**

Feel free to contribute to the development of this project by submitting issues or pull requests.

License
This project is licensed under the MIT License - see the LICENSE file for details.
