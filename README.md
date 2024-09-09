Securing API with JWT and OAuth2

This project demonstrates how to secure an API using Spring Boot, JWT, and OAuth2. The API includes authentication (register/login) and role-based access control (admin and user dashboards).
Prerequisites

    Java 17
    Maven
    Postman (for manual API testing)
    Spring Boot 3.x

Getting Started
1. Clone the Repository

bash

git clone https://github.com/your-repo/securing-api.git
cd securing-api

2. Build the Project

Run the following command to build the project using Maven:

bash

mvn clean install

3. Run the Application

Start the Spring Boot application:

bash

mvn spring-boot:run

The application will start on http://localhost:8080.
API Endpoints
HTTP Method	Endpoint	Role Required	Description
POST	/api/register	Public	Register a new user
POST	/api/login	Public	Login and get JWT token
POST	/api/validate	Public	Validate the provided JWT token
GET	/api/user/dashboard	USER, ADMIN	Access the user dashboard
GET	/api/admin/dashboard	ADMIN	Access the admin dashboard
Testing the API with Postman
Step 1: Register a New User

    Open Postman and create a new POST request.

    Set the URL to:

    bash

http://localhost:8080/api/register

In the Body section, choose raw and set the type to JSON.

Provide the following JSON payload:

json

{
    "username": "testuser",
    "password": "password123"
}

Click Send. You should receive a success message:

json

    {
        "jwt": "User registered successfully"
    }

Step 2: Login and Get JWT Token

    Create a new POST request in Postman.

    Set the URL to:

    bash

http://localhost:8080/api/login

In the Body section, choose raw and set the type to JSON.

Provide the following JSON payload:

json

{
    "username": "testuser",
    "password": "password123"
}

Click Send. You will receive a response with a JWT token:

json

    {
        "jwt": "your_generated_jwt_token"
    }

Step 3: Access User Dashboard (Requires JWT Token)

    Create a new GET request in Postman.

    Set the URL to:

    bash

http://localhost:8080/api/user/dashboard

In the Headers section, add a new header:

    Key: Authorization
    Value: Bearer your_generated_jwt_token (replace your_generated_jwt_token with the token received in Step 2)

Click Send. If the JWT token is valid and the user has the required role, you'll receive a response:

json

    Welcome to the user dashboard!

Step 4: Access Admin Dashboard (Requires JWT Token and Admin Role)

    Create a new GET request in Postman.

    Set the URL to:

    bash

http://localhost:8080/api/admin/dashboard

In the Headers section, add a new header:

    Key: Authorization
    Value: Bearer your_generated_jwt_token (replace your_generated_jwt_token with the token received in Step 2)

Click Send. If the JWT token is valid and the user has the required admin role, you'll receive a response:

json

    Welcome to the Admin Dashboard!

Step 5: Validate a JWT Token

    Create a new POST request in Postman.

    Set the URL to:

    bash

http://localhost:8080/api/validate

In the Params section, add the following parameter:

    Key: token
    Value: your_generated_jwt_token (replace your_generated_jwt_token with the token received in Step 2)

Click Send. You will receive a response indicating whether the token is valid:

json

    "Token is valid"

Notes

    The JWT token should be passed in the Authorization header as Bearer <token>.
    If you try to access the user or admin endpoints without a valid JWT, you'll receive a 403 Forbidden error.
    Only users with the ADMIN role can access the /api/admin/dashboard endpoint.

Dependencies

The project uses the following key dependencies:

    Spring Boot Starter Web
    Spring Boot Starter Security
    Spring Boot Starter OAuth2 Client
    JJWT (JSON Web Token)
