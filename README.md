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
Reproduce the following configuration. Make sure the *Change visibility on field or methods to lower access* option is unchecked.
![Save Actions configuration](https://i.imgur.com/kCNn7Eh.png)

### IntelliJ Code Style for auto-format
![Import checkstyle configuration](https://i.imgur.com/DWVAQmp.png)
![Choose IntelliJ-codestyle.xml](https://i.imgur.com/osKkkt9.png)
![Import to current scheme](https://i.imgur.com/OKVXlIf.png)
![Activate no formatting comments](https://i.imgur.com/cc5ERDX.png)

## RestAssured integration tests format
Unfortunately, the formatter break the cute indentation format we gave to the RestAssured integration tests. But, by disabling the formatter, we can keep our integration tests readable! Make sure you add the `//@formatter:off` and `//@formatter:on` before and after the test.
```java
public class SomeRestAssuredIT {
    @Test
    public void whenPinging_thenApplicationRespondWithEchoMessage() {
        //@formatter:off
        given()
            .param("echo", SOME_ECHO)
        .when()
            .get(API_PING_ROUTE)
        .then()
            .statusCode(200)
            .body("version", any(String.class))
            .body("date", any(String.class))
            .body("echo", equalTo(SOME_ECHO));
        //@formatter:on
    }
}
```

Also, make sure to always use the statically imported `given()`, `when()` and `then()` methods to keep the test ordered.

### Email configuration
On linux you need to source the `aws-creds` at the route of the project (you can use the instruction `set VARIABLE=value` on Windows), so the app has access to the SES service of AWS.
Here's a link with more info: https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/setup-credentials.html
The creds are assign to an AWS Role and only have access to the SES service.

Has of now the SES service is in SANDBOX, so it can only send email to verified address. Here are 2 gmail accounts you can use to send and receive email:
email:Archi.test.42@gmail.com
password: Archi.42.

email:invest.ul.2018@gmail.com
password: Archi.42.

A request is in progress to be able to send email to emails no verified my(Olivier Beaulieu) account.
