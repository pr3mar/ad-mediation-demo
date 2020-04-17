# Ad Mediation 

- [Setup](#setup)
- [Description](#description)
- [Future improvements](#future-improvements)
## Setup

Building the app:
```shell script

mvn clean package

# skipping the tests:
mvn clean package -DskipTests
```

Running the app locally:
```shell script
java -jar target/ad.mediation.jar
```

Deploying the app from local environment:
```shell script
mvn package appengine:deploy -DskipTests -Dapp.projectId=<PROJECT_ID>
```

## Description

### Tech stack used:
- Spring boot
- H2 database

### Data models
This application uses 3 distinct models:
- `AdNetwork` model:
```json
{
  "id": "Internally calculated entity ID, not exposed.",
  "externalId": "Custom identifier of the network for the external user. Max size is 64, and it cannot be an empty string.",
  "name": "Descriptive alias of the network. Max size is 64, and it cannot be an empty string.",
  "score": "Score of type integer. A valid: score >= 0>",
  "dateCreated": "Date when the entity was created.",
  "dateUpdated": "Date when the entity was last modified."
}
```
- `SuccessResponse` model:
```json
{
  "operation": "Operation that was successfully finished.",
  "rowsAffected": "Number of rows the operation affected."
}
```
- `ExceptionResponse` model:
### 
```json
{
  "status": ".HTTP Status code.",
  "message": "Descriptive exception message."
}
```
### REST API
To achieve the required functionalities efficiently, we need the following endpoints:
All the enpoints are exposed on the `/api/networks` base mapping.
- `GET /`
    - welcome endpoint which greets you
    - produces a `SuccessResponse`
- `GET /{externalId}`
    - get the `AdNetwork` with provided `{externalId}`
- `GET /all`
- `GET /all/{page}`
- `GET /all/{page}/{pageSize}`
    - These 3 endpoints produce a list of `AdNetwork`s from the database in a paginated manner. 
    - The default value are `page = 0`, `pageSize = 100`. There is no upper limit set for `page`, while `pageSize` has to be in the interval `[0, 1000]`.
- `POST /create`
    - Creates a new `AdNetwork` entity from the provided object in the body. NOTE: It ignores any set dates.
    - produces the created object.
- `POST /create/bulk`
    - Creates entities from all the `AdNetwork` objects provided in a list in the request body.      
    - Due to the large size of the input, this endpoint produces a `SuccessResponse` which indicates that the operation was successful and how many entities were created.
- `PUT /update/{externalId}/score/{score}`
    - update the score of the `AdNetwork` with `externalId` to `score`
    - produces the updated object
- `PUT /update/{externalId}`
    - update the name or score of the `AdNetwork` with `externalId`
    - consumes an `AdNetwork` object with updated properties
    - produces the updated `AdNetwork` object
- `PUT /update/`
    - similar as `/update/{externalId}`, but it consumes a list of `AdNetwork`s.
    - produces a `SuccessResponse` indicating the number of affected rows, due to potentially large input.
    

## Future Improvements

- database migration management with flyway or a similar service
- use a caching service
- slimmer responses
- enable Swagger