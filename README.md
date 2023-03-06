# HeartForChange backend app
HeartForChange is a Spring Boot backend project for a web application aimed at NGOs. The application provides a platform for NGOs to manage volunteers, grants, and other activities in their community to facilitate positive change.

The project is built using Java 11 and the Spring Boot framework, with a h2 embedded database for data storage. The backend provides a RESTful API that allows users to create and manage their accounts, create and manage events and campaigns, etc.

The API is secured using Spring Security with JSON Web Tokens (JWT), and all data is validated using Hibernate Validator to ensure data integrity and security.

The project uses best practices such as dependency injection, modular architecture, and continuous integration and delivery (CI/CD) to ensure scalability and maintainability. It also includes unit and integration tests to ensure code quality and reliability.

Overall, HeartForChange is a robust and secure backend project that provides a solid foundation for a web application aimed at connecting NGOs with their community to drive positive change.

# Documentation project
## Dependencies
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-devtools</artifactId>
	<scope>runtime</scope>
	<optional>true</optional>
</dependency>
<dependency>
	<groupId>com.h2database</groupId>
	<artifactId>h2</artifactId>
	<scope>runtime</scope>
</dependency>
<dependency>
	<groupId>org.projectlombok</groupId>
	<artifactId>lombok</artifactId>
	<optional>true</optional>
</dependency>
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-test</artifactId>
	<scope>test</scope>
</dependency>
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
	<groupId>org.hibernate.javax.persistence</groupId>
	<artifactId>hibernate-jpa-2.0-api</artifactId>
	<version>1.0.1.Final</version>
</dependency>
<dependency>
  <groupId>io.jsonwebtoken</groupId>
	<artifactId>jjwt</artifactId>
	<version>0.9.1</version>
</dependency>
<dependency> 
	<groupId>org.springframework.boot</groupId> 
	<artifactId>spring-boot-starter-validation</artifactId> 
</dependency>
```
## APIS
### Account API
| Url                    | Params        |  Response  | Description                         | 
| --------------------   | ------------- | ---------  | -----------------------------------
| /api/accounts/signin   | SigninRequestDTO | SigninResponseDTO | Login an account           |
| /api/accounts/refresh  | -        | SigninResponseDTO   | Refresh tokens of an account |

### Ong API
| Url                    | Params        |  Response  | Description                         | 
| --------------------   | ------------- | ---------  | -----------------------------------
| /api/ongs | -        | List<OngDTO>   | Get all ongs |
| /api/ongs/{id}   | - | OngDTO | Get ong by id   |
| /api/ongs/signup   | OngDTO | OngDTO | Register an ong        |
| /api/ongs/update/{id}  | OngDTO       | OngDTO | Get grant by id |
| /api/ongs/delete/{id}  | -        | String   | Delete an ong |

### Grant API
| Url                    | Params        |  Response  | Description                         | 
| --------------------   | ------------- | ---------  | -----------------------------------
| /api/grants/save   | GrantDTO | GrantDTO | save a grant        |
| /api/grants/get/ong  | -        | List<GrantDTO>   | Get all grants of an ong |
| /api/grants/get/{id}  | -        | List<GrantDTO>   | Get grant by id |
| /api/grants/update  | GrantDTO        | GrantDTO   | Update a grant |
| /api/grants/delete/{id}  | -        | String   | Delete a grant |

## DTOs
### SigninRequestDTO
| PROPERTY               | Type        |  
| --------------------   | ------------- | 
| username               |  String |
| password               | String |

### SigninResponseDTO
| PROPERTY               | Type        |  
| --------------------   | ------------- | 
| token               |  String |
| refresh               | String |

### OngDTO
| PROPERTY               | Type        |  
| --------------------   | ------------- | 
| id               |  Long |
| name               | String |
| cif               | String |
| description      | String |
| username               | String |
| email               | String |
| password               | String |
| rolAccount               | RolAccount(ONG, VOLUNTEER, BENEFICIARY) |
	
### GrantDTO
| PROPERTY               | Type        |  
| --------------------   | ------------- | 
| id               |  Long |
| privateGrant               | Boolean |
| gubernamental               | Boolean |
| state               | GrantState(REQUESTED, ACCEPTED, DENIED, REFORMULATION) |
| justification               | String |
| amount               | Integer |
	


