# ScaleCube Configuration Service
[![Build Status](https://travis-ci.org/scalecube/scalecube-configuration.svg?branch=master)](https://travis-ci.org/scalecube/scalecube-configuration)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/fb26db972acc4cb0afd008abef503dbc)](https://www.codacy.com/app/ScaleCube/scalecube-configuration?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=scalecube/scalecube-configuration&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/fb26db972acc4cb0afd008abef503dbc)](https://www.codacy.com/app/ScaleCube/scalecube-configuration?utm_source=github.com&utm_medium=referral&utm_content=scalecube/scalecube-configuration&utm_campaign=Badge_Coverage)
[![Join the chat at https://gitter.im/scalecube/Lobby](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/scalecube/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Twitter URL](https://img.shields.io/twitter/url/https/twitter.com/fold_left.svg?style=social&label=Follow%20%40ScaleCube)](https://twitter.com/scalecube)

Web Site: [http://scalecube.io](http://scalecube.io/)

ScaleCube-Configuration Service is a Multi-Tenant configuration service that enables clients to store key, value pairs in collections of data where the value is any json object. it is based on [redisson](https://github.com/redisson/redisson) project and [redis](https://redis.io/) as persistence storage.

To better understand how the ScaleCube-Configuration service work we first need to understand few concepts:

## Account Service:
The account service is responsible to manage users and organizations and the membership of users in organizations. 

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
The configuration service manages collections of [Key, Value]. clients of the configuration service can:
 - Get key from collection - requires read-level api key.
 - List all entries from collection - requires read-level api key.
 - save entry in a collection (create or update) - requires write -level api key.
 - Delete key from a collection - requires write -level api key.
 
### Configuration
 
```javascript
{
   "collection":"environments",
   "key":"dev",
   "value:{
      "url":"http://localhost:8080"
   }
}
```