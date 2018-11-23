# Hugbúnaðarverkefni 1: Very Wow Chat

A chat room application.
Members: 
* Róman (ror9@hi.is)
* Vilhelm (vilhelml@hi.is)
* Davíð (dah38@hi.is)

## Introduction
The project is split into Three parts 
1. The server part i.e backend (this repository)
2. The client part i.e frontend (another repository)
   Can be found at : https://github.com/RomanDatabasePimp/Hugbunadverkefni1-Webapp
3. The Email Server (another repository)<br>
   Can be found at : https://github.com/vilhelml/cluster-3f-mail-service <br>
   NOTE : You do not need to deploy this on your local machine this Server is running on : https://hugbomailserver.herokuapp.com/

## Dependencies For this project
This readme will cover all the steps on how to configure all the Dependencies except for Maven.<br>
These are the following Dependencies that are required to deploy the server.
1. Maven : https://maven.apache.org/
2. Neo4j Desktop version 1.1.10 or higher : https://neo4j.com/   
3. MongoDB Version 4.0.3 or higher : https://www.mongodb.com/
4. Redis Version 5.0.0 or higher https://redis.io/
  
## Setup of external services

### Neo4j

Run `Neo4j Desktop`.

In Neo4j Desktop create a project with name `Very_Wow_Chat` (this name doesn't really matter), and in that project create a graph with the name `Very_Wow_Chat`  (this name matters).

## Run external services

Run `Neo4j Desktop`.  Start graph `Very_Wow_Chat`.

### Neo4j

Run `Neo4j Desktop`.

### MongoDB

Simply run `mongod` in your CLI application.

### Redis

Simply run `redis-server` in your CLI application.

## Architecture

This server exposes a REST interface.  The request and responses are generally JSON objects.

Redis is used to store temporary data.  Neo4j is used to store chat rooms, users and tags.  MongoDB is used to store chat messages.

Chat messages and use email are encrypted.

## Other

### Roman's public server

* <http://85.220.46.169:8443>
