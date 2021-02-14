# embl-codetest-challenge

This is the coding test from embl to create, read, update and delete of Person Object
It is build with Spring Security Basic Authentication with unit and integration test

### Application Structure

- Files must be located under `/src/main/java` to be picked up by Spring
- The source code is located in
  `src/main/java/com/emblproject/moses/`
- The project assumes `com.emblproject.moses.EmblProjectApplication` is the Spring Boot Application.

### Running the app

The project is dockerize with the dockerfile inside.

Follow this process carefully

1) Clone the project and type
   
    `mvn clean package`
   This will generate the jar file
   
2) Build the dockerfile with this command
   
   `docker build -t moses/embl-project .`
   
3) Run the project with this command.  
   `docker run -p 8080:8080 moses/embl-project`
   
Then you can test the project on postman or any other tools.

 `http://localhost:8080/persons`

### Endpoints

###Security
Every endpoints will need to be authenticated with the basic authorization

`username:moses`
 `password:mosespass`

The project has six endpoints. They are all defined in the `PersonController`:

- `GET /persons or /persons?size=5` - To get all persons with default size of 3 and can increase adding the param size
- `GET /persons/{id}` - a GET endpoint and returns a  single JSON `Person` object by ID 
- `POST /persons` - a POST endpoint that takes a JSON body of the `Person` object and returns a JSON `Persons` object saved in Databse

- `PUT /persons/{id}` - a PUT endpoint that takes the parameter `id` of the object to be updated, and the JSON body of the `Person` object, 
                        and returns a JSON `Person` object
- `DELETE /persons/{id}` - a DELETE endpoint to delete `Person` object by `id`
- `DELETE /persons` - a DELETE endpoint to delete all `Person` object

# To create person, use this payload
local setup running -- http://localhost:8080/persons

POST http://localhost:8080/persons
`{
"first_name": "John",
"last_name": "Keynes",
"favourite_colour": "blue",
"age": "24"
}`

Response
`{
"id": 1,
"first_name": "John",
"last_name": "Keynes",
"age": "23",
"favourite_colour": "red"
}`

# To update person, use this payload
PUT http://localhost:8080/persons/1
`{
"first_name": "John",
"last_name": "Key",
"favourite_colour": "yellow",
"age": "24"
}`

Response
`{
"id": 1,
"first_name": "John",
"last_name": "Key",
"age": "24",
"favourite_colour": "yellow"
}`

# To Get the list of persons
GET http://localhost:8080/persons

# To Get a single person
GET http://localhost:8080/persons/1

# To Delete single person
DELETE http://localhost:8080/persons/id

# To Delete all person
DELETE http://localhost:8080/persons



