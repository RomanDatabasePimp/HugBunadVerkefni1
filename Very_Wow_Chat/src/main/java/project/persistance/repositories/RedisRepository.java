package project.persistance.repositories;

import org.json.JSONObject;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Repository;

/**
 * Class creates a connection to the Redis server and oversees all the of the 
 * sending and receiving of data between the Spring Server and Redis server.
 * 
 * TODO: rename this repository to something like to something like temporary
 * storage repository something-something...
 */
@Repository
public class RedisRepository {
	
	private final JedisConnectionFactory redisConn; // define our connection

	public RedisRepository() {
		this.redisConn = new JedisConnectionFactory();
		// TODO: put host name and port in application.properties file.
		this.redisConn.setHostName("localhost");
		this.redisConn.setPort(6379);
	}
	
	/**
	 *  Usage : red.insertString(key,string) 
	 *    For : red is a RedisServices class 
	 *          key is pointer to the data that will be stored in redis
	 *          string is the data you want to store
	 *   After: stores the string for 30 min */
	public void insertString(String key, String string) {
		RedisConnection con = this.redisConn.getConnection();
		con.setEx(key.getBytes(), 1800, string.getBytes());
		con.close();
	}
	
	/**
<<<<<<< HEAD
	 * 
	 * @param key
	 * @return
	 */
=======
	  * Usage : red.getString(key) 
	 *    For : red is a RedisServices class 
	 *          key is pointer to the data in redis 
	 *   After: returns the data that is assosiated to the key */
>>>>>>> 2d24c96e32ae5aefc810382eda3691667c017ae8
	public String getString(String key) {
		RedisConnection con = this.redisConn.getConnection();
		String string = new String(con.get(key.getBytes()));
		con.close();
		return string;
	}
	
	/**
<<<<<<< HEAD
	 * Inserts <code>data</code>, which is a stringified JSON object, with
	 * key <code>key</code>.
	 * 
	 * @param key access key
	 * @param data JSON object stringified.
	 */
=======
	 * Usage : red.insertData(key,data)
	 *   For : red is a RedisServices class 
	 *         key is the key to the data  in our case the username of the new user 
	 *         data is the data of the client - in our case stringified json 
	 * After: Insert the user into redis for 30 min */
>>>>>>> 2d24c96e32ae5aefc810382eda3691667c017ae8
	public void insertData(String key, String data) {
		/*
		 * insert the data in redis for 30 min if the data is not validated it is lost
		 * and you have to start the register proccess again from the start Since
		 * username is uniq in the long term storage data base we can use it as a key to
		 * the info
		 */
		RedisConnection con = this.redisConn.getConnection();
		con.setEx(key.getBytes(), 1800, data.getBytes());
		con.close();
	}

	/**
<<<<<<< HEAD
	 * Checks if entry with key <code>key</code> exists in Redis database.
	 * 
	 * @param key The key
	 * 
	 * @return <code>true</code> if an entry with key <code>key</code> exists
	 * in the database, otherwise <code>false</code>.
	 */
=======
	 * Usage : red.checkIfKeyExists(key) 
	 *   For : red is a RedisServices class key is point to the data in redis 
	 *  After: returns true if the key is associated with data in redis */
>>>>>>> 2d24c96e32ae5aefc810382eda3691667c017ae8
	public boolean checkIfKeyExists(String key) {
		RedisConnection con = this.redisConn.getConnection();
		boolean exists = con.exists(key.getBytes());
		con.close();
		return exists;
	}
	
	/**
<<<<<<< HEAD
	 * Retrieves entry with key <code>key</code>.  The object that is received
	 * is assumed to be a stringified JSON object.
	 * 
	 * @param key Access key
	 * 
	 * @return JSON object.
	 */
=======
	 * Usage : red.checkIfKeyExists(key) 
	 *   For : red is a RedisServices class key is point to the data in redis 
	 *  After: returns a json object of a form {username:, password: , email: } */
>>>>>>> 2d24c96e32ae5aefc810382eda3691667c017ae8
	public JSONObject getData(String key) {
		RedisConnection con = this.redisConn.getConnection();
		/*
		 * data is stored in byte array in redis so we need to first get it and convert
		 * it into a string
		 */
		String data = new String(con.get(key.getBytes()));
		con.close();
		/*
		 * next we create a json object of the recived data that was pasrsed as a string
		 */
		JSONObject user = new JSONObject(data);
		return user; // return the json obj
	}

	/**
<<<<<<< HEAD
	 * Deletes entry with key <code>key</code> in Redis database.
	 * 
	 * @param key The key.
	 */
=======
	 * Usage : red.destroyData(key) 
	 *   For : red is a RedisServices class 
	 *         key is point  to the data in redis 
	 *  After: Remove all the data that the key is pointing to (!CONFIRM IF DATA EXISTS FIRST !!!) */
>>>>>>> 2d24c96e32ae5aefc810382eda3691667c017ae8
	public void destroyData(String key) {
		RedisConnection con = this.redisConn.getConnection();
		con.del(key.getBytes());
		con.close();
	}

}
