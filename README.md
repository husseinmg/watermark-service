# watermark-service
The watermark service is a demo REST API application that simulates an asynchronous service used to watermark electronic publications (such as books, and journals).

## Features

This is a non-comprehensive list of the features of the app:


+ **Containerless Application:** The application is fully runnable from command line, it will start its embedded application server and database, hence it doesn't need any prior installations or configuration.
+ **Automated Tests:** Both Unit and Integration Tests are available, and part of the build process.
+ **One Line Builds and Deployments:** Application can be built, verified, and deployed using single line commands, making it easy to start and integrate with Contineous Integration tools such as Jenkins.
+ **REST API:** Implementation adheres to REST API design principles and best practices, presenting everything as a resource, that can be accessed with HTTP methods and parametrized URLs, getting relevant response content and HTTP status codes.
+ **Simulation:** There is no real service that watermarks the documents, yet it's simulated by a separate thread that changes watermark status in database after a configurable delay, thus helping testing different states of document.
+ **Asynchronous:** As the application simulates a time expensive operation, it's designed in an asynchronous way so that clients don't have to keep the connection open for too long, and server can handle many requests in a scalable way.
+ **Technologies Used:** Application showcases several technologies; Spring Boot, Spring Web, Spring Data, JPA, Maven, H2 DB (as in-memory database), JUnit, Mockito, and other technologies.

## Importing, Building, and Running the Application
### Importing the application into an IDE:
The application was coded in IntelliJ, yet it's built in an IDE agnostic way and should run on any IDE with little or no extra configuration, provided that it's checked out from Git correctly, and using "Import from Maven POM" or equivalent option on your IDE, as the Maven POM contains all the needed dependencies to make a buildable and runnable application.

### Building the application
As a Maven application, all you need is to navigate applications root (containing pom.xml) in your terminal, then issue the command `mvn clean package` in order to build the app, the app will be built as a JAR that you can find in "target" directory below application root.

### Running the application
As mentioned in features section, this is a containerless app, so it can be run by:
+ From IDE: by running the "Application" class, it has the main method that will start the server.
+ Using Maven and Spring boot: on the command line issue `mvn spring-boot:run`
+ By running the JAR: simply on the command line issue the command `java -jar .\target\watermark-service-0.1.0.jar`

All of those commands will cause the server to start up on localhost, and port 8080 by default.

### Testing the application
#### Manual Testing
**_Important Note on Manual Testing:_** As the application simulates a real time expensive watermarking process (10 seconds by default, configurable), then the behavior of the application changes, when a watermark request is made it doesn't mean it will be immediately accessible by the GET or HEAD methods, a test at say 5 seconds after creation would indicate that client should wait and retry later, while testing after 15 seconds from creation should return the watermarked document.

#### Testing using REST/HTTP tools
The best way to test the app is to use some specialzed tool (e.g. SoapUI, or the Google Chrome app "Postman"), because although the app is accessible from any browser, but any serious testing requires POSTing a document first, which would be hard to do without such tools. 

Calling the service is described in the API Documentation section, but basically the testing process goes as follows:

1. POSTing a new document: By building the correct POST body, and sending it to URL "http://localhost:8080/watermarks", this will create the a document representation.
2. You can try calling HEAD on the document (using its ID returned in response to POST) until it returns that watermark is created (HTTP Status code 201)
3. Retrieve the document and watermark by calling the service using its ID and in a GET method call.

### Checking the database
The H2 in-memory database has an exposed console (only in development environment), that can be accessed when the application is up and running, through this URL and connection data:

URL: http://localhost:8080/h2-console/login.jsp
JDBC URL: jdbc:h2:mem:springernature
username: sa

_Note:_ Console is not exposed if you run the application using the `java -jar` command.

## REST API Documentation

This is the documentation of various methods offered by the REST API, how it can be called, and what to expect from it:

### Create Watermark

Uploads the given file to database and start the watermarking process.

* **URL**

/watermarks

* **Method:**
  
   `POST`

* **Query/Body Params**

   **Required:**
 
   `file=[binary]` the file to be uploaded
   `author=[String]`
   `title=[String]`
   `content=[String, enumerated]` Must be one of: BOOK, Journal

   **Optional:**
 
   `topic=[String, enumerated]` Must be one of: MEDIA, BUSINESS, SCIENCE

* **Success Response:**

  * **Code:** 202 ACCEPTED <br />
    **Content:** `id` id of the accepted document, to be used later in querying watermarking status and retrieve the document.
 
* **Error Response:**

  * **Code:** 400 BAD REQUEST <br />
    **Reason:** A required parameter is missing. <br />
    **Content:** JSON object containing details about the error, including which parameter was missing from request.

  OR

  * **Code:** 422 UNPROCESSABLE ENTITY <br />
    **Reason:** An enumerated parameter had value that doesn't match allowed enumeration values. <br />
    **Content:** Error message indicating which parameter was faulty, and allowable values for it.

### Check Watermark creation status

Checks if for a given document the watermark is ready or not

* **URL**

/watermarks/{id}

* **Method:**
  
   `HEAD`
  
*  **URL Params**

   **Required:**
 
   `id=[integer]` id of the document to be retrieved

* **Success Response:**

  * **Code:** 201 CREATED , indicates that watermark has been added to document and it can now be retrieved <br /> 
 
* **Error Response:**

  * **Code:** 409 CONFLICT <br />
    **Reason:** Document is found but watermark is not created yet, resource should be polled later to check status. <br />
    
  OR

  * **Code:** 404 NOT FOUND <br />
    **Reason:** Document was not found, invalid ID was given. <br />
    **Content:** JSON object containing details about the error, including which parameter was missing from request.

  OR

  * **Code:** 422 UNPROCESSABLE ENTITY <br />
    **Reason:** An enumerated parameter had value that doesn't match allowed enumeration values. <br />
    **Content:** Error message indicating which parameter was faulty, and allowable values for it.

### Get Watermarked Document

Retrieves the watermarked document

* **URL**

/watermarks/{id}

* **Method:**
  
   `GET`
  
*  **URL Params**

   **Required:**
 
   `id=[integer]` id of the document to be retrieved

* **Success Response:**

  * **Code:** 200 OK  <br /> 
    **Content:** JSON messge that includes the document and the watermark associated with it.
    
* **Error Response:**

  * **Code:** 409 CONFLICT <br />
    **Reason:** Document is found but watermark is not created yet, resource should be polled later to check status. <br />
    
  OR

  * **Code:** 404 NOT FOUND <br />
    **Reason:** Document was not found, invalid ID was given. <br />
    **Content:** JSON object containing details about the error, including which parameter was missing from request.

  OR

  * **Code:** 422 UNPROCESSABLE ENTITY <br />
    **Reason:** An enumerated parameter had value that doesn't match allowed enumeration values. <br />
    **Content:** Error message indicating which parameter was faulty, and allowable values for it.

