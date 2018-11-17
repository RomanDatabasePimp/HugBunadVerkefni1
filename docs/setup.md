# Setup

## Spring Initializr

Generate a `Maven Project` with `Java` and Spring Boot `2.1.0`.
* NOTE: actually we're using version 2.0.5 :/

* Group
    * `hugbunadar_verkefni_hopur_1_veryWowchat`
        * It should be something like (according to Maven conventions): `is.hi.{group name}.hbv501` [2]
* Artifact
    * `Very_Wow_Chat`
        * It should be something like (according to Maven conventions): `very-wow-chat` [2]
* Dependencies
    * Web
    * Security
    * Neo4j
    * Redis
    * MongoDb
    * Websocket

In addition, add the following dependencies into the `pom.xml` file,

```xml
  <!-- Redis Java client. -->
  <dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
    <type>jar</type>
  </dependency>
  
  <!-- JSON Web Token support for the JVM.  Used to create JSON
       Web Tokens for user authentication. -->
  <dependency>
    <groupId>io.jsonwebtoken</groupId>
	<artifactId>jjwt</artifactId>
	<version>0.6.0</version>
  </dependency>

  <!-- Provides building blocks for both client side validation and server side
       data validation. We use it for client side data validation, e.g. email
       validations and etc. -->
  <dependency>
    <groupId>commons-validator</groupId>
    <artifactId>commons-validator</artifactId>
    <version>1.4.1</version>
  </dependency>

  <!-- Neo4j stuff that's supported by Spring Boot.  You might see that the 
       version is not needed, but sometimes vilhelm gets compile errors if he 
       dosen't add version, so please don't remove just set to ignore. -->
  <dependency>
    <groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-neo4j</artifactId>
	<version>2.0.6.RELEASE</version><!--$NO-MVN-MAN-VER$-->
  </dependency>

  <!-- JSON in Java.  We use it to create JSON objects. -->
  <dependency>
    <groupId>org.json</groupId>
    <artifactId>json</artifactId>
    <version>20180130</version>
  </dependency>
```


## Databases

### MongoDB

#### Setup: macOS

Run `brew install mongodb`. [1]

And you're done!

### Neo4j

#### Setup: macOS

Download `Neo4j Desktop`.

Create a new project and press `Add Graph` and select `Create a Local Graph`.  

The name of the graph should be the same as the project, i.e. `Very_Wow_Chat`.  The username and password are to be found the in `application.properties` file in the `Very_Wow_Chat` project.

The version of the neo4j database should be 3.4.7.

Press `Start`.

And you're done!


### Redis

#### Setup: macOS

Make sure you have `Homebrew` <https://brew.sh/>, otherwise install it first.

Then run `brew install redis` in Terminal.

And you're done!


 
## Sources

* [1] <https://docs.mongodb.com/manual/tutorial/install-mongodb-on-os-x/>
* [2] <https://maven.apache.org/guides/mini/guide-naming-conventions.html>