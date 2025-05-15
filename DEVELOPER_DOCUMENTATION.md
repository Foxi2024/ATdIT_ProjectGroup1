# Developer Documentation: Booking Application

## 1. Overall Architecture

The application follows a layered architecture, broadly divided into:

*   **`frontend`**: Responsible for the user interface (UI) and user interaction. Uses JavaFX. Contains FXML files for views and Java controller classes. Located in `src/main/java/com/atdit/booking/frontend/`.
*   **`backend`**: Contains the core business logic, data persistence (manual SQL via `DatabaseService`), and domain models. Structured by domain concepts like `customer` and `financialdata`. Located in `src/main/java/com/atdit/booking/backend/`.
*   **`applications`**: Contains starter classes (`CustomerRegistrationApplicationStarter`, `PaymentProcessApplicationStarter`) for launching JavaFX UI flows. Located in `src/main/java/com/atdit/booking/applications/`.

## 2. Backend Architecture

The `backend` package (`src/main/java/com/atdit/booking/backend/`) is central to the application's functionality. Persistence is primarily handled manually by `DatabaseService.java` using JDBC against an SQLite database, despite Hibernate being a dependency (its usage is not apparent in the core entities).

### Core Entities/Data Objects (Conceptual - Mapped manually in `DatabaseService`)

*   **`Customer`** (`backend.customer.Customer`):
    *   Attributes: `title`, `firstName`, `name`, `country`, `birthdate`, `address`, `email`.
    *   Relationships: Has one `FinancialInformation`.
*   **`FinancialInformation`** (`backend.financialdata.financial_information.FinancialInformation`):
    *   Attributes: `avgNetIncome`, `monthlyFixCost`, `liquidAssets`, `minCostOfLiving`, `monthlyAvailableMoney`.
    *   Relationships: Has one `IncomeProof`, one `LiquidAsset`, one `SchufaOverview`.
*   **`IncomeProof`** (`backend.financialdata.financial_information.IncomeProof`): Details of income verification.
*   **`LiquidAsset`** (`backend.financialdata.financial_information.LiquidAsset`): Details of liquid assets.
*   **`SchufaOverview`** (`backend.financialdata.financial_information.SchufaOverview`): SCHUFA score and details.
    *   Contains/uses **`Schufaauskunft`** (`backend.financialdata.financial_information.Schufaauskunft`): Individual SCHUFA entries/records.
*   **`Contract`** (`backend.financialdata.contracts.Contract`):
    *   Base class for financial agreements.
    *   Attributes: `paymentMethod`, `TOTAL_AMOUNT` (constant).
    *   Methods: `getContractText()` (intended to be overridden).
    *   Subclasses:
        *   `OneTimePaymentContract`
        *   `BuyNowPayLaterContract`
        *   `FinancingContract` (likely has more complex attributes like installments, interest rates).
*   **`BankTransferDetails`**, **`CreditCardDetails`** (`backend.financialdata.financial_information`): Payment method details, likely associated with `Contract` or `Customer`.

### Key Service / Logic Classes

*   **`DatabaseService`** (`backend.database.DatabaseService`):
    *   Responsible for all CRUD operations for the entities.
    *   Manages database connections (SQLite) and executes manual SQL queries.
    *   Handles data encryption/decryption via `Encrypter`.
*   **`Encrypter`** (`backend.database.Encrypter`): Provides encryption/decryption for sensitive data.
*   **`CustomerEvaluator`** (`backend.customer.CustomerEvaluator`): Contains logic to evaluate customer data (e.g., for creditworthiness). Uses `Customer` and `FinancialInformation`.
*   **`FinancialInformationEvaluator`** (`backend.financialdata.processing.FinancialInformationEvaluator`): Evaluates financial health using `FinancialInformation`.
*   **`PaymentMethodEvaluator`** (`backend.financialdata.processing.PaymentMethodEvaluator`): Validates or processes payment methods.
*   **`FinancialDocumentsGenerator`** (`backend.financialdata.processing.FinancialDocumentsGenerator`): Generates financial documents (e.g., contracts, summaries).
*   **`FinancialInformationParser`** (`backend.financialdata.processing.FinancialInformationParser`): Parses financial data from various inputs.

### Class Diagram Sketch (High-Level Text Representation)

For detailed visual diagrams, use a UML modeling tool.

```
[Customer] 1 -- 1 [FinancialInformation]
   - title
   - firstName
   ...

[FinancialInformation]
   - avgNetIncome
   ...
   -- 1 [IncomeProof]
   -- 1 [LiquidAsset]
   -- 1 [SchufaOverview]

[SchufaOverview]
   -- * [Schufaauskunft]

[Contract] (Base)
   - paymentMethod
    ^
    |--- [OneTimePaymentContract]
    |--- [BuyNowPayLaterContract]
    |--- [FinancingContract]

[DatabaseService] ---uses---> [Encrypter]
                   ---manages--> [Customer, FinancialInformation, Contract, etc. (via manual SQL)]

[CustomerEvaluator] ---uses---> [Customer, FinancialInformation]
[FinancialInformationEvaluator] ---uses---> [FinancialInformation]
```

## 3. Frontend Architecture

The `frontend` package (`src/main/java/com/atdit/booking/frontend/`) manages the JavaFX-based UI.

*   **Structure**: Divided into sub-packages like `customer_registration` and `payment_process`, which in turn contain `controllers` sub-packages.
*   **FXML Files**: Located in `src/main/resources/com/atdit/booking/frontend/` (mirroring the controller package structure). These define the UI layouts.
    *   Example: `src/main/resources/com/atdit/booking/frontend/customer_registration/Page1PaymentProcessStart.fxml` (assuming standard naming convention).
*   **Controller Classes**: Java classes in `frontend` sub-packages (e.g., `Page1PaymentProcessStartController.java`) handle UI events, data binding, and communication with backend services.
    *   The `customer_registration` flow includes controllers like `Page1PaymentProcessStartController`, `Page2DataPrivacyController`, `Page3PersonalInformationController`, etc., indicating a step-by-step process.
*   **`super_controller`**: This package might contain a base controller or a main application frame controller.

## 4. Application Starters

Located in `src/main/java/com/atdit/booking/applications/`:

*   **`CustomerRegistrationApplicationStarter.java`**:
    *   JavaFX `Application` subclass.
    *   Launches the customer registration UI flow.
    *   Configured as the main class in `pom.xml` for `mvn javafx:run`.
*   **`PaymentProcessApplicationStarter.java`**:
    *   JavaFX `Application` subclass.
    *   Launches a payment process UI flow.

## 5. Test Coverage

### Testing Frameworks

*   **JUnit 5**: Used for writing and running unit tests.
*   **Mockito**: Used for creating mock objects to isolate units for testing.

### Test Structure and Focus

*   Tests are located in `src/test/java/`.
*   Current test files observed under `src/test/java/com/atdit/booking/frontend/` suggest a primary focus on testing frontend controllers.
*   **Crucial Gap**: There is a need to add comprehensive unit tests for the backend logic, particularly for:
    *   `DatabaseService` (consider H2 in-memory DB for tests or extensive mocking)
    *   `CustomerEvaluator`, `FinancialInformationEvaluator`, `PaymentMethodEvaluator`
    *   Contract classes and their logic
    *   `FinancialInformationParser`, `FinancialDocumentsGenerator`
    *   `Encrypter`

### Measuring and Improving Test Coverage

*   **Recommendation**: Integrate the **JaCoCo Maven Plugin** (`jacoco-maven-plugin`) into `pom.xml` to measure code coverage.
*   **Action**: Add the following to the `<build><plugins>` section of `pom.xml`:
    ```xml
    <plugin>
        <groupId>org.jacoco</groupId>
        <artifactId>jacoco-maven-plugin</artifactId>
        <version>0.8.12</version> <!-- Check for the latest version -->
        <executions>
            <execution>
                <goals>
                    <goal>prepare-agent</goal>
                </goals>
            </execution>
            <execution>
                <id>report</id>
                <phase>test</phase>
                <goals>
                    <goal>report</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
    ```
*   After adding JaCoCo, run `mvn clean test jacoco:report` to generate an HTML coverage report (usually in `target/site/jacoco/index.html`). This report will highlight untested code areas.
*   **Goal**: Strive for high test coverage, especially for critical backend business logic, to ensure application reliability and maintainability.

## 6. Further Recommendations

1.  **UML Class Diagrams**: Create detailed, visual UML class diagrams using a dedicated tool. These diagrams should clearly depict attributes, methods, and relationships (associations, aggregations, inheritance) for all key classes in both backend and frontend.
2.  **Backend Unit Tests**: Significantly expand unit test coverage for all backend services and logic components.
3.  **Database Interaction Clarity**: If Hibernate is intended to be used more broadly, clarify its role and ensure entities are correctly annotated or mapped. If manual SQL in `DatabaseService` is the intended final approach for these core objects, ensure this is well-documented within `DatabaseService` itself, especially regarding table structures and relationships.
4.  **FXML Documentation**: Briefly document each FXML file, its purpose, and the main controller it's associated with.
5.  **Continuous Integration (CI)**: Consider setting up a CI pipeline (e.g., using GitHub Actions, Jenkins) to automatically build, run tests, and generate coverage reports on each commit. 