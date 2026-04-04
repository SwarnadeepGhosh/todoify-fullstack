# Todoify Full Stack with Angular and Spring Boot

![Todoify](https://cdni.iconscout.com/illustration/free/thumb/free-concept-of-to-do-list-2112526-1785604.png)


### [Live Link](https://todoify.vercel.app) - (login using below credentials)

```properties
user.name=user
user.password=dummy
```

### [Backend URL](https://todoify-backend.azurewebsites.net) - Deployed in Azure (Authenticate using below command to get JWT token)
```sh
curl --location --request POST 'https://todoify-backend.azurewebsites.net/authenticate' \
--header 'Origin: https://todoify.vercel.app' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "user",
    "password": "dummy"
}'
```

### [Backend URL 2](https://todoify-fullstack-api.onrender.com) - Deployed in Azure (Authenticate using below command to get JWT token)
```sh
curl --location 'https://todoify-fullstack-api.onrender.com/authenticate' \
--header 'Origin: https://todoify2.vercel.app/' \
--header 'Content-Type: application/json' \
--data '{
    "username": "user",
    "password": "dummy"
}'
```


## Introduction

Here I designed an Angular + Spring Boot Fullstack application with JWT authentication for a Todo List application, where logged in user can Create, Update, Delete todos of their own.

**Tech Stacks used :** **Angular, Spring Boot, Spring JPA, Spring Security, PostgreSQL DB, Lombok, Slf4j Logger.** 



## Features

1. Logged In user can check all Todo saved by him earlier.
2. User can Create / Update / Delete todo from the frontend page itself.
3. If user created new Todo, it will dave into real time database. next-time user access the todo page, user can view previously saved todo also.
4. Without logged in, user cannot view the Todos or Welcome page.
5. Slf4j Logging has been added and logs automatically saving into external file for future purpose.
6. Curently I am working for custom login for this project.



### Backend APIs for CRUD

| Operation      | Request Method | URI                               | Returns                                                      |
| :------------- | :------------- | :-------------------------------- | :----------------------------------------------------------- |
| Read all todos | GET            | /users/{username}/todos           | 200 OK with Todo List                                        |
| Read one todo  | GET            | /users/{username}/todos/{todo_id} | 200 OK with one Todo                                         |
| Create a todo  | POST           | /users/{username}/todos/          | 201 CREATED                                                  |
| Update a todo  | PUT            | /users/{username}/todos/{todo_id} | 200 OK with updated Todo                                     |
| Delete a todo  | DELETE         | /users/{username}/todos/{todo_id} | 204 NO CONTENT => for Successful Deletion, 404 NOT FOUND => for Todo Not Found |


### Docker commands

```sh
# Build
docker build -t swarnadeepghosh/todoify-fullstack:0.0.1-RELEASE .
 
# Run in local
docker run -d -p 8080:8080 --name=todoify-fullstack swarnadeepghosh/todoify-fullstack:0.0.1-RELEASE

# push to dockerhub
docker push swarnadeepghosh/todoify-fullstack:0.0.1-RELEASE
```


