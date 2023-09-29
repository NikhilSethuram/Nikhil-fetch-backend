"
# Nikhil-fetch-backend
 backend project for fetch. For this project, we will be using Spring Boot and Java to create REST API's. There will be three endpoints:
 1) /add --> Method: POST
 Description: When a user has points added, we will use an add route that accepts a transaction containing the number of points to be added, the 
 payer through which the points will be added, and the timestamp for when the transaction takes place. If the transaction was added successfully, 
 then the endpoint should respond with a status code of 200. Does not include a response body
 2) /spend --> Method: POST
 Description: When a spend request comes in, the service uses the following rules to decide which payer to spend    
 points through:
 the oldest points to be spent first (oldest based on transaction timestamp, not the order they're received).
 If a request was made to spend more points than what a user has in total, then it returns a status code of 400 and a message saying the user doesn't 
 have enough points. 
 if successful in removing points, the endpoint responds with a status code of 200 and a list of payer names and the number of points that were 
 subtracted.
 3) /balance -->
 Method: GET
 Description: This route returns a map of points the user has in their account based on the payer they were added through. This endpoint can be  used  to see how many points the user has from each payer at any given time. Endpoint always returns a 200 status code

 A detailed summary of setup has been given below.

#STEP 1
Spring boot setup : https://start.spring.io/
Choose gradle(groovy), Java 17, Spring Boot 3.14 ( stable version) and package it as jar.
Dependencies to add :Lombok, Spring Web, JOOQ Access Layer, MYSQL Driver
Download, unzip, open in your preferred editor ( I used Intellij )

#STEP 2
MYSQL setup. I love working my backend with databases. I used MYSQL for this as it connects well with Spring Boot. 
make a table user with string payer, int points and datetime timestamp. 
Use the build.gradle with jooqgenerate ,mysql connection and dependencies setup.
Dont forget to update applicatiom.properties as it decides the port to run on, ours runs on 8000
use generatejooq command using gradle. you should see the generated tables, pojo classes under generated-db-entities

#STEP 3
We are all set up now. we are ready to use repository, service , model, and controller packages. 
We can run the BackendPorjectApplication.java. I tested mine using Postman and everything seems to work.

Thank you!



