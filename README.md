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
![Add a checkstyle configuration](https://i.imgur.com/UcDfAjb.png)
![Browse to the configuration](https://i.imgur.com/zeYQwST.png)
![Select google_check.xml](https://i.imgur.com/izzhuTn.png)
![Press next](https://i.imgur.com/7mlSQb7.png)
![Select the modified config](https://i.imgur.com/FUWMPKx.png)

### Save Actions configuration
![Save Actions configuration](https://i.imgur.com/jc7MiWn.png)

### IntelliJ Code Style for auto-format
![Import checkstyle configuration](https://i.imgur.com/DWVAQmp.png)
![Choose IntelliJ-codestyle.xml](https://i.imgur.com/osKkkt9.png)
![Import to current scheme](https://i.imgur.com/OKVXlIf.png)
![Activate no formatting comments](https://i.imgur.com/cc5ERDX.png)