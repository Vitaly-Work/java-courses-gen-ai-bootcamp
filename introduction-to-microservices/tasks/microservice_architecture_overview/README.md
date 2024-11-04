# Table of Content

- [What to do](#what-to-do)
- [Sub-task 1: Resource Service](#sub-task-1-resource-service)
- [Sub-task 2: Song Service](#sub-task-2-song-service)
- [Notes](#notes)

> Note: This is the updated version of the task. If you have already started working on [the previous version](README-deprecated.md), please continue with the previous one.

## What to do

Your task is to implement a microservices system consisting of two services:

- **Resource Service** - for MP3 file processing
- **Song Service** - for song metadata management

### Service relationships

The services have a one-to-one relationship, where:
- Song metadata uses the Resource ID as its primary key.
- Deleting a resource triggers the cascading deletion of its associated metadata.

### Requirements

- **Spring Boot** 3.0 or higher
- **Java** 17 or later (LTS versions)
- **Build Tool**: Maven or Gradle
- **Database**: PostgreSQL

## Sub-task 1: Resource Service

The Resource Service implements CRUD operations for processing MP3 files. When uploading an MP3 file, the service:

- Extracts file metadata (using external libraries like [Apache Tika](https://www.tutorialspoint.com/tika/tika_extracting_mp3_files.htm))
- Stores the MP3 file in the database
- Invokes the Song Service to save the MP3 file metadata

### API endpoints

#### 1. Upload resource

```
POST /resources
```

**Request:**

- Content-Type: audio/mpeg
- Body: Binary MP3 audio data

**Response:**

```json
{
    "id": 1
}
```

**Status codes:**

- 200: OK
- 400: Validation failed or request body is invalid MP3
- 500: Internal server error

#### 2. Get resource

```
GET /resources/{id}
```

**Parameters:**

- `id` (Integer): The ID of the resource to retrieve
- Restriction: Must be an ID of an existing resource

**Response:**

- Body: Audio bytes (MP3 file)

**Status codes:**

- 200: OK
- 404: Resource with specified ID does not exist
- 500: Internal server error

#### 3. Delete resources

```
DELETE /resources?id=1,2
```

**Parameters:**

- `id` (String): Comma-separated list of resource IDs to remove
- Restriction: CSV string length must be < 200 characters

**Response:**

```json
{
    "ids": [1,2]
}
```

**Status codes:**

- 200: OK
- 400: Invalid input - CSV string format is invalid or too long
- 500: Internal server error

## Sub-task 2: Song Service

The Song Service implements CRUD operations for managing song metadata records. The service uses the Resource ID as the primary key for metadata records, ensuring a direct one-to-one relationship between resources and their metadata.

### API endpoints

#### 1. Create song metadata

```
POST /songs
```

**Request body:**

```json
{
    "id": "1",
    "name": "We are the champions",
    "artist": "Queen",
    "album": "News of the world",
    "duration": "02:59",
    "year": "1977"
}
```

**Validation rules:**

- All fields are required
- `id`: numeric string, must be an ID of an existing resource
- `name`: 1-100 characters text
- `artist`: 1-100 characters text
- `album`: 1-100 characters text
- `duration`: mm:ss format with leading zeros
- `year`: YYYY format between 1900-2099

**Response:**

```json
{
    "id": "1"
}
```

**Status codes:**

- 200: OK
- 400: Song metadata missing or validation error
- 409: Conflict - metadata for this ID already exists
- 500: Internal server error

#### 2. Get song metadata

```
GET /songs/{id}
```

**Parameters:**

- `id` (Integer): ID of the metadata to retrieve
- Restriction: Must be an ID of an existing resource

**Response:**

```json
{
    "id": "1",
    "name": "We are the champions",
    "artist": "Queen",
    "album": "News of the world",
    "duration": "02:59",
    "year": "1977"
}
```

**Status codes:**

- 200: OK
- 404: Song metadata with specified ID does not exist
- 500: Internal server error

#### 3. Delete songs metadata

```
DELETE /songs?id=1,2
```

**Parameters:**

- `id` (String): Comma-separated list of IDs to remove
- Restriction: CSV string length must be < 200 characters

**Response:**

```json
{
    "ids": [1,2]
}
```

**Status codes:**

- 200: OK
- 400: Invalid input - CSV string format is invalid or too long
- 500: Internal server error

## Notes

### Error handling

Please add a global exception handler using `@RestControllerAdvice` and manage errors with a unified response structure:

#### Simple error response

```json
{
    "errorMessage": "Resource with ID=1 not found",
    "errorCode": "404"
}
```

#### Validation error response

```json
{
    "errorMessage": "Validation error",
    "details": {
        "duration": "Duration must be in the format MM:SS",
        "year": "Year must be in a YYYY format"
    },
    "errorCode": "400"
}
```

### Postman collection for testing

Please use the [Postman collection](./api-tests/introduction_to_microservices.postman_collection.json) for testing the Resource Service and Song Service APIs. Ensure that your services handle requests accurately and comply with the API specifications outlined in the documentation. This collection will help validate the correct functioning of all features and data validations.

### Database implementation requirements

- Use Docker containers for database deployment
- [PostgreSQL](https://hub.docker.com/_/postgres) 16+ is required as the database engine
- Each service should have its own dedicated database instance

### Structure

Both microservices represent a unified application and (will) use shared files. Please merge them into a single folder (Git repository), using the following folder structure as an example:

For a Maven-based project:

```
maven-project/
├── resource-service/
│   ├── src/
│   └── pom.xml
├── song-service/
│   ├── src/
│   └── pom.xml
└── .gitignore
```

For a Gradle-based project:

```
gradle-project/
├── resource-service/
│   ├── src/
│   └── build.gradle
├── song-service/
│   ├── src/
│   └── build.gradle
├── settings.gradle
└── .gitignore
```
