 Aajil Technical Assessment – Flight Purchase Automation

Java + Selenium + JUnit5 | POM Framework

 Overview

This project is the practical implementation of the technical assessment for automating the Purchase Flight scenario on the BlazeDemo website.
The goal was to build automation that is organized, scalable, and easy to run, so it can execute automatically after any deployment.

In short:
Select cities → list flights → choose a flight → fill purchase data (random each run) → validate the result.

 Project Structure
src/
 ├── main/java/aajil/
 │       ├── core/          (Driver + BasePage)
 │       └── pages/         (Home, Flights, Purchase, Confirmation)
 │
 └── test/java/aajil/tests/
         └── PurchaseTest.java

pom.xml
README.md


The POM pattern is used clearly:
Each page has its own class, and all actions are structured logically and easy to read.

🛠️ Setup

You need:

Java 17+

Maven

Chrome browser (Selenium will handle it automatically)

Check installation:

java -version
mvn -version

▶️ Running the Tests

From the project directory:

mvn clean test


To run a single test:

mvn -Dtest=PurchaseTest test


During execution, the console will display test logs and results.

🧪 What Is Automated?

A main function purchaseEndToEnd performs the entire flow:

✔ Select cities (or generate random ones if not provided)
✔ Choose a flight by sequence (with validation)
✔ Use new random purchase data every run
✔ Complete the payment
✔ Validate:

Status = PendingCapture

Amount > 100 USD

If anything is invalid → the reason is printed to logs and the test stops.

 Why This Design?

POM for clarity and maintainability

Only Explicit Waits, no Thread.sleep

DriverFactory to manage WebDriver lifecycle correctly

Random Test Data for more dynamic scenarios

Exceptions + Assertions to validate inputs and results

✔️ Summary

The project is simple, organized, and scalable.
It fully covers all assessment requirements—from environment setup to executing all five required scenarios—
with clean, readable code that can be executed directly through Maven.