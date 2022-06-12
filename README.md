# Quiz Engine
The project creates a simple application to manage quizzes and solutions. The application is designed based on 
Model-View-Controller(MVC) architectural pattern. The application provides a list of APIs for 
registering users, logging in, creating quizzes and submitting according solutions.

## Techonologies
Project is created with Java Spring Boot with JPA and Postgres database at persistence layer. The application is secured 
by Spring Security framework.
 
## Installation 
First, please use your favorite IDE (IntelliJ/Eclipse) to build application QuizEngineApplication.
Or, use Maven to build and run the application from project home directory.  
    
     `./mvnw clean install`  
     `./mvnw spring-boot:run`

Then, simply deploy the whole application using docker-compose. This step will set up the Postgres database.

     `cd docker`  
     `docker-compose up -d`


## API Verification
Please use Postman or similar tools to verify APIs
* Users

  - Register user:  send a POST request with a valid email and password to URL
  
    URL: http://localhost:8080/users/register

    ![alt text](./images/register.png?raw=true) 

  - Login: send a POST with a valid email and associated password to URL
    
    URL: http://localhost:8080/users/login
  
    ![alt text](./images/login.png?raw=true)
    
    In the response, you will get the token to authenticate the user's requests.
* Quizzes
  - Create quiz
     
     URL: http://localhost:8080/quizzes

    ![alt text](./images/create_quiz.png?raw=true)

  - Add a question
  
     URL: http://localhost:8080/quizzes/addQuestion

    ![alt text](./images/add_question.png?raw=true)

  - Publish a quiz

     URL: http://localhost:8080/quizzes/publish/{encodedQuiz}

    ![alt text](./images/publish_quiz.png?raw=true)
* Solutions
  - Create a solution
     
     URL: http://localhost:8080/solutions

    ![alt text](./images/create_solution.png?raw=true)
    
    You should get the total score as percentage and subscores for all the questions.

  - Get solutions

     URL: http://localhost:8080/solutions

    ![alt text](./images/get_solutions.png?raw=true)

  - Get solutions by quiz

     URL: http://localhost:8080/solutions/byQuiz/{encodedQuiz}

    ![alt text](./images/get_solutions_by_quiz.png?raw=true)
