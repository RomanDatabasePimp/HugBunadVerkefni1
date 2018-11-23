# Hugbúnaðarverkefni 1: Very Wow Chat

A chat room application.

Members: 

* Róman (ror9@hi.is)
* Vilhelm (vilhelml@hi.is)
* Davíð (dah38@hi.is)

## Introduction

The project is split into two parts: the server part (this repository), and client (server) part (another repository).

Dependencies for this repository are,

* [Maven](https://maven.apache.org/)

"External" dependencies for this repository are,

* [git](https://git-scm.com/)
	* Version 2.17.2 or higher.
* [Neo4j](https://neo4j.com/)
	* Desktop version 1.1.10 or higher.
* [MongoDB](https://www.mongodb.com/)
	* Version 4.0.3 or higher.
* [Redis](https://redis.io/)
	* Version 5.0.0 or higher.

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
