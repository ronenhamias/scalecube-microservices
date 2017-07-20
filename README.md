# ScaleCube Configuration Service
[![Build Status](https://travis-ci.org/scalecube/scalecube-configuration.svg?branch=master)](https://travis-ci.org/scalecube/scalecube-configuration)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/fb26db972acc4cb0afd008abef503dbc)](https://www.codacy.com/app/ScaleCube/scalecube-configuration?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=scalecube/scalecube-configuration&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/fb26db972acc4cb0afd008abef503dbc)](https://www.codacy.com/app/ScaleCube/scalecube-configuration?utm_source=github.com&utm_medium=referral&utm_content=scalecube/scalecube-configuration&utm_campaign=Badge_Coverage)
[![Join the chat at https://gitter.im/scalecube/Lobby](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/scalecube/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Twitter URL](https://img.shields.io/twitter/url/https/twitter.com/fold_left.svg?style=social&label=Follow%20%40ScaleCube)](https://twitter.com/scalecube)

Web Site: [http://scalecube.io](http://scalecube.io/)

## CONFIGURATION SERVICE:

---
ScaleCube-Configuration Service is a Multi-Tenant configuration service that enables clients to store key, value pairs in collections of data where the value is any json object. it is based on [redisson](https://github.com/redisson/redisson) project and [redis](https://redis.io/) as persistence storage.

To better understand how the ScaleCube-Configuration service work we first need to understand few concepts:

## Account Service:
The account service is responsible to manage users and organizations and the membership of users in organizations. 

The Account Service manages and validates api-keys with write or read permission levels. the api-keys are based on jwt tokens.

See: [Account Service API](https://github.com/scalecube/scalecube-configuration/wiki/Account-Service-API)

### User
Users are registered in scalecube-configuration service after they have completed authentication with google and granted rights to the application.
once google replied with token_id it is sent to the account service for registration and verification, once verified client has responded with the user details.

each user has userId provided by the authentication service and it is used to identify the user.

### Organization
Once a user is registered it can create organizations and invite other or kickout users to its organization.
once a user is invited to an organization it become organization member, organization members might be one of two roles:
 - Owner: owners has rights to manage users membership and delete the organization.
 - Members: have the right to view the organization details only


## Configuration Service:
Any request The configuration service requires an api key issued by the Account-Service and the Configuration service validates the request with the account service to understand validity of the access token and the permission level it has to access the apis.

The configuration service manages collections of [Key, Value]. clients of the configuration service can:
 - Get key from collection - requires read-level api key.
 - List all entries from collection - requires read-level api key.
 - save entry in a collection (create or update) - requires write -level api key.
 - Delete key from a collection - requires write -level api key.
 
### Configuration
Configuration entry is a key value pair stored in a collection. in the example bellow is an entry that represent some details about logical environment called 'dev' and where its 'url' entry point. so clients may request from the configuration service the 'url' for their 'dev' environment.

its up to the users to determine what is the collection names, keys and values they wish to store and manage as their configuration.
  
```javascript
{
   "collection":"environments",
   "key":"dev",
   "value":{
      "url":"http://localhost:8080"
   }
}
```

## GETTING STARTED:

---
### Running Everything locally:

- install redis.
- clone the project
- run GatewayAll main.
- open your browser in http://localhost:8080
- Login to google.
- check your browser consul to see the interaction with the server.

### Running Microservices cluster on Docker:

[see wiki page](https://github.com/scalecube/scalecube-configuration/wiki/Running-ScaleCube-Dockers)
