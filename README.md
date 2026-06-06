# Mainframe UI Sample (very simple)

This is a minimal Spring Boot (Maven) project meant for practicing GitHub → Jenkins integration.

Key points:
- Java 21
- Spring Boot 3.2.x
- No database
- Simple static UI at `/` and two API endpoints under `/api`

Run locally:

```bash
mvn spring-boot:run
# then open http://localhost:8080/
```

Build package:

```bash
mvn -B -DskipTests package
```

Jenkins:
- The repository includes a `Jenkinsfile` with a single `Build` stage that runs `mvn -B -DskipTests package`.
# asif-sample-project