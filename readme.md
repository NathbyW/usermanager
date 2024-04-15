# Illustration for Home code: Manager System

## Overview
This is a possible solution for the Manager System problem, which includes interface specifications and some deeper thoughts.
## Functionality

#### 1. Base64 decode
User information in request headers are all base64 encoded, such as:

````json
{
    "contentType":"application/json",
    "roleInfo":"eyJ1c2VySWQiOjEyMywiYWNjb3VudE5hbWUiOiJhZG1pbnVzZXIiLCJyb2xlIjoidXNlciJ9"
}

````

role information is the user information encoded in Base64. Decoded header should be:

````json
{
    "userId":123,
    "accountName": "adminuser",
    "role": "admin"
}
````

This decoding method(filter) will be applied in each request.

#### 2. Add user resource

Jackson and objectMapper tools are used to read and write local file called `accessRecord.json` which contains user endpoint information. Also added checks for file non-existence or emptiness to prevent serialization failure.

Since the data is a arrayList, we need to evaluate and de-duplicate for each request message. We've also added detection logic to skip the endpoints that have already been added.

#### 3.Request resource by users
Base on resource record in second function, we can easily make a request from given users. Pay attention to the process and exceptions handling for each file read operation.

#### 4.Unit test and other features
Added unit tests for each feature through Junit and Mockito, unit tests may not cover all cases due to time constraints. Also added the slf4j logging utility to display potential file read/write or system exceptions.

## Further thoughts

+ For actual production use, relying on a Base64-encoded header without any form of encryption or secret signature is not recommended in daily productions, as Base64 is easily decoded. In actual production, we can use other encryption algorithms for the request message, such as JWT Or AES.
+ File persistence is a simple way for this demo project, but database should be considered in real situations. For scenarios with high request concurrency, caching like Redis or local thread-safe structures can also be considered.
+ From a security perspective, requests also need to include parameter verification as well as session management in order to
  ensure high availability of the system.