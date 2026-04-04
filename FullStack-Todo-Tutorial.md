​	

# Todo Full Stack with Angular and Spring Boot

[Live Link](https://todo-fullstack-swarna.herokuapp.com/) :  https://todo-fullstack-swarna.herokuapp.com/

Repository Link : https://github.com/in28minutes/full-stack-with-angular-and-spring-boot



## Table of contents

- [Frontend](#frontend)
  - [Introduction](#introduction)
  - [Todo List Design](#todo-list-design)
    - [List-Todos Component](#list-todos-component)
  - [Header-Footer-Error Component](#header-footer-error-component)
  - [Frontend Authentication](#frontend-authentication)
  - [RouteGuard Service](#routeguard-service)
- [Backend](#backend)
    - [Basic Backend Setup](#basic-backend-setup)
  - [Connnecting Backend to Frontend](#connnecting-backend-to-frontend)
  - [CRUD Operation](#crud-operation)
  - [READ](#read)
  - [DELETE](#delete)
  - [UPDATE](#update)
  - [CREATE](#create)
- [Authentication with JWT](#authentication-with-jwt)
  - [HardCoded Authentication](#hardcoded-authentication)
    - [Backend Hardcoded Auth](#backend-hardcoded-auth)
    - [Frontend Hardcoded Auth](#frontend-hardcoded-auth)
  - [Authentication using Backend Creds](#authentication-using-backend-creds)
    - [Steps](#steps)
  - [Spring Security with JWT](#spring-security-with-jwt)
    - [Frontend JWT Changes](#frontend-jwt-changes)
    - [Backend JWT Changes](#backend-jwt-changes)
  - [Endpoints](#endpoints)
- [Using Database](#using-database)
- [Deploying FullStack in Heroku](#deploying-fullstack-in-heroku)
  - [Deploying Backend](#deploying-backend)
  - [Deploying Frontend with Environment](#deploying-frontend-with-environment)
- [Logging by SLF4j](#logging-by-slf4j)
    - [Rolling Log](#rolling-log)
- [Caching by Hazlecast](#caching-by-hazlecast)
    
    



## Frontend 



### Introduction

Final App we'll build 

<img src="images/finalapp.png" alt="finalapp" style="zoom: 67%;" />

Learning Steps

<img src="images/LearningSteps.png" alt="LearningSteps" style="zoom: 67%;" />

AngularLearningTopics

<img src="images/AngularLearningTopics.png" alt="AngularLearningTopics" style="zoom:50%;" />

- Generated frontend project - `ng new todo`

##### AppComponent

***app.component.html***

```html
<router-outlet></router-outlet>
```

​	***app-routing.module.ts***

```typescript
const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'login', component: LoginComponent },
  { path: 'welcome/:name', component: WelcomeComponent }, //Activated Route Parameters
  { path: '**', component: ErrorComponent },
];
...
```

***app.module.ts***

```typescript
@NgModule({
  declarations: [
    AppComponent,
    WelcomeComponent,
    LoginComponent,
    ErrorComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule
  ],
...
```



##### LoginComponent

***login.component.html***

```html
Username : <input type="text" name="username" [(ngModel)] = "username">
Password : <input type="password" name="password" [(ngModel)] = "password">
<button (click)="handleLogin()">Login</button> <br>
<small *ngIf="invalidLogin">Login Unsuccessful</small>
```

***login.component.ts***

```typescript
export class LoginComponent implements OnInit {
  username = '';
  password = '';
  invalidLogin = false;
    
  constructor(private router: Router) {}
  ngOnInit(): void {}

  handleLogin() {
    if (this.username === 'user' && this.password === 'pass') {
      this.router.navigate(['welcome', this.username])
      this.invalidLogin = false;
      // console.log("Login successful of User : " + this.username)
    } else {
      this.invalidLogin = true
    }
  }
}
```



##### WelcomeComponent

***welcome.component.html***

```html
<p>welcome {{name}} !! to our awesome app</p>
```

***welcome.component.ts***

```typescript
export class WelcomeComponent implements OnInit {
  name = ''
  constructor(private aroute: ActivatedRoute) { }
  ngOnInit(): void {
    this.name =this.aroute.snapshot.params['name']
  }
}
```



### Todo List Design

#### List-Todos Component

***app-routing.module.ts***

```typescript
{ path: 'todos', component: ListTodosComponent },
```

***list-todos.component.html***

```html
<table border="1">
    <caption>My Todos</caption>
  <thead>
    <tr>
      <th>Description</th>
      <th>Date</th>
      <th>Is Completed ?</th>
    </tr>
  </thead>
  <tbody>
    <tr *ngFor="let todo of todos">
      <td>{{ todo.description }}</td>
      <td>{{ todo.targetDate | date }}</td>
      <td>{{ todo.done }}</td>
    </tr>
  </tbody>
</table>
```

***list-todos.component.ts***

```typescript
export class Todo {
  constructor(
    public id: number,
    public description: string,
    public done: boolean,
    public targetDate: Date
  ) {}
}

@Component({
    ... 
export class ListTodosComponent implements OnInit {
  todos = [
    new Todo(1, 'My first Todo', true, new Date()),
    new Todo(1, 'Welcome to India', false, new Date()),
    new Todo(1, 'Angular project', true, new Date()),
  ];
...
}
```

***welcome.component.html*** - adding routing from welcome page to TodoList page

```html
<p>welcome {{name}} !! to our awesome app</p>
<h3>You can manage your todos <a routerLink="/todos">here</a>.</h3>
```

Snapshot after TodoList component. (Before Bootstrap styling)

<img src="images\before-bootstrap.png" alt="before-bootstrap" style="zoom:67%;" />

##### Added Bootstrap

```shell
$ npm install bootstrap
$ npm install jquery
 //   "bootstrap": "^4.6.1",
 //  "jquery": "^3.6.0",
```

***angular.json***

```json
"architect": {
    "build": { ...
        "styles": [
            "src/styles.css",
            "./node_modules/bootstrap/dist/css/bootstrap.min.css"
        ],
        "scripts": [
            "./node_modules/jquery/dist/jquery.js",
            "./node_modules/bootstrap/dist/js/bootstrap.js"
        ] ...
```





### Header-Footer-Error Component

Created 3 components for Header, Footer and Error.

***nav.component.html*** - Header

```html
<nav class="navbar navbar-expand-lg navbar-dark mb-2" style="background-color: #016f77;">
    <a class="navbar-brand" routerLink="/">Todo Fullstack</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <!--<li *ngIf="!loggedIn()" class="nav-item">
                <a class="nav-link" routerLink="/login" routerLinkActive="active" [routerLinkActiveOptions]="{exact: true}">Login <span class="sr-only">(current)</span></a>
            </li>-->
            <li class="nav-item">
                <a class="nav-link" routerLink="/welcome/user" routerLinkActive="active">Home</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" routerLink="/todos" routerLinkActive="active">Todos</a>
            </li>
        </ul>

        <ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link" routerLink="/login" routerLinkActive="active">Login</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" routerLink="/logout" routerLinkActive="active">Logout</a>
            </li>
        </ul>

    </div>
</nav>
```



***footer.component.html*** - Footer

```html
<footer class="footer text-light">
    <div class="container align-middle text-center">
        <i>Made with ❤ by <a class="text-light" href="https://swarnadeepghosh.github.io/">Swarnadeep</a></i>
    </div>
</footer>
```

***footer.component.css*** - Footer

```css
.footer{
    position: absolute;
    bottom: 0;
    width: 100%;
    height: 40px;
    background-color: #016f77;
}
.container{
    padding: 8px;
}
```



***error.component.html*** - For error page

```html
<div class="d-flex my-5 justify-content-center align-items-center align-middle" id="error">
    <h1 class="mr-3 pr-3 align-top border-right inline-block align-content-center">404</h1>
    <div class="inline-block align-middle">
    	<h2 class="font-weight-normal lead" id="desc">The page you requested was not found. Please contact administrator.</h2>
    </div>
</div>
```

***error.component.css*** - For error page

```css
#error { height: 100%; }
```



##### Setting Bootstrap designs

***app-routing.module.ts*** - Routing Module

```typescript
const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'welcome/:name', component: WelcomeComponent },
  { path: 'todos', component: ListTodosComponent },
  { path: '**', component: ErrorComponent },
];
```

***app.component.html***

```html
<app-nav></app-nav>
<div class="container">
  <router-outlet></router-outlet>
</div>
<app-footer></app-footer>
```



***list-todos.component.html***

```html
<h2>My Todo's</h2>

<div class="container">
  <table class="table">
    <thead>
      <tr>
        <th>Description</th>
        <th>Date</th>
        <th>Is Completed ?</th>
      </tr>
    </thead>
    <tbody>
      <tr *ngFor="let todo of todos">
        <td>{{ todo.description }}</td>
        <td>{{ todo.targetDate | date }}</td>
        <td>{{ todo.done }}</td>
      </tr>
    </tbody>
  </table>
</div>

```



***login.component.html***

```html
<form>
    <div class="form-group">
      <label for="user">Username</label>
      <input type="text" class="form-control" id="user" name="username" [(ngModel)] = "username">
    </div>
    <div class="form-group">
        <label for="password">Password</label>
        <input type="password" class="form-control" id="password" name="password" [(ngModel)] = "password">
      </div>
    <button type="submit" class="btn btn-primary" (click)="handleLogin()">Login</button>
    <i *ngIf="invalidLogin" class="text-danger m-4">Login Unsuccessful</i>
</form>
```

***welcome.component.html***

```html
<h1>Welcome!!</h1>
<p>Welcome <b>{{ name }}</b> to Todo Fullstack app designed with -</p>
<ul>
  <li>Frontend using Angular</li>
  <li>Backend using Spring Boot</li>
  <li>Authentication using Spring Security</li>
</ul>

<p>You can manage all your todos <a routerLink="/todos"><b>here</b></a>.</p>
```





### Frontend Authentication

Created a service hardcodedAuth for Hardcoded Authentication.

`$ ng g service hardcodedAuth`

##### Hardcoded Authentication Service

***hardcoded-auth.service.ts*** -- We are hardcoding, checking if Logged In and removing  user on logout thorugh this service.

```typescript
export class HardcodedAuthService {
  constructor() {}

  authenticate(username: string, password: string) {
    if (username === 'user' && password === 'pass') {
      sessionStorage.setItem('authenticatedUser', username);
      return true;
    } else {
      return false;
    }
  }

  isUserLoggedIn() {
    let user = sessionStorage.getItem('authenticatedUser');
    return !(user === null);
  }

  logout() { let user = sessionStorage.removeItem('authenticatedUser'); }
}
```

***login.component.ts***

```typescript
...
handleLogin() {
    // if (this.username === 'user' && this.password === 'pass') {
    if (this.hardcodedAuthService.authenticate(this.username, this.password)) {
...
```



##### Logout Component

***logout.component.html*** 

```html
<h1> You have successfully logged out.</h1>
<p>Thanks for using our application.</p>
```

***logout.component.ts***

```typescript
export class LogoutComponent implements OnInit {
    constructor(private hardcodedAuthService: HardcodedAuthService) {}
    ngOnInit(): void { this.hardcodedAuthService.logout(); }
}
```



##### Allowing Nav items to Loggedin User

***nav.component.html*** - configured the below lines.

```html
...
<a *ngIf="hardcodedAuthService.isUserLoggedIn()"  class="nav-link" routerLink="/todos" routerLinkActive="active">Todos</a>
<a *ngIf="!hardcodedAuthService.isUserLoggedIn()"  class="nav-link" routerLink="/login" routerLinkActive="active">Login</a>
<a *ngIf="hardcodedAuthService.isUserLoggedIn()"  class="nav-link" routerLink="/logout" routerLinkActive="active">Logout</a>
...
```

***nav.component.ts***

```typescript
constructor(public hardcodedAuthService : HardcodedAuthService) { }
```



### RouteGuard Service

***route-guard.service.ts***

```typescript
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { HardcodedAuthService } from './hardcoded-auth.service';

@Injectable({ providedIn: 'root',})
export class RouteGuardService implements CanActivate {
  constructor(private hardcodedAuthService: HardcodedAuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    if (this.hardcodedAuthService.isUserLoggedIn()) { return true; }
    else{
      this.router.navigate(['/login']);
      return false;
    }
  }
}
```

***app-routing.module.ts***

```typescript
const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'welcome/:name', component: WelcomeComponent, canActivate:[RouteGuardService] },
  { path: 'todos', component: ListTodosComponent, canActivate:[RouteGuardService] },
  { path: 'logout', component: LogoutComponent, canActivate:[RouteGuardService] },
  { path: '**', component: ErrorComponent },
];
```





## Backend

#### Basic Backend Setup

Created a Spring Boot starter project TodoFullStack using below dependencies:

```
spring-boot-starter-web
spring-boot-starter-data-jpa
spring-boot-devtools
postgresql
```

***backend\TodoFullStack\src\main\java\com\swarna\todoFullStack\HelloWorldBean.java***

```java
package com.swarna.todoFullStack;
public class HelloWorldBean {

	public String message;
	public HelloWorldBean(String message) {
		this.message = message;
	}
    // Auto Generated Getter, Setter and ToString below ...
}
```

***backend\TodoFullStack\src\main\java\com\swarna\todoFullStack\HelloController.java*** - Created 3 API

- `/hello` - returns String hardcoded rsponse
- `/hello-bean` - returns  new HelloWorldBean object which has property "message"
- `/hello/path-variable/{name}` - prints path variable also

```java
package com.swarna.todoFullStack;

// Allowing localhost:4200 so that backend can be called by frontend server
@CrossOrigin(origins = "http://localhost:4200") 
@RestController
public class HelloController {

	@GetMapping("hello")
	public String helloWorld() {
		return "Hello world";
	}

	@GetMapping("hello-bean") // Bean is converting into JSON and send to view.
	public HelloWorldBean helloWorldBean() {
		// throw new RuntimeException("Some error occurred. Please contact helpdesk.");
		return new HelloWorldBean("Hello World from HelloWorldBean backend"); 
	}

	@GetMapping("hello/path-variable/{name}")
	public HelloWorldBean helloWorldBeanWithPathVariable(@PathVariable String name) {
		return new HelloWorldBean(String.format("Hello World from Path Variable, Username : %s", name));
	}
}
```



### Connnecting Backend to Frontend

***app.module.ts*** - importing HttpClientModule

```typescript
import { HttpClientModule } from '@angular/common/http'
  imports: [ ...
    HttpClientModule
	...
```

***welcome-data.service.ts*** - This will fetch data from backend by REST-API call and share the response to angular components.

```typescript
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

class HelloWorldBean {
  constructor(public message: String) {}
}

@Injectable({
  providedIn: 'root',
})
export class WelcomeDataService {
  constructor(private http: HttpClient) {}

  executeHelloWorldBeanService() {
    return this.http.get<HelloWorldBean>('http://localhost:8080/hello-bean');
    // returns> Observable {source: Observable, operator: ƒ} and no call in network tab
    // console.log('executeHelloWorldBean');
  }

  executeHelloWorldBeanServiceWithParameter(name: any) { // Fetching Path Variable from backend
    return this.http.get<HelloWorldBean>(`http://localhost:8080/hello/path-variable/${name}`);
  }
}
```



***welcome.component.ts*** - Added 2 methods to handle success and error response, 1 method to fetch Welcome message without parameter and 1 method to fetch Welcome message with parameter

```typescript
export class WelcomeComponent implements OnInit {
  welcomeMessageFromService: string | undefined;
  ...
  handleSuccessResponse(response: any) {
    this.welcomeMessageFromService = response.message;
    console.log(response.message);
  }
  handleErrorResponse(error: any) {
    this.welcomeMessageFromService = error.error.message;
  }

  getWelcomeMessage() {
    // console.log(this.welcomeDataService.executeHelloWorldBeanService());
    this.welcomeDataService.executeHelloWorldBeanService().subscribe(
      (response) => this.handleSuccessResponse(response),
      (error) => this.handleErrorResponse(error)
    );
    // console.log('Last line of getWelcomeMessage'); // this will be print before response because observable is asynchronous call
  }

  getWelcomeMessageWithParameter() {
    this.welcomeDataService
      .executeHelloWorldBeanServiceWithParameter(this.name)
      .subscribe(
        (response) => this.handleSuccessResponse(response),
        (error) => this.handleErrorResponse(error)
      );
  }
}
```

***welcome.component.html*** - created a button that will fetch data from local backend server and show output message in html page.

```typescript
...
<div class="container">
  Click here to get welcome message from backend 
  <button (click)="getWelcomeMessageWithParameter()" class="btn btn-primary">Get Welcome Message</button>
</div>

<div class="container" *ngIf="welcomeMessageFromService">
  <h3>Welcome Message : </h3>
  {{welcomeMessageFromService}}
</div>
```



### CRUD Operation

| Operation      | Request Method | URI                               | Returns                                                      |
| -------------- | -------------- | --------------------------------- | ------------------------------------------------------------ |
| Read all todos | GET            | /users/{username}/todos           | 200 OK with Todo List                                        |
| Read one todo  | GET            | /users/{username}/todos/{todo_id} | 200 OK with one Todo                                         |
| Create a todo  | POST           | /users/{username}/todos/          | 201 CREATED                                                  |
| Update a todo  | PUT            | /users/{username}/todos/{todo_id} | 200 OK with updated Todo                                     |
| Delete a todo  | DELETE         | /users/{username}/todos/{todo_id} | 204 NO CONTENT => for Successful Deletion,<br/>404 NOT FOUND => for Todo Not Found |



**VsCode Lombok issue :** 

Adding Lombok in ***pom.xml***

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```

If your project loads before installing this plugin `Lombok Annotations Support for VS Code`, you can run this command in vscode to reload the project.

Press `Command + shift + P` and execute:

```java
Java: Clean Java language server workspace
```



##### Backend Model, Service, Controller

***Todo.java***

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Todo {
    private long id;
    private String username;
    private String description;
    private Date targetDate;
    private boolean isDone;
}
```

***TodoHardCodedService.java***

```java
@Service
public class TodoHardCodedService {
	private static List<Todo> todos = new ArrayList<>();
	private static int idCounter = 0;

	static {
		todos.add(new Todo(++idCounter, "user", "My first Todo", new Date(), false));
		todos.add(new Todo(++idCounter, "user", "Spring Boot microservices", new Date(), true));
		todos.add(new Todo(++idCounter, "user", "Angular", new Date(), false));
	}
}
```

***TodoController.java***

```java
@CrossOrigin(origins = "http://localhost:4200") // Allowing localhost:4200 so that backend can be called by frontend server
@RestController
public class TodoController {
    @Autowired
    private TodoHardCodedService todoService;
}
```



### READ

##### ReadTodos Backend

***TodoHardCodedService.java***

```java
public List<Todo> getAllTodos() {
    return todos;
}
```

***TodoController.java***

```java
@GetMapping("/users/{username}/todos")
public List<Todo> getAllTodos(@PathVariable String username) {
    return todoService.getAllTodos();
    // returns default 200 OK with Todo List
}
```



##### ReadTodos FrontEnd

***todo-data.service.ts*** - to get data from backend

```typescript
@Injectable({
  providedIn: 'root'
})
export class TodoDataService {
  constructor(private http: HttpClient) { }
  getAllTodos(username: any) {
    return this.http.get<Todo[]>(`http://localhost:8080/users/${username}/todos`);
  }
}
```

***list-todos.component.ts*** - to serve data from data service to frontend

```typescript
export class ListTodosComponent implements OnInit {
  message: string | undefined;
  todos: Todo[] = [];
  // [
  //   new Todo(1, 'My first Todo', true, new Date()),
  //   new Todo(1, 'Welcome to India', false, new Date()),
  //   new Todo(1, 'Angular project', true, new Date()),
  // ];

  constructor(private todoService: TodoDataService) {}

  ngOnInit(): void {
    this.refreshTodos();
  }
    
  refreshTodos(){
    this.todoService.getAllTodos('user').subscribe((response) => {
      // console.log(response);
      this.todos = response;
    });
  }
}

```



### DELETE

##### DeleteTodo Backend

***TodoHardCodedService.java***

```java
public Todo findById(long id) {
    for(Todo todo : todos){
        if(todo.getId() == id) return todo;
    }
    return null;
}
public Todo deleteById(long id){
    Todo todo = findById(id);

    if(todo == null) return null;
    if(todos.remove(todo)) return todo;
    return null;
}
```

***TodoController.java***

```java
@DeleteMapping("/users/{username}/todos/{id}")
public ResponseEntity<Void> deleteTodo(@PathVariable String username, @PathVariable long id) {
    Todo todo = todoService.deleteById(id);
    if (todo != null) {
        return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
    // returns 204 no Content for successful deletion and returns 404 for not found
}
```



##### DeleteTodo FrontEnd

***todo-data.service.ts*** - to get data from backend

```typescript
deleteTodo(username: any, id: number) {
    return this.http.delete(`http://localhost:8080/users/${username}/todos/${id}`);
}
```

***list-todos.component.ts*** - to serve data from data service to frontend

```typescript
deleteTodo(id: number) {
    console.log(`Todo Deleted ${id}`);
    this.todoService.deleteTodo('user', id).subscribe((response) => {
        console.log(response);
        this.message = `Delete of Todo ${id} successful`;
        this.refreshTodos();
    });
}
```

***list-todos.component.html***

```html
<h2>My Todo's</h2>

<div class="alert alert-success" *ngIf="message">{{message}}</div>

<div class="container">
  <table class="table">
    <thead>
      <tr>
        <th>Description</th>
        <th>Date</th>
        <th>Is Completed ?</th>
        <th>Delete</th>
      </tr>
    </thead>
    <tbody>
      <tr *ngFor="let todo of todos">
        <td>{{ todo.description }}</td>
        <td>{{ todo.targetDate | date }}</td>
        <td>{{ todo.done }}</td>
        <td><button (click)="deleteTodo(todo.id)" class="btn btn-danger">Delete</button></td>
      </tr>
    </tbody>
  </table>
</div>
```



### UPDATE

##### UpdateTodo Backend

***TodoHardCodedService.java***

```java
public Todo getTodo(long id) { // This data will be populated in update frontend screen
    return findById(id);
}

public Todo save(Todo todo) {
    if (todo.getId() == -1 || todo.getId() == 0) { // Saving Todo if not exist
        todo.setId(++idCounter);
        todos.add(todo);
    } else {
        deleteById(todo.getId()); // Deleting and adding Todo if already exists.
        todos.add(todo);
    }
    return todo;
}
```

***TodoController.java***

```java
@PutMapping("/users/{username}/todos/{id}")
public ResponseEntity<Todo> updateTodo(@PathVariable String username, @PathVariable long id,
                                       @RequestBody Todo todo) {
    Todo todoUpdated = todoService.save(todo);
    return new ResponseEntity<Todo>(todo, HttpStatus.OK); 
    // returns 200 OK status with updated Todo
}
```



##### UpdateTodo FrontEnd

We need **Todo Component** to **Update** / **Add** a new Todo in Todo List. (`ng generate component todo`)

***app-routing.module.ts*** - to get data from backend

```typescript
{ path: 'todos/:id', component: TodoComponent, canActivate:[RouteGuardService] },
```

***todo-data.service.ts*** - to get data from backend

```typescript
getTodo(username: any, id: number) {
    return this.http.get<Todo>(`http://localhost:8080/users/${username}/todos/${id}`);
}

updateTodo(username: any, id: number, todo: Todo) {
    return this.http.put(`http://localhost:8080/users/${username}/todos/${id}`, todo);
}
```



***list-todos.component.ts*** - to serve data from data service to frontend

```typescript
constructor(private todoService: TodoDataService, private router: Router) {}  
updateTodo(id: number) {
    // console.log(`Todo Updated- ${id}`);
    this.router.navigate(['todos', id]);
}
```

***list-todos.component.html***

```html
<th>Update</th>
...
<td><button (click)="updateTodo(todo.id)" class="btn btn-warning">Update</button></td>
```



***todo.component.ts*** - to Update / Add a Todo

```typescript
export class TodoComponent implements OnInit {
  id: number = 0;
  todo!: Todo;

  constructor(private todoService:TodoDataService, private route:ActivatedRoute, private router:Router) {}

  ngOnInit(): void {
    this.id = this.route.snapshot.params['id'];
    this.todo = new Todo(this.id, '', false, new Date()); // Initialising todo to avoid undefined error in console.

    if (this.id != -1) {
      this.todoService.getTodo('user', this.id).subscribe((data) => (this.todo = data));
    }
  }

  saveTodo() {
    if(this.id === -1){
      //Create todo
      this.todoService.createTodo('user', this.todo).subscribe((data) => {
          this.router.navigate(['todos']);
        });
    } else{
      this.todoService.updateTodo('user', this.id, this.todo).subscribe((data) => {
          this.router.navigate(['todos']);
        });
    }
  }
}
```

***todo.component.html***

```html
<h1>Update / Create Todo</h1>

<div class="container">
    <div class="alert alert-warning" *ngIf="todoForm.dirty && todoForm.invalid">Enter valid values</div>
    <div class="alert alert-warning" *ngIf="todoForm.dirty && description.invalid">Enter atleast 4 characters in Description</div>
    <div class="alert alert-warning" *ngIf="todoForm.dirty && targetDate.invalid">Enter valid Target Date</div>

    <form (ngSubmit)="!todoForm.invalid && saveTodo()" #todoForm="ngForm">
        <fieldset class="form-group">
            <label for="description">Description</label>
            <input type="text" [(ngModel)]="todo.description" #description="ngModel" name="description" class="form-control" required="required" minlength="4">
        </fieldset>
        <fieldset class="form-group">
            <label for="date">Target Date</label>
            <input type="date" [ngModel]="todo.targetDate | date:'yyyy-MM-dd'" (ngModelChange)="todo.targetDate=$event" #targetDate="ngModel"
            name="date" class="form-control" required="required">
        </fieldset>
    
        <button class="btn btn-success" type="submit">Save</button>
    </form>
</div>
```



### CREATE

##### AddTodo Backend

***TodoHardCodedService.java*** - No change -- `todoService.save(todo)` method is already added.

***TodoController.java***

```java
@PostMapping("/users/{username}/todos")
public ResponseEntity<Void> createTodo(@PathVariable String username, @RequestBody Todo todo) {
    Todo createdTodo = todoService.save(todo);

    // Creating a new URI for the new Todo and returning URL by appending id
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdTodo.getId()).toUri();
    System.out.println(uri);
    return ResponseEntity.created(uri).build();
    // returns Location = http://localhost:8080/users/user/todos/4 with response code 201 Created.
}
```



##### AddTodo FrontEnd

***todo-data.service.ts*** - to get data from backend

```typescript
createTodo(username: any, todo: Todo) {
    return this.http.post(`http://localhost:8080/users/${username}/todos`, todo);
}
```

***list-todos.component.ts*** - to serve data from data service to frontend

```typescript
addTodo(){
    this.router.navigate(['todos', -1]);
}
```

***list-todos.component.html***

```html
<div class="row">
    <button (click)="addTodo()" class="btn btn-success">Add Todo</button>
</div>
```







## Authentication with JWT

### HardCoded Authentication

#### Backend Hardcoded Auth

***pom.xml***

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

***basicAuth/SecurityConfigurationBasicAuth.java*** - Configuring CSRF(Cross Site Request Format) with Spring Security

```java
package com.swarna.todoFullStack.basicAuth;

@Configuration
@EnableWebSecurity
public class SecurityConfigurationBasicAuth extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // disabling Cross Site Request Format
                .authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();
        // .formLogin();
    }
}
```

***application.properties***

```properties
spring.security.user.name=user
spring.security.user.password=password
```



#### Frontend Hardcoded Auth

***app.module.ts*** -- adding HTTP Interceptor as providers, so that it can intercept all incoming requests 

```typescript
import { HttpInterceptorBasicAuthService } from './services/http/http-interceptor-basic-auth.service';

providers: [
    {provide: HTTP_INTERCEPTORS, useClass: HttpInterceptorBasicAuthService, multi: true},
  ],
```

***welcome-data.service.ts***

```typescript
  executeHelloWorldBeanServiceWithParameter(name: any) {
    let basicAuthHeaderString = this.createBasicAuthenticationHeader();
    let headers = new HttpHeaders({
      Authorization : basicAuthHeaderString
    })

    return this.http.get<HelloWorldBean>(`http://localhost:8080/hello/path-variable/${name}`, {headers});
  }

  createBasicAuthenticationHeader() {
    let username = 'user';
    let password = 'pass';
    let basicAuthHeaderString = 'Basic ' + window.btoa(username + ':' + password);
    return basicAuthHeaderString;
  }

  /* welcome/user:1 Access to XMLHttpRequest at 'http://localhost:8080/hello/path-variable/user' from origin 'http://localhost:4200' has been blocked by CORS policy: No 'Access-Control-Allow-Origin' header is present on the requested resource.*/
  // Access to XMLHttpRequest at 'http://localhost:8080/hello/path-variable/user' from origin 'http://localhost:4200' has been blocked by CORS policy: Response to preflight request doesn't pass access control check: No 'Access-Control-Allow-Origin' header is present on the requested resource.
  // OPTION is being sent before GET request happen and it is failing
}
```



But putting createBasicAuthenticationHeader() in each and individual incoming HTTP requests is a tedious task. So we will create a **HttpInterceptor** , which will intercept all incoming requests

***app\services\http\http-interceptor-basic-auth.service.ts***

```typescript
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HttpInterceptorBasicAuthService implements HttpInterceptor{

  // We will avoid adding Http headers in all Http requests. For eg, we will avoid below line. 
  // Thatswhy we will use HttpInterceptor which will be added automatically to all the http requests in our application. 
  // return this.http.get<HelloWorldBean>(`http://localhost:8080/hello/path-variable/${name}`, {headers});
  constructor() { }

  // We are intercpting incoming requests and adding Header entry, and returns the modified request back
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let username = 'user';
    let password = 'pass';
    let basicAuthHeaderString = 'Basic ' + window.btoa(username + ':' + password);

    request = request.clone({
      setHeaders: {
        Authorization : basicAuthHeaderString
      }
    })
    return next.handle(request);
  }
}
```



### Authentication using Backend Creds

#### Steps

Now we will create a Service which will return validated or not response to Angular frontend on login. This is the workflow to follow. 

1. We will authenticate users in frontend using credentials using backend credentials from ***application.properties***

2. For this target, we will generate ***BasicAuthController.java***, which will run at URI : `/basicauth` and it will return a ***AuthenticationBean***

3. Creating a file ***app.constants.ts*** to store all the constants. Initially put below code into it.

   ```typescript
   export const API_URL = "http://localhost:8080";
   ```

4. Added a new service ***basic-authentication.service.ts*** to call `/basicauth` and return *AuthenticationBean* to check if user is authenticated or not. 
5. Calling method `handleBasicAuthLogin()` of ***login.component.ts*** from ***login.component.html*** 
6. Removed all hardcoded dependency from frontend of ***http-interceptor-basic-auth.service.ts*** - so that for every requests, it will intercept them all, take **basicAuthHeaderString** from Session Storage, set it as **Authorization** header and send the modified request back with headers added in it. So frontend server can take data from backend seamlessly.
7. Removed hardcoded header addition in ***welcome-data.service.ts***, so that ***http-interceptor-basic-auth.service.ts*** can do the header-addition work automatically.

 

***AuthenticationBean.java***

```java
package com.swarna.todoFullStack.basicAuth;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationBean {
	public String message;
}
```

***BasicAuthController.java***

```java
package com.swarna.todoFullStack.basicAuth;
@CrossOrigin(origins = "http://localhost:4200") 
@RestController
public class BasicAuthController {

	@GetMapping("/basicauth") // Bean is converting into JSON and send to view.
	public AuthenticationBean helloWorldBean() {
		// throw new RuntimeException("Some error occurred. Please contact helpdesk.");
		return new AuthenticationBean("You are authenticated"); 
	}
}
```



***basic-authentication.service.ts***

```typescript
export const TOKEN = 'token';
export const AUTHENTICATED_USER = 'authenticatedUser';

@Injectable({
  providedIn: 'root'
})
export class BasicAuthenticationService {
  constructor(private http : HttpClient) { }

  executeAuthenticationService(username: string, password: string) {
    let basicAuthHeaderString = 'Basic ' + window.btoa(username + ':' + password);
    let headers = new HttpHeaders({
      Authorization : basicAuthHeaderString
    })
    // pipe means if this is successful, then do this. i.e, map 
    return this.http.get<AuthenticationBean>(`${API_URL}/basicauth`, {headers}).pipe(map(
      data => {
        sessionStorage.setItem(AUTHENTICATED_USER, username);
        sessionStorage.setItem(TOKEN, basicAuthHeaderString);
        return data;
      }
    ));
  }

  getAuthenticatedUser(){ return sessionStorage.getItem(AUTHENTICATED_USER); }

  getAuthenticatedToken(){
    if(this.getAuthenticatedUser())
      return sessionStorage.getItem(TOKEN); 
  }

  isUserLoggedIn() {
    let user = sessionStorage.getItem(AUTHENTICATED_USER);
    return !(user === null);
  }

  logout() {
    sessionStorage.removeItem(AUTHENTICATED_USER);
    sessionStorage.removeItem(TOKEN);
  }
}

export class AuthenticationBean{ constructor(public message : string){ } }
```



***login.component.ts***

```typescript
 constructor(
    private router: Router,
    private hardcodedAuthService: HardcodedAuthService,
    private basicAuthService: BasicAuthenticationService
  ) {}
  ...
  handleBasicAuthLogin() {
    this.basicAuthService.executeAuthenticationService(this.username, this.password).subscribe(
        (data) => {
          console.log(data);
          this.router.navigate(['welcome', this.username]);
          this.invalidLogin = false;
          // console.log("Login successful of User : " + this.username)
        },
        (error) => { 
          console.log(error);
          this.invalidLogin = true; 
        }
      );
  }
}
```



***login.component.html*** - Changed onclick method only.

```html
<!-- <button type="submit" class="btn btn-primary" (click)="handleLogin()">Login</button> -->
<button type="submit" class="btn btn-primary" (click)="handleBasicAuthLogin()">Login</button>
```



***http-interceptor-basic-auth.service.ts***

```typescript
export class HttpInterceptorBasicAuthService implements HttpInterceptor{
  constructor( private basicAuthenticationService: BasicAuthenticationService) { }

  // We are intercpting incoming requests and adding Header entry, and returns the modified request back
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    
      let basicAuthHeaderString = this.basicAuthenticationService.getAuthenticatedToken();
    let username = this.basicAuthenticationService.getAuthenticatedUser();

    if(basicAuthHeaderString && username){
      request = request.clone({
        setHeaders: {
          Authorization : basicAuthHeaderString
        }
      })
    }
    return next.handle(request); // returning the request so that it can do it next job.
  }
}
```



***welcome-data.service.ts***

```typescript
export class WelcomeDataService {
  constructor(private http: HttpClient) {}

  executeHelloWorldBeanService() {
    return this.http.get<HelloWorldBean>(`${API_URL}/hello-bean`);
  }

  executeHelloWorldBeanServiceWithParameter(name: any) {
    return this.http.get<HelloWorldBean>(`${API_URL}/hello/path-variable/${name}`/*,{headers}*/);
  }
}
```

***todo-data.service.ts***

```typescript
Replace -> http://localhost:8080
To this -> ${API_URL}
```





### Spring Security with JWT 

Here we will connect Spring Security and Spring Boot with JWT Framework.

#### Frontend JWT Changes

***basic-authentication.service.ts***

```typescript
  executeJWTAuthenticationService(username: string, password: string) {

    // pipe means if this is successful, then do this. i.e, map 
    return this.http.post<any>(`${API_URL}/authenticate`, { username, password}).pipe(map(
      data => {
        sessionStorage.setItem(AUTHENTICATED_USER, username);
        sessionStorage.setItem(TOKEN, `Bearer ${data.token}`);
        return data;
      }
    ));
  }
```



***login.component.ts***

```typescript
  handleJWTAuthLogin() {
    this.basicAuthService.executeJWTAuthenticationService(this.username, this.password).subscribe(
        (data) => {
          console.log(data);
          this.router.navigate(['welcome', this.username]);
          this.invalidLogin = false;
          // console.log("Login successful of User : " + this.username)
        },
        (error) => { 
          console.log(error);
          this.invalidLogin = true; 
        }
      );
  }
```

***login.component.html***

```html
<!-- <button type="submit" class="btn btn-primary" (click)="handleLogin()">Login</button> -->
<!-- <button type="submit" class="btn btn-primary" (click)="handleBasicAuthLogin()">Login</button> -->
<button type="submit" class="btn btn-primary" (click)="handleJWTAuthLogin()">Login</button>
```



#### Backend JWT Changes

***pom.xml***

```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.9.1</version>
</dependency>
```



- Moved the below 3 files from package **com.swarna.todoFullStack.hello** to **com.swarna.todo.basicAuth** 
  - `SecurityConfigurationBasicAuth.java`, `AuthenticationBean.java`, `BasicAuthController.java`

- Added below files for JWT configuration.
  - <img src="images\jwt_files.png" alt="jwt_files.png"  />

**`jwt.resource` package**

***`todoFullStack\jwt\resource\JwtAuthenticationRestController.java`***

```java
package com.swarna.todoFullStack.jwt.resource;

import java.util.Objects;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.swarna.todoFullStack.jwt.JwtTokenUtil;
import com.swarna.todoFullStack.jwt.JwtUserDetails;

@RestController
@CrossOrigin(origins = "${crossorigins.origin.url}")
public class JwtAuthenticationRestController {

	@Value("${jwt.http.request.header}")
	private String tokenHeader;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService jwtInMemoryUserDetailsService;

	@RequestMapping(value = "${jwt.get.token.uri}", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtTokenRequest authenticationRequest)
			throws AuthenticationException {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = jwtInMemoryUserDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtTokenResponse(token));
	}

	@RequestMapping(value = "${jwt.refresh.token.uri}", method = RequestMethod.GET)
	public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
		String authToken = request.getHeader(tokenHeader);
		final String token = authToken.substring(7);
		String username = jwtTokenUtil.getUsernameFromToken(token);
		JwtUserDetails user = (JwtUserDetails) jwtInMemoryUserDetailsService.loadUserByUsername(username);

		if (jwtTokenUtil.canTokenBeRefreshed(token)) {
			String refreshedToken = jwtTokenUtil.refreshToken(token);
			return ResponseEntity.ok(new JwtTokenResponse(refreshedToken));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}

	@ExceptionHandler({ AuthenticationException.class })
	public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}

	private void authenticate(String username, String password) {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new AuthenticationException("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new AuthenticationException("INVALID_CREDENTIALS", e);
		}
	}
}
```

***`todoFullStack\jwt\resource\JwtTokenRequest.java`***

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtTokenRequest implements Serializable {
	private static final long serialVersionUID = -5616176897013108345L;
	private String username;
	private String password;
}
```

**`todoFullStack\jwt\resource\JwtTokenResponse.java`**

```java
@Data
@AllArgsConstructor
@Builder
public class JwtTokenResponse implements Serializable {

	private static final long serialVersionUID = 8317676219297719109L;
	private final String token;
}
```

**`todoFullStack\jwt\resource\AuthenticationException.java`**

```java
package com.swarna.todoFullStack.jwt.resource;
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
```



**`jwt` package**

**`todoFullStack\jwt\JwtTokenUtil.java`**

```java
package com.swarna.todoFullStack.jwt;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;

@Component
public class JwtTokenUtil implements Serializable {

	static final String CLAIM_KEY_USERNAME = "sub";
	static final String CLAIM_KEY_CREATED = "iat";
	private static final long serialVersionUID = -3301605591108950415L;
	private Clock clock = DefaultClock.INSTANCE;

	@Value("${jwt.signing.key.secret}")
	private String secret;

	@Value("${jwt.token.expiration.in.seconds}")
	private Long expiration;

	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getIssuedAtDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getIssuedAt);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(clock.now());
	}

	private Boolean ignoreTokenExpiration(String token) {
		// here you specify tokens, for that the expiration is ignored
		return false;
	}

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails.getUsername());
	}

	private String doGenerateToken(Map<String, Object> claims, String subject) {
		final Date createdDate = clock.now();
		final Date expirationDate = calculateExpirationDate(createdDate);

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(createdDate)
				.setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public Boolean canTokenBeRefreshed(String token) {
		return (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}

	public String refreshToken(String token) {
		final Date createdDate = clock.now();
		final Date expirationDate = calculateExpirationDate(createdDate);

		final Claims claims = getAllClaimsFromToken(token);
		claims.setIssuedAt(createdDate);
		claims.setExpiration(expirationDate);

		return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		JwtUserDetails user = (JwtUserDetails) userDetails;
		final String username = getUsernameFromToken(token);
		return (username.equals(user.getUsername()) && !isTokenExpired(token));
	}

	private Date calculateExpirationDate(Date createdDate) {
		return new Date(createdDate.getTime() + expiration * 1000);
	}
}
```

***`todoFullStack\jwt\JwtInMemoryUserDetailsService.java`***

```java
package com.swarna.todoFullStack.jwt;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtInMemoryUserDetailsService implements UserDetailsService {

	static List<JwtUserDetails> inMemoryUserList = new ArrayList<>();

	static {
		inMemoryUserList.add(new JwtUserDetails(1L, "user",
				"$2a$10$3zHzb.Npv1hfZbLEU5qsdOju/tk2je6W6PnNnY.c1ujWPcZh4PL6e", "ROLE_USER_2"));
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<JwtUserDetails> findFirst = inMemoryUserList.stream()
				.filter(user -> user.getUsername().equals(username)).findFirst();

		if (!findFirst.isPresent()) {
			throw new UsernameNotFoundException(String.format("USER_NOT_FOUND '%s'.", username));
		}
		return findFirst.get();
	}
}

```

***`todoFullStack\jwt\JwtTokenAuthorizationOncePerRequestFilter.java`***

```java
package com.swarna.todoFullStack.jwt;
import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtTokenAuthorizationOncePerRequestFilter extends OncePerRequestFilter {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserDetailsService jwtInMemoryUserDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Value("${jwt.http.request.header}")
	private String tokenHeader;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		logger.debug("Authentication Request For '{}'", request.getRequestURL());

		final String requestTokenHeader = request.getHeader(this.tokenHeader);

		String username = null;
		String jwtToken = null;
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				logger.error("JWT_TOKEN_UNABLE_TO_GET_USERNAME", e);
			} catch (ExpiredJwtException e) {
				logger.warn("JWT_TOKEN_EXPIRED", e);
			}
		} else {
			logger.warn("JWT_TOKEN_DOES_NOT_START_WITH_BEARER_STRING");
		}

		logger.debug("JWT_TOKEN_USERNAME_VALUE '{}'", username);
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = this.jwtInMemoryUserDetailsService.loadUserByUsername(username);

			if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}

		chain.doFilter(request, response);
	}
}
```

***`todoFullStack\jwt\JwtUnAuthorizedResponseAuthenticationEntryPoint.java`***

```java
package com.swarna.todoFullStack.jwt;

import java.io.IOException;
import java.io.Serializable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtUnAuthorizedResponseAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

	private static final long serialVersionUID = -8970718410437077606L;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
				"You would need to provide the Jwt Token to Access This resource");
	}
}
```

***`todoFullStack\jwt\JwtUserDetails.java`***

```java
package com.swarna.todoFullStack.jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class JwtUserDetails implements UserDetails {

	private static final long serialVersionUID = 5155720064139820502L;

	private final Long id;
	private final String username;
	private final String password;
	private final Collection<? extends GrantedAuthority> authorities;

	public JwtUserDetails(Long id, String username, String password, String role) {
		this.id = id;
		this.username = username;
		this.password = password;

		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(role));

		this.authorities = authorities;
	}

	@JsonIgnore
	public Long getId() {
		return id;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
```

***`todoFullStack\jwt\JWTWebSecurityConfig.java`***

```java
package com.swarna.todoFullStack.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class JWTWebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtUnAuthorizedResponseAuthenticationEntryPoint jwtUnAuthorizedResponseAuthenticationEntryPoint;

	@Autowired
	private UserDetailsService jwtInMemoryUserDetailsService;

	@Autowired
	private JwtTokenAuthorizationOncePerRequestFilter jwtAuthenticationTokenFilter;

	@Value("${jwt.get.token.uri}")
	private String authenticationPath;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(jwtInMemoryUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}

	// @Bean
	// public PasswordEncoder passwordEncoderBean() {
	// return new BCryptPasswordEncoder();
	// }

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable().exceptionHandling()
				.authenticationEntryPoint(jwtUnAuthorizedResponseAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests().anyRequest()
				.authenticated();

		httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

		httpSecurity.headers().frameOptions().sameOrigin() // H2 Console Needs this setting
				.cacheControl(); // disable caching
	}

	@Override
	public void configure(WebSecurity webSecurity) throws Exception {
		webSecurity.ignoring().antMatchers(HttpMethod.POST, authenticationPath).antMatchers(HttpMethod.OPTIONS, "/**")
				.and().ignoring().antMatchers(HttpMethod.GET, "/" // Other Stuff You want to Ignore
				).and().ignoring().antMatchers("/h2-console/**/**");// Should not be in Production!
	}
}
```



### Endpoints

```sh
# JWT generate Token 
curl --location --request POST 'http://localhost:8080/authenticate' \
--header 'Origin: http://localhost:4200' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "user",
    "password": "dummy"
}'

# JWT refresh Token
curl --location --request GET 'http://localhost:8080/refresh' \
--header 'Origin: http://localhost:4200' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNjUzNTM3MzkwLCJpYXQiOjE2NTI5MzI1OTB9.Ap-ydDmt7jsJEtvM15d6y5zUJllE9LXyHpNuivTFuKQs-5jBCk104nwq_Ztq1uuORC8TOzMfyxWuei3VlalJYg'

# Get all Todos for a user
curl --location --request GET 'http://localhost:8080/users/user/todos' \
--header 'Origin: http://localhost:4200' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNjUzNTkxMDcyLCJpYXQiOjE2NTI5ODYyNzJ9.5HPfVvVQiY6OVFfw0OevJ2kqRI4weLtvegUB7d066lKcAY1pCndY6iyAkx60sc5kwtgHKnq43z8gQJzBEwduxA'
```





## Using Database

To connect database with our fullstack application , we need to configure `application.properties`, Model , Repository, Service and Controller.



***application.properties***

```properties
# Spring Security Properties
spring.security.user.name=user
spring.security.user.password=dummy
crossorigins.origin.url=http://localhost:4200
# crossorigins.origin.url=https://todo-fullstack-swarna.herokuapp.com

# JWT properties
jwt.signing.key.secret=mySecret
jwt.get.token.uri=/authenticate
jwt.refresh.token.uri=/refresh
jwt.http.request.header=Authorization
jwt.token.expiration.in.seconds=604800

############### JPA config ###############
#spring.h2.console.enabled=true
#update means directly change in db while I run application
spring.jpa.hibernate.ddl-auto=update
# spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql=true
# spring.jpa.defer-datasource-initialization=true
spring.sql.init.mode=always
#spring.datasource.initialization-mode=always #--> Or for Spring Boot before 2.5:

############### Connecting to Postgres Database ###############
#spring.datasource.url=jdbc:postgresql://host:port/database
spring.datasource.url=jdbc:postgresql://free-tier12.aws-ap-south-1.cockroachlabs.cloud:26257/swarna-db-200.defaultdb
spring.datasource.username=swarnadeep
spring.datasource.password=uLYrds69nT_WNO5vEQn9rQ
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.PostgreSQLDialect
#spring.jooq.sql-dialect=postgres


############### Logging ###############
# logging.file.name=logs/application.log
logging.level.root=INFO
# Log Level for springframework (info, error, debug etc)
#logging.level.org.springframework = error
# Log Level for Specific Controller
#logging.level.com.swarna.todoFullStack.todo = error
```



***`backend\TodoFullStack\src\main\java\com\swarna\todoFullStack\todo\Todo.java`***

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="TODO_FULLSTACK", schema = "public")
public class Todo {

    @Id
    @SequenceGenerator(name = "todo_seq",sequenceName = "todo_seq",allocationSize = 1, schema = "public")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "todo_seq")
    private Long id;

    private String username;
    private String description;
    private Date targetDate;
    private boolean isDone;
}
```

***`backend\TodoFullStack\src\main\java\com\swarna\todoFullStack\TodoRepository.java`***

```java
public interface TodoRepository extends CrudRepository<Todo, Long>{
    public List<Todo> findByUsername(String username);
}
```

***`backend\TodoFullStack\src\main\java\com\swarna\todoFullStack\todo\TodoService.java`***

```java
@Service
public class TodoService {
	
	@Autowired
	private TodoRepository todoRepository;
    
	public List<Todo> getAllTodos(String username) {
		return todoRepository.findByUsername(username);
		// todoRepository.findAll().forEach(t -> todos.add(t));;
		// return todos;
	}

	public Optional<Todo> getTodo(long id) {
		return todoRepository.findById(id);
	}

	public Todo save(Todo todo, String username) {
		todo.setUsername(username);
        Todo savedTodo = todoRepository.save(todo);
		return savedTodo;
	}

	public Optional<Todo> deleteById(long id) {
		Optional<Todo> todo = todoRepository.findById(id);
        todoRepository.deleteById(id);
		return todo;
	}
}
```

***`backend\TodoFullStack\src\main\java\com\swarna\todoFullStack\todo\TodoController.java`***

```java
@CrossOrigin(origins = "${crossorigins.origin.url}")
@RestController
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping("/users/{username}/todos")
    public List<Todo> getAllTodos(@PathVariable String username) {
        return todoService.getAllTodos(username);
    }

    @GetMapping("/users/{username}/todos/{id}")
    public Optional<Todo> getTodo(@PathVariable String username, @PathVariable long id) {
        return todoService.getTodo(id);
    }

    @DeleteMapping("/users/{username}/todos/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable String username, @PathVariable long id) {
        Optional<Todo> todo = todoService.deleteById(id);
        if (todo != null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/users/{username}/todos/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable String username, @PathVariable long id, @RequestBody Todo todo) {
        Todo todoUpdated = todoService.save(todo, username);
        return new ResponseEntity<Todo>(todoUpdated, HttpStatus.OK); 
    }

    @PostMapping("/users/{username}/todos")
    public ResponseEntity<Void> createTodo(@PathVariable String username, @RequestBody Todo todo) {
        Todo createdTodo = todoService.save(todo, username);

        // Creating a new URI for the new Todo and returning URL by appending id
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdTodo.getId()).toUri();
        System.out.println(uri);
        return ResponseEntity.created(uri).build();
    }
}
```

***`backend\TodoFullStack\src\main\resources\data.sql`*** - To save some initial data in database.

```postgresql
INSERT INTO public.TODO_FULLSTACK (id, username, description, target_date, is_done) 
VALUES (nextval('public.todo_seq'), 'user', 'My first Todo', CURRENT_TIMESTAMP ,true);

INSERT INTO public.TODO_FULLSTACK (id, username, description, target_date, is_done) 
VALUES (nextval('public.todo_seq'), 'user', 'Spring Boot microservices', CURRENT_TIMESTAMP ,false);

INSERT INTO public.TODO_FULLSTACK (id, username, description, target_date, is_done) 
VALUES (nextval('public.todo_seq'), 'user', 'Angular', CURRENT_TIMESTAMP ,true);
```





## Deploying FullStack in Heroku

### Deploying Backend

We have to mention java version in Spring Boot pom.xml and heroku property file and both should match. 

**`backend\TodoFullStack\system.properties`**

```properties
java.runtime.version=11
```

***pom.xml***

```xml
<properties>
    <java.version>11</java.version>
</properties>
```



### Deploying Frontend with Environment

We should provide backend API url using ***enviroment.ts*** files for different enviromnents.

***environment.ts***

```typescript
export const environment = {
  production: false,
  baseUrl : 'http://localhost:8080' // baseUrl is the URL for backend
};
```

***environment.prod.ts***

```typescript
export const environment = {
  production: true,
  baseUrl: 'https://todo-fullstack-backend.herokuapp.com' // baseUrl is the URL for backend
};
```

Now in ***service.ts*** file, we have to mention like below

```typescript
export class TodoDataService {
  baseUrl = environment.baseUrl;
  ...
  
  getAllTodos(username: any) {
    return this.http.get<Todo[]>(`${this.baseUrl}/users/${username}/todos`);
  }
```



***package.json*** - output dist folder path should mention in start command

```json
"scripts": {
    ...
    "heroku-postbuild":"ng build --prod && npm install -g http-server-spa",
    "start": "http-server-spa dist/todo index.html $PORT",
	...
},
```





## Logging by SLF4j

***application.properties***

```properties
############### Logging ###############
logging.file.name=logs/application.log
logging.level.root=INFO,ERROR,DEBUG
# Log Level for springframework (info, error, debug etc)
#logging.level.org.springframework = error
# Log Level for Specific Controller
#logging.level.com.swarna.todoFullStack.todo = error
```

***TodoController.java***

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// defining LOGGER 
private static final Logger LOGGER = LoggerFactory.getLogger(TodoController.class);

// using Logger within method 
LOGGER.info("Fetching All todos for particular User " +username);
LOGGER.info("Fetching a todos for particular User " + id);
```

Output file : **`backend\TodoFullStack\logs\application.log`**

```log
...
2022-05-23 12:54:02.330  INFO 7632 --- [http-nio-8080-exec-7] c.s.todoFullStack.todo.TodoController    : Fetching All todos for particular User user
...
```

#### Rolling Log

Spring boot automatic roll its log files. Lets say `logging.file.name=logs/application.log` , then it will make a `.gz` file with the previous day's log and save it in same folder with name : `application.log.2022-05-22.0.gz`



## Caching by Hazlecast

Caching is a process of storing the objects that are converted from database records into a temporary memory location so that when the client reads the same exact data the orm tools need not go against the database.

To enable caching for our applications Spring boot uses third party cash providers like Hazelcast or EH cache or JBoss cache. Cache Hazelcast is a popular one and we'll use that.

#### Steps 
1. Add dependencies - SpringBoot and Hazlecast
  ```xml
  <dependency>
  	<groupId>org.springframework.boot</groupId>
  	<artifactId>spring-boot-starter-cache</artifactId>
  </dependency>
  <dependency>
  	<groupId>com.hazelcast</groupId>
  	<artifactId>hazelcast</artifactId>
  </dependency>
  <dependency>
  	<groupId>com.hazelcast</groupId>
  	<artifactId>hazelcast-spring</artifactId>
  </dependency>
  ```

2. Create Cache Configuration and we need to serialize model class also.

   **`com\swarna\todoFullStack\config\CacheConfig.java`**

   ```java
   package com.swarna.todoFullStack.config;
   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.Configuration;
   import com.hazelcast.config.Config;
   import com.hazelcast.config.MapConfig;
   
   @Configuration
   public class CacheConfig {
   	@Bean
   	public Config cacheConfig() {
   		return new Config()
               .setInstanceName("hazle-instance") // instance name for caching
               .addMapConfig(new MapConfig() // Adding one cache, we can configure as many cache as we want
                         .setName("todo-cache") // cache will be saved with this name
                         .setTimeToLiveSeconds(3000)); // cache will be evicted after 5 minutes
   	}
   }
   ```

   ***Todo.java*** - Serializing model class also.

   ```java
   import java.io.Serializable;
   ...
   public class Todo implements Serializable {
   	private static final long serialVersionUID = 1L;
   	...
   }
   ```

   

3. Enable and user caching - `@EnableCaching`, `@Cacheable("cache-name")` and `@Transactional(readOnly = true)` in Controller method.

   ***TodoFullStackApplication.java*** - Enabling cashing in `main()` method.

   ```java
   import org.springframework.cache.annotation.EnableCaching;
   ...
   @EnableCaching
   public class TodoFullStackApplication {
   	public static void main(String[] args) {
   		SpringApplication.run(TodoFullStackApplication.class, args);
   	}
   }
   ```

   ***TodoController.java*** - Adding Cacheable and Transactional

   ```java
   import org.springframework.cache.annotation.Cacheable;
   ...
       @GetMapping("/users/{username}/todos/{id}")
       @Transactional(readOnly = true) // True for select, for update/insert, false for default
       @Cacheable("todo-cache")
       public Optional<Todo> getTodo(@PathVariable String username, @PathVariable long id) {... }
   ...
   ```

   

4. Evict cache method - by what time the cache should deleted.
  - LRU(least recently used)
  - LFU(least frequently used)
  - NONE - application will crash after cache full
  - RANDOM	

​		***TodoController.java***

```java
import org.springframework.cache.annotation.CacheEvict;
...
    @DeleteMapping("/users/{username}/todos/{id}")
    @CacheEvict("todo-cache")
    public ResponseEntity<Void> deleteTodo(@PathVariable String username, @PathVariable long id) {...}
...
```







