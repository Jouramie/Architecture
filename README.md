# Les Anarchitectes


## How to maven
##### Clean the target folder
```bash
mvn clean
```

##### Compile the project
```bash
mvn compile
```

##### Launch the project
```bash
mvn exec:java
```

##### Run the unit tests
```bash
mvn test
```

##### Run the integration tests
```bash
mvn verify
```

##### Launch the checkstyle
```
mvn checkstyle:check
```

The commands can also be combined. Each goal will be executed from left to right.
```bash
# Clean the target, checkstyle then run the unit tests.
mvn clean checkstyle:check test
```

## IntelliJ plugins
You should install the following plugins: 
- CheckStyle-IDEA
- Save Actions

### CheckStyle-IDEA configuration
![Checkstyle configuration](https://i.imgur.com/JVqsrk9.png)

### Save Actions configuration
![Save Actions configuration](https://i.imgur.com/eQFTzW5.png)

### IntelliJ Code Style for auto-format
To import the google code style to your IntelliJ configuration, follow the steps.
![Import checkstyle configuration](https://i.imgur.com/XiHhEhm.png)
Choose the `google_checks.xml` file in the root of the repository.
![Choose google_checks.xml](https://i.imgur.com/qNcgos2.png)