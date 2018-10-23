package project.services;

import org.json.JSONObject;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

/* Class creates a connection to the redis server and oversees all the 
 * of the sending and reciving of data between the Spring Server and Redis server */
public class RedisServices {
  private final JedisConnectionFactory redisConn; // define our connection
  
public RedisServices() {
	/* The way our server works is that only the Spring-boot api is open
	 * to the internet everything else is on the local network */
    this.redisConn = new JedisConnectionFactory();
    this.redisConn.setHostName("localhost");
    this.redisConn.setPort(6379);
  }
  
  /* Usage : red.insertUser(key,data)
   *  For  : red is a RedisServices class
   *         key is the key to the data - in our case the username of the new user
   *         data is the data of the client - in our case stringified json 
   *  After: Insert the user into redis for 30 min */
  public void insertUser(String key, String data) {
    /* insert the data in redis for 30 min if the data is not validated it is lost 
	 * and you have to start the register proccess again from the start 
	 * Since username is uniq in the long term storage data base we can use it as a key to the info */
	this.redisConn.getConnection().setEx(key.getBytes(), 1800,  data.getBytes());
  }
  
  /* Usage : red.checkIfKeyExists(key)
   *   For : red is a RedisServices class
   *         key is point to the data in redis
   *  After: returns true if the key is associated with data in redis  */
  public boolean checkIfKeyExists(String key) { return this.redisConn.getConnection().exists(key.getBytes()); }
  
  /* Usage : red.checkIfKeyExists(key)
   *   For : red is a RedisServices class
   *         key is point to the data in redis
   *  After: returns a json object of a form {username: ,
   *                                          password: ,
   *                                          email:    } */
  public JSONObject getData(String key) {
	/* data is stored in byte array in redis so we need to first get it and convert it into a string */
	String data = new String(this.redisConn.getConnection().get(key.getBytes()));
	/* next we create a json object of the recived data that was pasrsed as a string*/
	JSONObject user = new JSONObject(data);
	return user; // return the json obj
  } 
 
}


