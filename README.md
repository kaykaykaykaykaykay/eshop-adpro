Reflection Module 1

Reflection 1

Thus far I believe that I have implemented basic CRUD functionality for the Product entity using the Spring Boot MVC architecture.

A few clean code principles that I applied are consistent naming of classes, methods, and model attributes improved clarity and reduced errors making the code easy to read and maintain.
Secure coding practice wise, data access is restricted to the repository layer and product IDs are generated on the server side using UUIDs instead of user input, helping prevent data manipulation and ensuring safer request handling.

The problems that I've encountered while doing this module is that I am still learning the entire Spring Boot architecture. Hence there was a lot of trail and errors.

Reflection 2

Unit testing feels like an extra workload however it provides a safety net for refactoring and bug detections.
There is no fixed number instead its every logical path and edge case should be covered.
No. While Code Coverage is a valuable metric to identify untested parts of the code, 100% coverage does not guarantee a bug-free program. It only means every line of code was executed during testing, not that the logic is correct or that all possible input combinations were validated.
To ensure tests are "enough," focus on Boundary Value Analysis and Negative Testing rather than chasing a coverage percentage

Creating a naew functional test suite by duplicating the setup procedures and instance variables from existing classes, such as CreateProductFunctionalTest.java, significantly reduces code quality by violating the DRY (Don't Repeat Yourself) principle and introducing unnecessary code duplication. This approach leads to a "Code Smell" where maintenance becomes difficult and error-prone; for instance, any change to the base URL logic or port configuration would require manual updates across every test class. To improve the cleanliness and maintainability of the code, a better approach would be to implement a Base Test Class that encapsulates the common @BeforeEach setup and @LocalServerPort configuration, allowing specific test suites to inherit these shared functionalities. Furthermore, adopting the Page Object Model (POM) would further enhance the design by separating the technical details of web element interactions from the high-level test logic, making the suite more resilient to changes in the UI structure.

Reflection Module 2

1. I have identified several code quality issues using SonarCloud and compiler feedback. First, a duplicate class definition in ProductServiceImpl caused structural errors and was fixed by removing the unintended inner class so that only one service implementation remained. Second, field injection using @Autowired was replaced with constructor injection to follow Spring best practices and improve testability and maintainability. Third, potential NullPointerException risks in the repository were resolved by replacing direct .equals() comparisons with Objects.equals() for null-safe comparisons. Finally, fields that were not reassigned were marked as final to improve immutability and remove code smells. The overall strategy focused on improving correctness, safety, and adherence to recommended design practices.

2. The current implementation successfully meets the criteria for both Continuous Integration (CI) and Continuous Deployment (CD). For the CI portion, the GitHub Actions workflow automates the integration process by checking out the code, setting up the Java environment, and running the Gradle build and JaCoCo test suites every time a push or pull request occurs. This ensures that any continuous changes are immediately verified by an automated script without manual intervention. For the CD portion, integrating the repository with Koyeb using a pull-based approach fulfills the deployment definition. Because Koyeb remotely monitors the main branch and automatically pulls, builds, and deploys the Docker container upon any merged changes, the pipeline achieves true Continuous Deployment.

Reflection Module 3
1. Single Responsibility Principle (SRP): I removed the inheritance where CarController extended ProductController. Now, CarController is strictly responsible for handling car-related HTTP requests, and ProductController handles product-related ones.

Liskov Substitution Principle (LSP): By removing the inheritance relationship between CarController and ProductController, I eliminated a false hierarchy. Previously, substituting a ProductController with a CarController would result in a bloated class carrying inappropriate routing logic.

Dependency Inversion Principle (DIP): In CarController, I changed the injected dependency from the concrete CarServiceImpl class to the CarService interface.

2. Applying SOLID makes the codebase more modular, testable, and easier to maintain. For example, by applying DIP and injecting the CarService interface instead of the concrete class, I can now easily swap out the implementation (e.g., creating a MockCarService for unit testing) without touching the CarController code.
3. Failing to apply SOLID principles leads to tightly coupled and fragile code. For example, when CarController extended ProductController (violating SRP and LSP), any changes made to ProductController's base routing or methods could accidentally break or expose unwanted endpoints in CarController. It created an entangled architecture that is difficult to safely modify or scale.