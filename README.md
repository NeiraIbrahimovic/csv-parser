# Character-Level CSV Parser

A stateful Java CSV parser and supporting character reader.

## Technical depth

Quoted fields, delimiters, record boundaries, and format-error handling.

## Product perspective

This project supports technical product discussions about input contracts, edge cases, acceptance criteria, and the tradeoffs visible in the implementation. It demonstrates hands-on technical study, not a production deployment.

## Repository layout

- `src/`: application source and recovered tests, with neutral Java package names.
- `pom.xml`: Maven build and test configuration.
- Root data files, where present: recovered educational fixtures.
- `SOURCE-MANIFEST.json`: hashes of the source used to prepare this independent copy.

## Running and testing

Construct CSVReader with a CharacterReader; inspect the recovered tests for examples.

Run `mvn test` from the repository root with Maven and a JDK (the recovery run used JDK 24). Tests use JUnit; the audit used JUnit Platform Console Standalone 1.11.4. Open the chosen source folder as a Java project, add its required libraries and JUnit to the classpath, compile `src/`, and run the recovered test classes with that folder as the working directory. Test results are scoped to the selected files and fixtures.

## Validation status

The repackaged source compiled with JDK 24. The local JUnit run passed **5 tests**, with 0 failures. Maven configuration is provided for convenience; the reported run used javac and JUnit Console directly. Passing tests do not establish exhaustive correctness or production readiness.
