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
| Url                    | Params        |  Response  | Description                         | Operation |
| --------------------   | ------------- | ---------  | ----------------------------------- |---------- |
| /api/accounts/signin   | SigninRequestDTO | SigninResponseDTO | Login an account           |POST|
| /api/accounts/refresh  | -        | SigninResponseDTO   | Refresh tokens of an account | GET|

### Ong API
| Url                    | Params        |  Response  | Description                         |  Operation |
| --------------------   | ------------- | ---------  | ----------------------------------- |---------- |
| /api/ongs/get | - | OngDTO | Get ong logged | GET|
| /api/ongs/signup   | OngDTO | OngDTO | Register an ong        |POST|
| /api/ongs/update  | OngDTO       | OngDTO |Update an ong |PUT|
| /api/ongs/delete  | -        | String   | Delete an ong |POST|

### Beneficiary API
| Url                    | Params        |  Response  | Description                         |  Operation |
| --------------------   | ------------- | ---------  | ----------------------------------- |---------- |
| /api/beneficiaries/{id}   | Long id | BeneficiaryDTO | Get beneficiary by id   |GET|
| /api/beneficiaries/total  | - | Integer |Return the total number of beneficiaries|GET|
| /api/beneficiaries/ong   | - | List<BeneficiaryDTO> | Get beneficiaries by ong logged in  |GET|
| /api/beneficiaries/signup  | BeneficiaryDTO       | BeneficiaryDTO | Register beneficiary by id |POST|
| /api/beneficiaries/update/{id}  | Long id, BeneficiaryDTO| BeneficiaryDTO | Update beneficiary by id |PUT|
| /api/beneficiaries/delete/{id}  | Long id        | String   | Delete a beneficiary |POST|

### Volunteer API
| Url                    | Params        |  Response  | Description                         |  Operation |
| --------------------   | ------------- | ---------  | ----------------------------------- |---------- |
| /api/volunteers/{id}   | Long id | VolunteerDTO | Get volunteer by id   |GET|
| /api/volunteers/ong  | - | List<VolunteerDTO>   | Get volunteer by ong logged in| GET|
| /api/volunteers/total  | - | Integer  | Return the total number of volunteers|GET|
| /api/volunteers/signup   | VolunteerDTO | VolunteerDTO | Register a volunteer        |POST|
| /api/volunteers/update/{id}  | Long id, VolunteerDTO | VolunteerDTO | Update volunteer by id |PUT|
| /api/volunteers/delete/{id}  | Long id      | String   | Delete a volunteer | POST|

### Grant API
| Url                    | Params        |  Response  | Description                         |  Operation |
| --------------------   | ------------- | ---------  | ----------------------------------- |---------- |
| /api/grants/save   | GrantDTO | GrantDTO | Save a grant        |POST|
| /api/grants/get/ong  | -        | List<GrantDTO>   | Get all grants of an ong logged in|GET|
| /api/grants/get/{id}/amount  | Long id| List<Double>   | Get total amount accepted grants by ong logged in|GET|
| /api/grants/get/{id}  | Long id| GrantDTO| Get a grant of ong logged in by id|GET|
| /api/grants/update/{id}  | Long id, GrantDTO        | GrantDTO   | Update a grant by id |PUT|
| /api/grants/delete/{id}  | Long id        | String   | Delete a grant by id|POST|

### Task API
| Url                    | Params        |  Response  | Description                         |  Operation |
| --------------------   | ------------- | ---------  | ----------------------------------- |---------- |
| /api/tasks/ong/get/{id}  | Long id        | List<TaskDTO>   | Get all task of an ong by id |GET|
| /api/tasks/get/{id} | Long id | TaskDTO | Get task by id|GET|
| /api/tasks/new | TaskDTO | TaskDTO | Save new task |POST|
| /api/tasks/update/{id} | Long id, TaskDTO | TaskDTO | Update task |PUT|
| /api/tasks/delete/{id} | Long id | String | Delete task |POST|
| /api/tasks/get/{id}/attendances | Long id | List<AttendancesDTO> | Return a list of attendances by task id |GET|
| /api/tasks/volunteers/get/{id}/attendances | Long id | List<AttendancesDTO> | Get all attendances of a volunteer by volunteer id |GET|
| /api/tasks/count | - | Integer | Get total number of tasks of ong logged in |GET|

### Attendance API
| Url                    | Params        |  Response  | Description                         |  Operation |
| --------------------   | ------------- | ---------  | ----------------------------------- |---------- |
| /api/attendances/{id} | Long id | AttendanceDTO | Get attendance by id |GET|
| /api/attendances/new/{idTask} | Long idTask | AttendanceDTO | Create a petition by Volunteer |POST|
| /api/attendances/cancel/{id} | Long id | AttendanceDTO | Cancel a petition by Volunteer |POST|
| /api/attendances/accept/{id} | Long id | AttendanceDTO | Accept a petition by ONG |PUT|
| /api/attendances/deny/{id} | Long id | AttendanceDTO | Deny a petition by ONG |PUT|
| /api/attendances/confirm/{id}/{type} | Long id, int type | AttendanceDTO | Confirm attendance by ONG. Type = [0 = "TOTAL", 1 = "PARCIAL", 2 = "NO_ASISTIDA"] |PUT|
| /api/attendances/add/{idTask}/{idPerson} | Long idTask, Long idPerson | AttendanceDTO | Add beneficiary from a Attendance by ONG |POST|
| /api/attendances/quit/{idTask}/{idPerson} | Long idTask, Long idPerson | String | Delete beneficiary from a Attendance by ONG |POST|

### Academic Experience API
| Url                    | Params        |  Response  | Description                         |  Operation |
| --------------------   | ------------- | ---------  | ----------------------------------- |---------- |
| /api/academicExperiences/get/{id}  | Long id        | AcademicExperienceDTO   | Get an academic experience by id |GET|
| /api/academicExperiences/get/volunteer/{id} | Long id | List<AcademicExperienceDTO> | Get all academic experience of a volunteer by id|GET|
| /api/academicExperiences/get/beneficiary/{id} | Long id | List<AcademicExperienceDTO> | Get all academic experience of a beneficiary by id|GET|
| /api/academicExperiences/save/{id} | Long id, AcademicExperienceDTO | AcademicExperienceDTO | Save new academic experience associated to a id person|POST|
| /api/academicExperiences/update/{id} | Long, AcademicExperienceDTO | AcademicExperienceDTO | Update an academic experience by id|PUT|
| /api/academicExperiences/delete/{id} | Long id | String | Delete an academic experience by id |POST|

### WorkExperience API
 | Url                    | Params        |  Response  | Description                         |  Operation |
| --------------------   | ------------- | ---------  | ----------------------------------- |---------- |
 | /api/workExperiences/get/{id} | Long id | WorkExperienceDTO | Get work experience by id |GET|
 | /api/workExperiences/get/volunteer/{id} | Long id | List<WorkExperienceDTO> | Get all work experiences of a volunteer by volunteer id|GET|
 | /api/workExperiences/get/beneficiary/{id} | Long id | List<WorkExperienceDTO> | Get all work experiences of a beneficiary by beneficary id|GET|
 | /api/workExperiences/save/{id} | Long id, WorkExperienceDTO | WorkExperienceDTO | Save new work experience associated to a id person |POST|
 | /api/workExperiences/update/{id} | Long id, WorkExperienceDTO | WorkExperienceDTO | Update a work experience by id |PUT|
 | /api/workExperiences/delete/{id} | Long id | String | Delete a work experience by id |POST|

### Appointment API
 | Url                    | Params        |  Response  | Description                         |  Operation |
| --------------------   | ------------- | ---------  | ----------------------------------- |---------- |
 | /api/appointments/get/{id} | Long id | AppointmentDTO | Get an appointment by id |GET|
 | /api/appointments/get/ong/{id} | Long id | List<AppointmentDTO> | Get all appointments by ong id |GET|
 | /api/appointments/get/beneficary/{id} | Long id | List<AppointmentDTO> | Get all appointments by beneficary id |GET|
 | /api/appointments/save/{id} | Long id, AppointmentDTO| AppointmentDTO | Save new appointment associated to a beneficiary id |POST|
 | /api/appointments/update/{id} | Long id, AppointmentDTO | AppointmentDTO | Update an appointment by id |PUT|
 | /api/appointments/delete/{id} | Long id | String | Delete an appointment by id|POST|

### ComplementaryFormation API
 | Url                    | Params        |  Response  | Description                         |  Operation |
| --------------------   | ------------- | ---------  | ----------------------------------- |---------- |
 | /api/complementaryFunctions/get/{id} | Long id | ComplementaryFormationDTO | Get complementary formation by id |GET|
 | /api/complementaryFunctions/get/volunteer/{id} | Long id | List<ComplementaryFormationDTO> | Get all complementary formations of a volunteer by the id|GET|
 | /api/complementaryFunctions/get/beneficiary/{id} | Long id | List<ComplementaryFormationDTO> | Get all complementary formations of a beneficiary by the id|GET|
 | /api/complementaryFunctions/save/{id} | Long id, ComplementaryFormationDTO | ComplementaryFormationDTO | Save new complementary formation associated to a person id|POST|
 | /api/complementaryFunctions/update/{id}| Long id, ComplementaryFormationDTO | ComplementaryFormationDTO | Update a complementary formation by id |PUT|
 | /api/complementaryFunctions/delete/{id} | Long id | String | Delete a complementary formation by id|POST|

	
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
