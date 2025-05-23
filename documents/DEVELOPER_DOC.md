# Developer Documentation

## Project Overview
This is a booking application built using Java and JavaFX, utilizing a modern tech stack for robust functionality and user experience. The application follows a layered architecture pattern with clear separation between frontend and backend components.

## Architecture

### System Architecture
The application follows a layered architecture with the following main components:

1. **Presentation Layer (Frontend)**
    - Built with JavaFX (version 23.0.2)
    - FXML-based UI layouts
    - MVC (Model-View-Controller) pattern implementation

- **Location**: `src/main/java/com/atdit/booking/frontend/`
- **Primary Responsibilities**: User interface, input handling, view management
- **Key Classes**:
  ```
  ├── customer_registration/
  │   └── controllers/
  │       ├── Page1PaymentProcessStartController
  │       ├── Page2DataPrivacyController
  │       ├── Page3PersonalInformationController
  │       ├── Page4DeclarationFIController
  │       ├── Page5ProofFIController
  │       ├── Page6CreateAccountController
  │       └── Page7AccountConfirmationController
  ├── payment_process/
  │   ├── abstract_controllers/
  │   └── controllers/
  │       ├── Page1ControllerPageLogin
  │       ├── Page2aSelectPaymentController
  │       ├── Page2bSelectPaymentPlanController
  │       ├── Page3aCreditCardController
  │       ├── Page3bBankTransferController
  │       ├── Page4aOneTimePaymentContractController
  │       ├── Page4bBuyNowPayLaterContractPage
  │       ├── Page4cFinancingContractController
  │       └── Page5ConfirmationController
  └── abstract_controller/
      (*Contains abstract controller and interfaces like Navigatable, Cacheable*)
  ```

2. **Application Layer**
    - Manages application lifecycle and startup
    - Coordinates between different processes

- **Location**: `src/main/java/com/atdit/booking/applications/`
- **Primary Responsibilities**: Application entry points, process initialization.
- **Key Classes**:
  ```
  ├── AbstractApplication (abstract)
  ├── CustomerRegistration
  └── PaymentProcess
  ```
Hinweis: Die eigentlichen Java-Anwendungsstarter (mit `main`-Methoden, die direkt ausgeführt werden können oder von der `pom.xml` für `javafx:run` referenziert werden) sind `CustomerRegistrationApplicationStarter.java` und `PaymentProcessApplicationStarter.java` im Paket `com.atdit.booking` (eine Ebene über dem `applications`-Paket). Diese rufen die entsprechenden `main`-Methoden der Klassen `CustomerRegistration` und `PaymentProcess` im `com.atdit.booking.applications`-Paket auf. Die in der `pom.xml` konfigurierte `mainClass` für den Befehl `mvn clean javafx:run` ist `com.atdit.booking/com.atdit.booking.CustomerRegistrationApplicationStarter`.

3. **Business Logic Layer**
    - Core business logic implementation
    - Process handling (Payment, Customer Registration)
    - Abstract application framework

- **Location**: `src/main/java/com/atdit/booking/backend/`
- **Primary Responsibilities**: Business rules, data validation, process management
- **Key Classes**:
  ```
  ├── customer/
  │   ├── Customer
  │   └── CustomerEvaluator
  ├── financialdata/
  │   ├── financial_information/
  │   │   ├── BankTransferDetails
  │   │   ├── Credit
  │   │   ├── CreditCardDetails
  │   │   ├── FinancialInformation
  │   │   ├── IncomeProof
  │   │   ├── LiquidAsset
  │   │   ├── Schufaauskunft
  │   │   └── SchufaOverview
  │   ├── processing/
  │   │   ├── FinancialDocumentsGenerator
  │   │   ├── FinancialInformationEvaluator
  │   │   ├── FinancialInformationParser
  │   │   └── PaymentMethodEvaluator
  │   └── contracts/
  (*Contains contract related classes*)
  ```

4. **Data Access Layer**
    - Hibernate ORM integration
    - SQLite database connectivity
    - Entity management and persistence

- **Location**: `src/main/java/com/atdit/booking/backend/database/`
- **Primary Responsibilities**: Database operations, data encryption, persistence
- **Key Classes**:
  ```
  ├── database/
  │   ├── DatabaseService
  │   └── Encrypter
  ├── exceptions/
  │   ├── CryptographyException
  │   ├── DecryptionException
  │   ├── EncryptionException
  │   ├── EvaluationException
  │   ├── HashingException
  │   └── ValidationException
  ```

```mermaid
graph TD
    A[Frontend Layer] --> B[Business Logic Layer]
    B --> C[Data Access Layer]
    C --> D[(SQLite Database)]
    
    subgraph Frontend
    A --> E[JavaFX UI]
    A --> F[FXML Controllers]
    end
    
    subgraph Business Logic
    B --> G[Process Handlers]
    B --> H[Service Layer]
    end
    
    subgraph Data Access
    C --> I[Hibernate ORM]
    C --> J[Entity Models]
    end
```

## Technology Stack

### Core Technologies
1. **Java**
    - Version: Java 23 
    - Build System: Maven
    - Compiler Configuration: Targets Java 23. 

2. **Frontend Framework**
    - JavaFX 23.0.2
    - FXML for UI layouts

3. **Database**
    - SQLite 3.49.1.0 (via `sqlite-jdbc`)
    - Hibernate ORM 6.6.13.Final for data persistence

4. **Testing Framework**
    - JUnit Jupiter 5.10.2
    - Mockito 5.11.0 for mocking

### Dependencies
```xml
Core Dependencies:
- javafx-controls:23.0.2
- javafx-fxml:23.0.2
- hibernate-core:6.6.13.Final
- sqlite-jdbc:3.49.1.0

Testing Dependencies:
- junit-jupiter-api:5.10.2
- junit-jupiter-engine:5.10.2
- mockito-core:5.11.0
- mockito-junit-jupiter:5.11.0
```

## Detailed Class Structure

### Core Domain Model

```mermaid
classDiagram
    class Customer {
        -String title
        -String firstName
        -String lastName
        -String country
        -String birthdate
        -String address
        -String email
        -FinancialInformation financialInformation
        +setTitle(String)
        +setFirstName(String)
        +setName(String)
        +setCountry(String)
        +setBirthdate(String)
        +setAddress(String)
        +setEmail(String)
        +setFinancialInformation(FinancialInformation)
        +getters()
    }

    class FinancialInformation {
        -int avgNetIncome
        -int liquidAssets
        -int monthlyFixCost
        -int minCostOfLiving
        -int monthlyAvailableMoney
        -IncomeProof proofOfIncome
        -LiquidAsset proofOfLiquidAssets
        -SchufaOverview schufa
        +setters()
        +getters()
        -updateMonthlyAvailableMoney()
    }

    class IncomeProof {
        <<record>>
        +int monthlyNetIncome
        +String employer
        +String employmentType
        +int employmentDurationMonths
        +String dateIssued
        +IncomeProof(int, String, String, int, String)
    }

    class SchufaOverview {
        -String firstName
        -String lastName
        -double score
        -int totalCredits
        -int totalCreditSum
        -int totalAmountPayed
        -int totalAmountOwed
        -int totalMonthlyRate
        -String dateIssued
        +setters()
        +getters()
    }

    class LiquidAsset {
        -String iban
        -String description
        -int balance
        -String dateIssued
        +LiquidAsset(String, String, int, String)
        +setters()
        +getters()
    }

    Customer "1" *-- "1" FinancialInformation
    FinancialInformation "1" *-- "1" SchufaOverview
    FinancialInformation "1" *-- "1" LiquidAsset
    FinancialInformation "1" *-- "1" IncomeProof
```

### Service Layer

```mermaid
classDiagram
    class DatabaseService {
        -String DB_URL
        -Encrypter encrypter
        -Connection connection
        -Customer currentCustomer
        +setCurrentCustomer(Customer)
        +saveCustomerInDatabase(String)
        +checkIfCustomerIsInDatabase(String)
        +getCustomerWithFinancialInfoByEmail(String, String)
        -createTables()
        -insertCustomerData()
        -insertFinancialData()
        -insertSchufaRecord()
        -extractCustomerFromResultSet()
    }

    class CustomerEvaluator {
        +Customer customer
        +evaluateCustomerInfo()
        -appendError()
        +checkFirstName(String)
        +checkName(String)
        +checkBirthdate(String)
        +checkCountry(String)
        +checkAddress(String)
        +checkEmail(String)
    }

    class FinancialInformationEvaluator {
        -double MAX_DEVIATION
        -int MAX_DOCUMENT_AGE_DAYS
        -FinancingContract journeyDetails
        -double MIN_MONTHLY_MONEY
        -FinancialInformation financialInfo
        -Customer currentCustomer
        +setCurrentCustomer(Customer)
        +evaluateFinancialInfo()
        -validateDocumentDates()
        -validateFinancialData()
    }

    DatabaseService -- Customer
    CustomerEvaluator -- Customer
    FinancialInformationEvaluator -- Customer
    FinancialInformationEvaluator -- FinancialInformation
```

### Frontend Controllers

```mermaid
classDiagram
    class Controller {
        <<abstract>>
        +handleNavigation()
    }

    class Navigatable {
        <<interface>>
        +navigateToNextPage()
        +navigateToPreviousPage()
    }

    class Cacheable {
        <<interface>>
        +cacheData()
        +restoreData()
    }

    class e.g. Page3PersonalInformationController {
        -ComboBox titleField
        -TextField nameField
        -TextField firstNameField
        -DatePicker birthDatePicker
        -TextField countryField
        -TextField addressField
        -TextField emailField
        -Button continueButton
        -Button backButton
        -Customer currentCustomer
        -CustomerEvaluator evaluator
        +initialize()
        +cacheData()
        +restoreData()
    }

    Controller <|-- e.g. Page3PersonalInformationController
    Navigatable <|.. e.g. Page3PersonalInformationController
    Cacheable <|.. e.g. Page3PersonalInformationController
```

### Security and Encryption

The application employs robust security measures for data protection, focusing on encryption of sensitive customer data and secure password handling.

- **Encryption Algorithm**: AES (Advanced Encryption Standard) with a 256-bit key size is used for encrypting sensitive data. The specific mode of operation is CBC (Cipher Block Chaining) with PKCS5Padding.
- **Key Derivation**: Encryption keys are derived from the user's email and password using PBKDF2WithHmacSHA256. This process incorporates a fixed salt ("1.FC Kaiserslautern") and 65536 iterations to enhance key strength and resistance against brute-force attacks. The user's password is first hashed using SHA-256 before being used in the key derivation.
- **Initialization Vector (IV)**: A random 16-byte Initialization Vector (IV) is generated for each encryption operation. This IV is prepended to the encrypted data and is crucial for ensuring that identical plaintexts encrypt to different ciphertexts.
- **Hashing**: SHA-256 (Secure Hash Algorithm 256-bit) is used for hashing passwords and other sensitive information where one-way transformation is required.
- **Data Storage**: The `Encrypter` class handles the encryption and decryption of data. The `DatabaseService` utilizes the `Encrypter` to ensure that sensitive information stored in the SQLite database is encrypted.

```mermaid
classDiagram
    class Encrypter {
        +encrypt(String, String, String)
        +decrypt(String, String, String)
        +hashString(String)
    }

    class CryptographyException {
        +String message
    }

    class ValidationException {
        +String message
    }

    CryptographyException <|-- EncryptionException
    CryptographyException <|-- DecryptionException
    CryptographyException <|-- HashingException

    DatabaseService --> Encrypter
```

## Build and Deployment

### Build Configuration
The project uses Maven for build automation. Key plugins:
- maven-compiler-plugin (3.14.0)
- maven-surefire-plugin (3.2.5)
- javafx-maven-plugin (0.0.8)

### Build Commands
```bash
# Clean and build the project
mvn clean install

# Run the application
mvn clean javafx:run
```

## Testing

The project utilizes JUnit Jupiter for unit testing and Mockito for creating mock objects to isolate units of code. Integration tests are also present, particularly for database operations.

### Test Directory Structure
Tests are located in the `src/test/java` directory, mirroring the package structure of the main source code (`src/main/java`). For example, tests for frontend controllers can be found under `src/test/java/com/atdit/booking/frontend/controllers/`, with subdirectories for different processes like `payment_process` and `customer_registration`.

### Testing Framework
- JUnit Jupiter for unit testing
- Mockito for mocking dependencies
- Integration tests for database operations

### Test Categories
1. Unit Tests
    - Business logic testing
    - Service layer testing
    - Utility class testing

2. Integration Tests
    - Database operations
    - External service integrations

### Test Coverage Report

| Component                    | Class Coverage | Method Coverage | Line Coverage | Branch Coverage |
|-----------------------------|----------------|-----------------|---------------|-----------------|
| Backend        | 57%            | 64%             | 44%           | 55%             |


### Running Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=Page7ControllerPageLoginTest

# Run with coverage report
mvn verify
``` 

## Development Guidelines

### Code Style
- Follow Java naming conventions
- Use meaningful variable and method names
- Document public APIs and complex logic
- Keep methods focused and single-responsibility

### Version Control
- Use feature branches for new development
- Write meaningful commit messages
- Review code before merging to main branch

### Best Practices
1. Error Handling
    - Use appropriate exception handling
    - Log errors with sufficient context
    - Implement graceful degradation

2. Performance
    - Use connection pooling for database operations
    - Implement caching where appropriate
    - Optimize UI rendering

3. Maintainability
    - Follow SOLID principles
    - Write clean, self-documenting code
    - Maintain proper documentation

## Troubleshooting

### Common Issues
1. Database Connection
    - Verify SQLite file permissions
    - Check connection string
    - Ensure proper Hibernate configuration

2. UI Issues
    - Verify FXML file locations
    - Check scene graph hierarchy
    - Validate event handlers

### Logging
- Implement comprehensive logging
- Use appropriate log levels
- Include relevant context in log messages

## Future Improvements
1. Consider implementing:
    - Caching layer for improved performance
    - Additional security measures
    - API documentation generation
    - Containerization support




