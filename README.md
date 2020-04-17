# Ad Mediation 

- [Setup](#setup)
- [Description](#description)
- [Implementation limitations](#implementation-limitations)
- [Future improvements](#future-improvements)

## Setup

These instructions assume that the user is in the repo root
```shell script
cd <repo root>
```

Building the app:
```shell script
# with tests
mvn clean package

# skipping the tests:
mvn clean package -DskipTests
```

Running the app locally:
```shell script
java -jar target/ad.mediation.jar 
```
after initialization of the application, open:
```
http://localhost:8080/api/networks/
```
in your preferred browser, or API explorer such as [Postman](https://www.postman.com/)
and follow the description below to start exploring the API :)

Deploying the app from a local environment:
**NOTE:** as a prerequisite you must have `gcloud` [configured](https://cloud.google.com/sdk/docs) on your machine, and a google app engine app [created](https://cloud.google.com/appengine/docs/flexible/java/quickstart) (follow the before you begin section to create an app engine application). This application assumes that it is the default service of the GAE app.
```shell script
mvn package appengine:deploy -DskipTests -Dapp.projectId=<YOUR_PROJECT_ID>
```

Online deployment is available on this [link](https://ad-mediation-demo.ew.r.appspot.com/api/networks/).

The app is being built, tested and deployed automatically as soon as the changes are pushed to the `master` branch of the Github repository.

## Description

In the following subsections I present a brief explanation of what the system is composed of, and the details for kicking off with it.

### Tech stack used
- Spring boot
- H2 database
- Google App Engine
- Github Actions for CI/CD automation

### Data models
This application uses 3 distinct models:
- `AdNetwork` model:
```json
{
  "id": "Internally calculated entity ID, not exposed.",
  "externalId": "Custom identifier of the network for the external user. Max size is 64, and it cannot be an empty string.",
  "name": "Immutable descriptive alias of the network. Max size is 64, and it cannot be an empty string.",
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
  "status": "HTTP Status code.",
  "message": "Descriptive exception message."
}
```
### REST API
To achieve the required functionalities efficiently, we need the following endpoints:
All the enpoints are exposed on the `/api/networks` base mapping.

#### GET methods
- `GET /`
    - welcome endpoint which greets you
    - produces a `SuccessResponse`
- `GET /{externalId}`
    - get the `AdNetwork` with provided `{externalId}`
- `GET /all?page={page}&pageSize={pageSize}`
    - This endpoint produces a list of `AdNetwork`s from the database in a paginated manner. 
    - The default value are `page = 0`, `pageSize = 100`. 
    - There is no upper limit set for `page`, while `pageSize` has to be in the interval `[0, 1000]`.
- `GET /list?page={page}&pageSize={pageSize}`
    - This endpoint works and has exactly the same properties as the one above (`GET /all`) but retrieves just an ordered list of externalIds of the `AdNetwork`s.

#### POST methods
- `POST /create`
    - Creates a new `AdNetwork` entity from the provided object in the body. NOTE: It ignores any set dates.
    - produces the created object.
- `POST /create/bulk`
    - Creates entities from all the `AdNetwork` objects provided in a list in the request body.      
    - Due to the large size of the input, this endpoint produces a `SuccessResponse` which indicates that the operation was successful and how many entities were created.
    
#### PUT methods
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

### Actuator
[Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html) is a plugin which exposes some common useful information about the running service. It is exposed on the `/api/actuator` base mapping and it consists of: 
- `GET /`
    - exposes the docs and all available actuator endpoints
- `GET /health`
    - exposes whether the service is UP
- `GET /info`
    - basic info about the application
- `GET /metrics`
    - a list of all available metrics - `metricName`
- `GET /metrics/{metricName}`
    - details for `metricName`

### Accessing the database
The database is available for exploration at http://localhost:8080/api/h2-console

It consists of a single table `Ad_Networks`, same as described in the [AdNetworks](src/main/java/com/demo/ad/mediation/models/entity/AdNetwork.java) entity file.

There is a predefined sample data loaded each time the application is started, the data is available in [data.sql](src/main/resources/data.sql).

### Github actions
For the purpose of automating the deployment process, I decided to use github actions.
It is the simplest way of automating the entire process for small projects, as there are plugins for almost every requirement.
The workflow is triggered only when there has been a push detected on the master branch. 

The implemented workflow consists of:
    - code compilation
    - building and testing of the application
    - deploying to Google App Engine.

The workflow halts whenever an exception occurs, and sends out a failure notification.

### Considered scenario while developing
1. The data is pre-processed and waits to be loaded into the service, in a format as defined in [Data Models](#data-models)
2. Upon deployment, the data needs to be loaded the data in the service (`POST /create/bulk`) 
3. The data is served via `GET /list` which returns a list of `AdNetwork`'s `externalId`s, and each network is expanded separately when (if) needed with `GET /{externalId}` (sort of lazy load to preseve traffic)
4. Whenever the need arises, the `score` of each `AdNetwork` can be updated via `PUT /update/{externalId}/score/{score}` or in bulk via `PUT /update`


## Implementation limitations
The limitations listed here are due to the priority of the development backlog:
- Using the H2 database: 
    - due to this we are unable to scale the service properly, as each instance has its own in-memory database which cannot be shared. As a consequence of this, the state is not shared among the horizontally scaled instances.
    - Although this does not imply that it cannot be configured, the priority was lower, and the functionality is not affected.
- Limited entity model
    - It is limited because it is very static and rigid, we do not have much information to leverege from
    - A proposal for improvement is to create a mechanism for customizing the ad networks based on geo-locations, as different ad networks have different content and based on that we can further adjust the targeting of the audience.   
- Serving content statistics:
    - as requests fly in our servers we can keep track of what they have been requesting and at which rate, sort of an very focused and analyzable audit log.
    - this is helpful to further adjust the scores of the ad networks and to potentially identify outliers 
- Lack of tests:
    - for this one there is no real excuse, just a lack of time
    - it was tested carefully via Postman, but that does not ensure future reliability
 
## Future Improvements

- Use a caching service
    - since the number of GET operations is (I think) far greater than the number of `POST` and `PUT` operations it makes perfect sense to have a caching system in place which is going to boost the performance of the service.
- Improve the tests
    - they do not cover the application at all currently :(
- Enable [Swagger](https://swagger.io/), or other similar service
    - what I documented in this file should be available out of the box at a REST API endpoint
- Add a layer of security: 
    - https,
    - managed access for creating/updating the `AdNetwork`s
- Improved metrics and monitoring
    - since we need to ensure high availability of our service we need to closely monitor it so that we can act accordingly should there be any need for that.
- Database migration management with flyway or a similar service
    - should the need arise of frequently updating the database, we need to have a tool which will easily manage the versions of the database for us 
