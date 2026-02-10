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

Creating a new functional test suite by duplicating the setup procedures and instance variables from existing classes, such as CreateProductFunctionalTest.java, significantly reduces code quality by violating the DRY (Don't Repeat Yourself) principle and introducing unnecessary code duplication. This approach leads to a "Code Smell" where maintenance becomes difficult and error-prone; for instance, any change to the base URL logic or port configuration would require manual updates across every test class. To improve the cleanliness and maintainability of the code, a better approach would be to implement a Base Test Class that encapsulates the common @BeforeEach setup and @LocalServerPort configuration, allowing specific test suites to inherit these shared functionalities. Furthermore, adopting the Page Object Model (POM) would further enhance the design by separating the technical details of web element interactions from the high-level test logic, making the suite more resilient to changes in the UI structure.