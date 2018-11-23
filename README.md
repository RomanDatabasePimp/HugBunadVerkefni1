# Hugbúnaðarverkefni 1: Very Wow Chat

A chat room application.
Members: 
* Róman (ror9@hi.is)
* Vilhelm (vilhelml@hi.is)
* Davíð (dah38@hi.is)

## Architecture
This server exposes a REST interface.  The request and responses are JSON objects.
Redis is used to store temporary data.  Neo4j is used to store chat rooms, users and tags.  MongoDB is used to store chat messages.
Chat messages and use email are encrypted, this is the backend i.e server the client side i.e frontend is in another repository.

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
  
## Guide on how to set up the services
I recomend you follow these steps before cloning the repo, there is allot of stuff that needs to be done<br>
before strarting the project.

### Neo4j

1. open https://neo4j.com/
2. on the top right corner there is a download button
3. Choose the free Desktop version ( the default pop up should be it you just click download)
4. Fill the form and click download
5. Once download is complete just run the setup with all the default settings
6. Run `Neo4j Desktop` as administrator
7. In Neo4j Desktop create a project with name `Very_Wow_Chat` (this name doesn't really matter)
8. In that project create a graph with the name `Very_Wow_Chat`  (this name matters) !
9. You can choose any password you want BUT REMEMBER !<br> 
   you will have to use the same password in the application.properties in Spring
10. Once the Graph is created make sure its created,<br>
    you can do this by clicking "Manage" in the graph <br>
    and in there click the play button it will say status Running

### Redis
Please note this setup is for windows sorry mac people :(
1. open https://redis.io/
2. Click on the link that says " Check the downloads page "
3. Download the latest stable version of redis (its a rar file)
4. I recomend you extract your .rar file on your root my pc's root is c:\
5. open the command promp in the file you extracted
6. type : redis-service.exe
7. you should see the server boot and running on PORT 6379 keep it this way
( PLEASE NOTE : there are more settings that need to be added for long term usage <br>
  the default setting will do for testing, you might be greeted with that redis dosent want to save <br>
  files or something like that, in that case just turn off redis and turn it on again)

### MongoDB
1. open  https://www.mongodb.com/
2. In the top right corner click on "Get MongoDB"
3. In the Tabs Select the "Server" tab 
4. Select the "Current version" and choose download MSI
5. Run the MSI -> Click Next until u reach what kind of  mongodb install you want
6. Select Complete install
7. In services configuration click on the radio button "Run Service as local or domain user"
8. keep the domain as "." (means local) , The account name and password is the your local machines account and password <br>
   NOTE : pin dosent work you have to create a password if you are using pin to loggin into your machine
9. finsh the install 
10. if you installed MongoDb with all the default paths you can navigate to C:\Program Files\MongoDB\Server\4.1\bin
11. run "mongod" as administrator it will open and close right away<br>
    if you open your task manager you will see it running


## Cloning the repo and running the server
1. clone this repo on to your desktop
2. Please note this is a Maven project
3. open the project in your preffered IDE
4. navigate to src/main/resources and find the application.properties.example
5. remove the .example ending from the file and fill in the following constants
   - spring.data.neo4j.password=   (the password you choose when you created the graph)
6. if you used all the default settings this is the only thing you should configure
7. run application.java and you will be fine !!!

## Creating Test data
1. if you have POSTMAN or Curl you can create data in the data base
2. send a HTTP GET REQUEST on http://localhost:9090/createdata and you will create 3 users
   - username : ror9  ,  Pass: Test$1234
   - username : vilhelml  ,  Pass: Test$1234
   - username : dah38  ,  Pass: Test$1234
3. open https://hugbomailserver.herokuapp.com/ just to wake up the service since this is a free version of heroku <br>
   the services goes to sleep after 30 min of no use

### You thought you were done ??? O nononono
As we said in the beginning this is only the backend, you still have to deploy the frontend
which can be found here : https://github.com/RomanDatabasePimp/Hugbunadverkefni1-Webapp
