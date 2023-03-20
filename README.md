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

### Beneficiary API
| Url                    | Params        |  Response  | Description                         | 
| --------------------   | ------------- | ---------  | -----------------------------------
| /api/beneficiaries | -        | List<BeneficiaryDTO>   | Get all beneficiaries |
| /api/beneficiaries/{id}   | - | BeneficiaryDTO | Get beneficiary by id   |
| /api/beneficiaries/signup   | BeneficiaryDTO | BeneficiaryDTO | Register a beneficiary        |
| /api/beneficiaries/update/{id}  | BeneficiaryDTO       | BeneficiaryDTO | Update beneficiary by id |
| /api/beneficiaries/delete/{id}  | -        | String   | Delete a beneficiary |
| /api/beneficiaries/ong/{username}  | -        | List<BeneficiaryDTO>   | Get beneficiaries by ong |

### Volunteer API
| Url                    | Params        |  Response  | Description                         | 
| --------------------   | ------------- | ---------  | -----------------------------------
| /api/volunteers | -        | List<VolunteerDTO>   | Get all volunteers |
| /api/volunteers/{id}   | - | VolunteerDTO | Get volunteer by id   |
| /api/volunteers/signup   | VolunteerDTO | VolunteerDTO | Register a volunteer        |
| /api/volunteers/update/{id}  | VolunteerDTO       | VolunteerDTO | Update volunteer by id |
| /api/volunteers/delete/{id}  | -        | String   | Delete a volunteer |
| /api/volunteers/ong/{username}  | -        | List<VolunteerDTO>   | Get volunteer by ong |

### Grant API
| Url                    | Params        |  Response  | Description                         | 
| --------------------   | ------------- | ---------  | -----------------------------------
| /api/grants/save   | GrantDTO | GrantDTO | save a grant        |
| /api/grants/get/ong  | -        | List<GrantDTO>   | Get all grants of an ong |
| /api/grants/get/{id}  | -        | List<GrantDTO>   | Get grant by id |
| /api/grants/update  | GrantDTO        | GrantDTO   | Update a grant |
| /api/grants/delete/{id}  | -        | String   | Delete a grant |

### Task API
| Url                    | Params        |  Response  | Description                         | 
| --------------------   | ------------- | ---------  | -----------------------------------
| /api/tasks   |  | List<TaskDTO>| Get all task        |
| /api/tasks/ong/{username}  | -        | List<TaskDTO>   | Get all task of an ong |
| /api/tasks/{id} | - | TaskDTO | Get task by id|
| /api/tasks/new | TaskDTO | TaskDTO | Save new task |
| /api/tasks/update/{id} | TaskDTO | TaskDTO | Update task |
| /api/tasks/delete/{id} | - | String | Delete task |

### Attendance API
| Url                    | Params        |  Response  | Description                         | 
| --------------------   | ------------- | ---------  | -----------------------------------
| /api/attendances/{id} | - | AttendanceDTO | Get attendance by id |
| /api/attendances/new/{idTask} | - | AttendanceDTO | Create a petition by Volunteer |
| /api/attendances/cancel/{id} | - | String | cancel a petition by Volunteer |
| /api/attendances/accept/{id} | - | AttendanceDTO | Accept a petition by ONG |
| /api/attendances/deny/{id} | - | AttendanceDTO | Deny a petition by ONG |
| /api/attendances/confirm/{id}/{type} | - | AttendanceDTO | Confirm attendance by ONG. Type = [0 = "TOTAL", 1 = "PARCIAL", 2 = "NO_ASISTIDA"] |
| /api/attendances/add/{idTask}/{idPerson} | - | AttendanceDTO | Add beneficiary to a Attendance by ONG |
| /api/attendances/quit/{idTask}/{idPerson} | - | AttendanceDTO | Delete beneficiary to a Attendance by ONG |

### Academic Experience API
| Url                    | Params        |  Response  | Description                         | 
| --------------------   | ------------- | ---------  | -----------------------------------
| /api/academicExps   |  | List<AcademicExperienceDTO>| Get all academic experiences        |
| /api/academicExps/get/{id}  | -        | AcademicExperienceDTO   | Get an academic experience by id |
| /api/academicExps/volunteer/{id} | - | List<AcademicExperienceDTO> | Get all academic experience of a volunteer by id|
| /api/academicExps/beneficiary/{id} | - | List<AcademicExperienceDTO> | Get all academic experience of a beneficiary by id|
| /api/academicExps/save | AcademicExperienceDTO | AcademicExperienceDTO | Save new academic experience |
| /api/academicExps/update/{id} | AcademicExperienceDTO | AcademicExperienceDTO | Update an academic experience |
| /api/academicExps/delete/{id} | - | String | Delete an academic experience |

### WorkExperience API
 | Url                    | Params        |  Response  | Description                         | 
 | --------------------   | ------------- | ---------  | -----------------------------------
 | /api/workExperience | - | List<WorkExperience> | Get all work experiences |
 | /api/workExperience/get/{id} | - | WorkExperienceDTO | Get work experience by id |
 | /api/workExperience/get/volunteer/{username} | - | List<WorkExperienceDTO> | Get all work experiences of a volunteer by the username|
 | /api/workExperience/get/beneficiary/{username} | - | List<WorkExperienceDTO> | Get all work experiences of a beneficiary by the username|
 | /api/workExperience/save/{username} | - | WorkExperienceDTO | Save new work experience |
 | /api/workExperience/update | - | WorkExperienceDTO | Update a work experience |
 | /api/workExperience/delete/{id} | - | String | Delete a work experience |

### Appointment API
 | Url                    | Params        |  Response  | Description                         | 
 | --------------------   | ------------- | ---------  | -----------------------------------
 | /api/appointments | - | List<AppointmentDTO> | Get all appointments |
 | /api/appointments/get/{id} | - | AppointmentDTO | Get an appointment by id |
 | /api/appointments/get/ong/{username} | - | List<AppointmentDTO> | Get all appointments of an ong by the username|
 | /api/appointments/save/{username} | - | AppointmentDTO | Save new appointment |
 | /api/appointments/update | - | AppointmentDTO | Update an appointment |
 | /api/appointments/delete/{id} | - | String | Delete an appointment |

### ComplementaryFormation API
 | Url                    | Params        |  Response  | Description                         | 
 | --------------------   | ------------- | ---------  | -----------------------------------
 | /api/complementaryFormation | - | List<ComplementaryFormation> | Get all complementary formations |
 | /api/complementaryFormation/get/{id} | - | ComplementaryFormationDTO | Get complementary formation by id |
 | /api/complementaryFormation/get/volunteer/{username} | - | List<ComplementaryFormationDTO> | Get all complementary formations of a volunteer by the username|
 | /api/complementaryFormation/get/beneficiary/{username} | - | List<ComplementaryFormationDTO> | Get all complementary formations of a beneficiary by the username|
 | /api/complementaryFormation/save/{username} | - | ComplementaryFormationDTO | Save new complementary formation |
 | /api/complementaryFormation/update | - | ComplementaryFormationDTO | Update a complementary formation |
 | /api/complementaryFormation/delete/{id} | - | String | Delete a complementary formation |

	
## DTOs
### SigninRequestDTO
| PROPERTY               | Type        |  Restrictions | Mandatory |
| --------------------   | ------------- | ------------ | -------- |
| username               |  String | Length Max = 20 | Yes |
| password               | String | Length Max = 20 | Yes |

### SigninResponseDTO
| PROPERTY               | Type        |  Restrictions | Mandatory |
| --------------------   | ------------- | ----------- |----------|
| token               |  String | --- | No |
| refresh               | String | --- | No |

### OngDTO
| PROPERTY               | Type        |  Restrictions | Mandatory |
| --------------------   | ------------- | ------------|-----------| 
| id               |  Long |----|No (you can specify it, but if you don't write it is assigned by default)|
| name               | String |Length Max = 50|Yes|
| cif               | String |----|Yes|
| description      | String |Length Max = 250|Yes|
| username               | String |Length Max = 20|Yes|
| email               | String |Length Max = 200 |Yes|
| password               | String |Length Max = 120|Yes|
| rolAccount               | RolAccount(ONG, VOLUNTEER, BENEFICIARY)|------|No (is assigned by default)|

	
### BeneficiaryDTO
| PROPERTY               | Type        |  Restrictions | Mandatory |
| --------------------   | ------------- | ------------|-----------| 
| id               |  Long |----|No (you can specify it, but if you don't write it is assigned by default)|
| nationality               | String | Length Max = 50 | Yes |
| doublenationality               | Boolean |------|Yes|
| arrivedDate      | LocalDate |-------|No|
| europeanCitizenAuthorization               | Boolean |------|No|
| touristVisa               | Boolean |--------|No|
| date_touristVisa               | LocalDate |--------|No|
| healthCard               | Boolean |	---------|No|
| employmentSector               | String | Length Max = 150 | Yes|
| perceptionAid               | String | Length Max = 100|Yes|
| savings_possesion               | Boolean |------|No|
| saeInscription               | Boolean |-------|No|
| working               | Boolean |-------|No|
| computerKnowledge               | Boolean |-------|No|	
| ownedDevices               | String |Length Max = 200|Yes|
| languages               | String |Length Max = 100|Yes|
| entryDate               | LocalDate |---------|Yes|
| firstSurname               | String |Length Max = 20|Yes|
| secondSurname               | String |Length Max = 20|Yes|
| name               | String |Length Max = 20|Yes|
| documentType               | DocumentType(DNI, NIE, PASSPORT) |------|Yes|
| documentNumber               | String |Length Max = 9 && Length Min = 9|Yes|
| gender               | Gender(MALE, FEMALE)|---------|Yes|
| birthday               | LocalDate |-------|Yes|
| civilStatus               | CivilStatus(SINGLE, MARRIED, WIDOWED, DIVORCED) |------|Yes|
| numberOfChildren               | int |Min(0)|Yes|
| address               | String |Length Max = 50|Yes|
| postalCode               | String |Length Max = 100|Yes|
| registrationAddress               | String |Length Max = 50|No|
| town               | String |Length Max = 20|Yes|
| telephone          | String |Length max = 15|Yes|
| leavingDate          | LocalDate |------|No|
| driveLicenses          | String |Length max = 100|Yes|
| otherSkills          | String |Length max = 350|Yes|
| username               | String |Length Max = 20|Yes|
| email               | String |Length Max = 200 |Yes|
| password               | String |Length Max = 120|Yes|
| rolAccount               | RolAccount(ONG, VOLUNTEER, BENEFICIARY)|------|No (is assigned by default)|

### VolunteerDTO
| PROPERTY               | Type          |  Restrictions | Mandatory |
| --------------------   | ------------- | ------------|-----------| 
| id               |  Long |-----|No (you can specify it, but if you don't write it is assigned by default)|
| hourOfAvailability       |  String |Length max = 50|Yes|
| sexCrimes               | Boolean |-----|Yes|
| entryDate               | LocalDate |---------|Yes|
| firstSurname               | String |Length Max = 20|Yes|
| secondSurname               | String |Length Max = 20|Yes|
| name               | String |Length Max = 20|Yes|
| documentType               | DocumentType(DNI, NIE, PASSPORT) |------|Yes|
| documentNumber               | String |Length Max = 9 && Length Min = 9|Yes|
| gender               | Gender(MALE, FEMALE)|---------|Yes|
| birthday               | LocalDate |-------|Yes|
| civilStatus               | CivilStatus(SINGLE, MARRIED, WIDOWED, DIVORCED) |------|Yes|
| numberOfChildren               | int |Min(0)|Yes|
| address               | String |Length Max = 50|Yes|
| postalCode               | String |Length Max = 100|Yes|
| registrationAddress               | String |Length Max = 50|No|
| town               | String |Length Max = 20|Yes|
| telephone          | String |Length max = 15|Yes|
| leavingDate          | LocalDate |------|No|
| driveLicenses          | String |Length max = 100|Yes|
| otherSkills          | String |Length max = 350|Yes|
| username               | String |Length Max = 20|Yes|
| email               | String |Length Max = 200 |Yes|
| password               | String |Length Max = 120|Yes|
| rolAccount               | RolAccount(ONG, VOLUNTEER, BENEFICIARY)|------|No (is assigned by default)|

### GrantDTO
| PROPERTY               | Type        |  Restrictions | Mandatory |
| --------------------   | ------------- | ------------|-----------| 
| id               |  Long |-----|No (you can specify it, but if you don't write it is assigned by default)|
| privateGrant               | Boolean |-------|Yes|
| gubernamental               | Boolean |-------|Yes|
| state               | GrantState(REQUESTED, ACCEPTED, DENIED, REFORMULATION) |------|Yes|
| justification               | String |Length max = 1000|Yes|
| amount               | Double |Min(0)|Yes|
	
### TaskDTO
| PROPERTY               | Type        |  Restrictions | Mandatory |
| --------------------   | ------------- | ------------|-----------| 
| id | Long |-----|No (you can specify it, but if you don't write it is assigned by default)|
| name | String |Length max = 32|Yes|
| type | TaskType(CURSO, ACTIVIDAD, TALLER )|-----|Yes|
| date | LocalDateTime (format: "yyyy-MM-dd HH:mm:ss")|-----|Yes
| teacher | String |-----|Yes|
| certificate | Boolean |-----|Yes|
| observations | String |Length Max = 256|No|
| incidences | String |----|No|
| coordinator | String |----|Yes|
| numParticipants|Integer|-----|No|
| place | String |------|Yes|

### AttendanceDTO
| PROPERTY               | Type        |  Restrictions | Mandatory |
| --------------------   | ------------- | ------------|-----------| 
| id | Long |-----|No (you can specify it, but if you don't write it is assigned by default)|
| type | AttendanceType(TOTAL, PARCIAL, NO_ASISTIDA) |----|No|
| state | PetitionState(ESPERA, ACEPTADA, DENEGADA, CANCELADA)|----|Yes|
	
### AcademicExperienceDTO
| PROPERTY               | Type        |  Restrictions | Mandatory |
| --------------------   | ------------- | ------------|-----------|
| id | Long |-----|No (you can specify it, but if you don't write it is assigned by default)|
| speciality | String |Length Max = 50|Yes|
| endingYear | Integer|Min(2023)|Yes|
| satisfactionDegree | Integer|Min(1) && Max(5)|Yes|
| educationalLevel | String |Length Max = 1000|Yes|

### WorkExperienceDTO
 | PROPERTY               | Type        |  Restrictions | Mandatory |
| --------------------   | ------------- | ------------|-----------|
 | id | Long |-----|No (you can specify it, but if you don't write it is assigned by default)|
 | job | String |Length Max = 255 |Yes|
 | time | String |Length Max = 100|Yes|
 | place | String |Length Max = 255|Yes|
 | reasonToFinish | String |Length Max = 1000|Yes|
	
### ComplementaryFormationDTO
 | PROPERTY               | Type        |  Restrictions | Mandatory |
 | --------------------   | ------------- | ------------|-----------|
 | id | Long |-----|No (you can specify it, but if you don't write it is assigned by default)|
 | name | String |----|Yes|
 | organization | String |----|Yes|
 | date | LocalDate|----|No|
 | place | String |----|Yes|

### AppointmentDTO
 | PROPERTY               | Type        |  Restrictions | Mandatory |
 | --------------------   | ------------- | ------------|-----------|
 | id | Long |-----|No (you can specify it, but if you don't write it is assigned by default)|
 | date| LocalDate|----|Yes|
 | hour | String |Length Max = 25|Yes|
 | notes | String |Length Max = 255|Yes|
 
### AccountDTO
| PROPERTY               | Type        |  Restrictions | Mandatory |
| --------------------   | ------------- | ------------|-----------|
| username               | String |Length Max = 20|Yes|
| email               | String |Length Max = 200 |Yes|
| password               | String |Length Max = 120|Yes|
| rolAccount               | RolAccount(ONG, VOLUNTEER, BENEFICIARY)|------|No (is assigned by default)|

### PersonDTO
| PROPERTY               | Type        |  Restrictions | Mandatory |
| --------------------   | ------------- | ------------|-----------|
| entryDate               | LocalDate |---------|Yes|
| firstSurname               | String |Length Max = 20|Yes|
| secondSurname               | String |Length Max = 20|Yes|
| name               | String |Length Max = 20|Yes|
| documentType               | DocumentType(DNI, NIE, PASSPORT) |------|Yes|
| documentNumber               | String |Length Max = 9 && Length Min = 9|Yes|
| gender               | Gender(MALE, FEMALE)|---------|Yes|
| birthday               | LocalDate |-------|Yes|
| civilStatus               | CivilStatus(SINGLE, MARRIED, WIDOWED, DIVORCED) |------|Yes|
| numberOfChildren               | int |Min(0)|Yes|
| address               | String |Length Max = 50|Yes|
| postalCode               | String |Length Max = 100|Yes|
| registrationAddress               | String |Length Max = 50|No|
| town               | String |Length Max = 20|Yes|
| telephone          | String |Length max = 15|Yes|
| leavingDate          | LocalDate |------|No|
| driveLicenses          | String |Length max = 100|Yes|
| otherSkills          | String |Length max = 350|Yes|
| username               | String |Length Max = 20|Yes|
| email               | String |Length Max = 200 |Yes|
| password               | String |Length Max = 120|Yes|
| rolAccount               | RolAccount(ONG, VOLUNTEER, BENEFICIARY)|------|No (is assigned by default)|
