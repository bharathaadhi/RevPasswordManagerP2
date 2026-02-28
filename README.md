# Rev Password Manager

**Rev Password Manager** is a secure full-stack password management application designed to safely store, manage, and protect user credentials using strong encryption and multi-layer authentication.

The system enables users to securely manage digital accounts while ensuring confidentiality through encrypted storage, verification mechanisms, and secure backup functionality.

---

## **Project Overview**

Rev Password Manager helps users to:

- Store account credentials securely  
- Encrypt passwords before database storage  
- Access passwords only after verification  
- Backup and restore vault data safely  
- Monitor password security health  

The application follows enterprise-level security practices including authentication, encryption, verification codes, and protected vault operations.

---

## **Key Features**

### **Authentication & Security**
- Secure User Registration and Login  
- JWT-based Authentication  
- Master Password Protection  
- Two-Factor Authentication (2FA)  
- OTP Verification System  
- Session-based Authorization  

### **Password Vault Management**
- Add new passwords securely  
- Update stored credentials  
- Delete passwords safely  
- Mark important accounts as favorites  
- Category-based filtering  
- Search and sorting functionality  
- Password visibility only after verification  

### **Import & Export Backup**
- Secure Vault Export (JSON backup)  
- Encrypted password export  
- Vault Import functionality  
- Verification-code protected backup  
- Secure data restoration  

### **Security Audit**
- Weak Password Detection  
- Reused Password Identification  
- Security Score Dashboard  
- Old Password Tracking  

### **User Profile Management**
- Update profile details  
- Change master password  
- Enable or Disable Two-Factor Authentication  
- Security question validation  

---

## **Tech Stack**

### **Frontend (Angular)**

| Technology | Usage |
|------------|------|
| Angular 21 | UI Framework |
| Standalone Components | Modular Architecture |
| Angular Router | Navigation |
| Angular Material | UI Components |
| HttpClient | API Communication |
| JWT Interceptor | Secure Requests |
| Custom CSS | Dark Theme UI |

---

### **Backend (Spring Boot)**

| Technology | Usage |
|------------|------|
| Java 17 | Programming Language |
| Spring Boot 3 | Backend Framework |
| Spring Security | Authentication |
| JWT | Authorization |
| Spring Data JPA | Database ORM |
| Hibernate | Persistence |
| MySQL | Database |
| BCrypt | Password Hashing |
| AES Encryption | Vault Security |

---

## **Architecture**

Backend architecture follows:

Controller → Service → Repository → Database

Implemented using:

- DTO Pattern  
- Layered Architecture  
- Secure API Communication  
- Encryption Utilities  

---

## **Project Structure**

### **Backend Structure**
src/main/java/com/rev/revpasswordmanagerp2
│
├── controller      # REST APIs
├── service         # Business logic
├── repository      # Database operations
├── model           # Entity classes
├── dto             # Request/Response objects
├── security        # JWT & authentication
├── config          # Application configs
└── util            # Encryption utilities


### **Frontend Structure**
src/app
│
├── core
│   ├── services
│   ├── guards
│   └── interceptors
│
├── features
│   ├── auth
│   ├── dashboard
│   ├── vault
│   ├── backup
│   ├── security
│   └── profile
│
└── shared

## **Setup and Installation**

### **Prerequisites**
- Node.js (v18+)
- Angular CLI
- Java JDK 17+
- Maven
- MySQL Server
- IntelliJ IDEA or VS Code

## **Backend Setup**

Navigate to backend directory:

cd RevPasswordManagerP2

## **Configure Database**

application.properties

spring.datasource.url=jdbc:mysql://localhost:3306/rev_password_manager_p2
spring.datasource.username=root
spring.datasource.password=

application-local.properties

spring.datasource.password=YOUR_DB_PASSWORD
app.encryption.secret=YourSecretKey

Run Backend

## **Using Maven:**

mvn spring-boot:run

OR from IntelliJ Run Button.

## **Backend runs at:**

http://localhost:8080

## **Frontend Setup**

## **Navigate to UI folder**

cd rev-password-manager-ui

## **Install Dependencies**

npm install

## **Start Application**

ng serve

## **Open browser:**

http://localhost:4200

## **Important API Endpoints**

Method	    Endpoint	                      Description
POST	  /api/auth/login	                User Login
POST	  /api/auth/register	            Register User
GET	    /api/vault	                    Get Vault Data
POST	  /api/vault	                    Add Password
PUT	    /api/vault/{id}	                Update Password
POST	  /api/vault/export	              Export Vault
POST	  /api/vault/import	              Import Vault
POST	  /api/security/generate-code	    Generate OTP
POST	  /api/auth/changePassword	      Change Master Password

## **Security Implementation**

Password Encryption before DB storage
Master password verification
OTP validation before sensitive operations
JWT protected APIs
Secure vault export/import
Role & session validation

## **Testing Performed**

Authentication Flow
Vault CRUD Operations
Export & Import Backup
OTP Verification
Security Audit Checks
Profile Management

## **Future Enhancements**

Cloud Backup Integration
Browser Extension Support
Password Auto-fill
Biometric Authentication
Mobile Application
