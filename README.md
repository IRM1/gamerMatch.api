# GamerMatch API

Spring Boot backend for the GamerMatch MVP.

## Current MVP Scope

The API now supports:

- WSU-domain signup and login
- token-backed session auth
- player discovery and search
- Tinder-style like flow backed by mutual matches
- direct messaging between matched users
- profile update and delete
- legacy user/game endpoints from the original course project

## Stack

- Java 17
- Spring Boot 3.5
- Gradle wrapper
- MariaDB on `localhost:3308`

Base URL:

- `http://localhost:8080/GamerMatch`

## Key Configuration

The default local configuration in [`demo/src/main/resources/application.properties`](./demo/src/main/resources/application.properties) expects:

- database: `jdbc:mariadb://localhost:3308/MEW_GM_DB`
- username: `root`
- password: `toor`
- allowed email domains: `wright.edu,mail.wright.edu`

## Run Locally

1. Start the database from the `gamerMatch.main/DatabaseDesign` repo.
2. Move into [`demo`](./demo).
3. Run:

```bash
./gradlew bootRun
```

## Validation

```bash
./gradlew test
```

## Main API Areas

- `POST /auth/signup`
- `POST /auth/login`
- `POST /auth/logout`
- `GET /auth/me`
- `GET /discover`
- `GET /matches/me`
- `POST /matches/{targetUserId}/like`
- `GET /messages/{otherUserId}`
- `POST /messages/{otherUserId}`
- `GET /me`
- `PUT /me/profile`
- `DELETE /me`

## Repository Layout

```text
gamerMatch.api
├─ demo/
│  ├─ build.gradle
│  ├─ gradlew
│  └─ src/main/java/com/mew/demo/...
├─ homework/
├─ MEWdb/
├─ SQL/
└─ README.md
```
