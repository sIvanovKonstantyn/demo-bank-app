#Infrastructure

### _There are few infrastructure components:_
- _**Configuration server** (spring boot application);_
- _**Message broker** (kafka + zookeeper);_
- _**Deposit service DB** (postgres);_
- _**Query API DB** (Mongo DB);_

_A startup script for all components is describing in infrastructure/docker-compose.yml_
_Details about every component below..._

---------------------------------------------------------------

#Configuration server

Just build a container and start it. Configuration server will be run on 8888 port.

---------------------------------------------------------------

#Message broker

It's a dev environment component, so we have just one kafka and zookeeper node. They use local server IP address and default ports 
(9092 and 2181 respectively). There are no volumes present so every start of container - is a empty broker environment.
All topics will be created manually (we could use scripts, tools or java admin clients for these issues).


---------------------------------------------------------------

#Deposit service DB

In dev environment postgres will be start with default properties.
DB could be accessed on 5432.

---------------------------------------------------------------

#Query API DB

We couldn't use default admin user in our application, so we should create our DB
and use in some console. For these issues we could use compass UI and its cli
(https://www.mongodb.com/try/download/compass).

```
> use deposit_query_api_db
> db.createUser( { user: "test", pwd: "test",  roles: ["readWrite"] })
> db.user.insert({name: "test"})
```
When a container will be started - mongo DB could be accessed on 27017.