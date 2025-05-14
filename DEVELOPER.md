# Developer Documentation

## Project Overview
This is a booking application built using Java and JavaFX, utilizing a modern tech stack for robust functionality and user experience. The application follows a layered architecture pattern with clear separation between frontend and backend components.

## Architecture

### System Architecture
The application follows a layered architecture with the following main components:

1. **Presentation Layer (Frontend)**
   - Built with JavaFX (version 24.0.1)
   - FXML-based UI layouts
   - MVC (Model-View-Controller) pattern implementation

- **Location**: `src/main/java/com/atdit/booking/frontend/`
- **Primary Responsibilities**: User interface, input handling, view management
- **Key Classes**:
  ```
  ├── Controller/
  │   ├── AbstractApplication
  │   ├── Controller (abstract)
  │   ├── Page3PersonalInformationController
  │   ├── Page4DeclarationFIController
  │   ├── Page5ProofFIController
  │   ├── Page6CreateAccountController
  │   ├── Page7ControllerPageLogin
  │   ├── Page9aCreditCardController
  │   └── Page9bBankTransferController
  ```
- **Interfaces**:
  - `Navigatable`: Navigation between pages
  - `Cacheable`: Data persistence between views

2. **Business Logic Layer**
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
  │   │   ├── FinancialInformation
  │   │   ├── IncomeProof
  │   │   ├── LiquidAsset
  │   │   └── SchufaOverview
  │   └── processing/
  │       ├── FinancialInformationEvaluator
  │       ├── FinancialInformationParser
  │       └── FinancialDocumentsGenerator
  ├── PaymentProcess
  └── CustomerRegistrationProcess
  ```

3. **Data Access Layer**
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
  └── exceptions/
      ├── CryptographyException
      └── ValidationException
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
   - Version: Java 24
   - Build System: Maven
   - Compiler Configuration: JDK 1.8 compatible

2. **Frontend Framework**
   - JavaFX 24.0.1
   - FXML for UI layouts

3. **Database**
   - SQLite 3.49.1
   - Hibernate ORM 6.6.13.Final for data persistence

4. **Testing Framework**
   - JUnit Jupiter 5.10.2
   - Mockito 5.11.0 for mocking

### Dependencies
```xml
Core Dependencies:
- javafx-controls:24.0.1
- javafx-fxml:24.0.1
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
        -String name
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

# Run tests
mvn test
```

## Testing

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

### Test Coverage Metrics

| Component                    | Class Coverage | Method Coverage | Line Coverage | Branch Coverage |
|-----------------------------|----------------|-----------------|---------------|-----------------|
| Backend        | 57%            | 64%             | 46%           | 56%             |


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




