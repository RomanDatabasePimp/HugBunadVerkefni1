package project.persistance.repositories;

import org.json.JSONObject;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Repository;

/**
 * Class creates a connection to the Redis server and oversees all the of the 
 * sending and receiving of data between the Spring Server and Redis server.
 */
@Repository
public class RedisRepository {
	
	private final JedisConnectionFactory redisConn; // define our connection

	public RedisRepository() {
		/*
		 * Since the Spring boot server will be deployed on Roman's Server all the
		 * databases are hosted localy on the same server so we can just define our
		 * server connections as local
		 */
		this.redisConn = new JedisConnectionFactory();
		this.redisConn.setHostName("localhost");
		this.redisConn.setPort(6379);
	}
	
	/**
	 * Insert string
	 * 
	 * @param key
	 * @param string
	 */
	public void insertString(String key, String string) {
		RedisConnection con = this.redisConn.getConnection();
		con.setEx(key.getBytes(), 1800, string.getBytes());
		con.close();
	}
	
	public String getString(String key) {
		RedisConnection con = this.redisConn.getConnection();
		String string = new String(con.get(key.getBytes()));
		con.close();
		return string;
	}

	/**
	 * Usage : red.insertData(key,data)
	 *   For : red is a RedisServices class 
	 *         key is the key to the data  in our case the username of the new user 
	 *         data is the data of the client - in our case stringified json 
	 * After: Insert the user into redis for 30 min
	 */
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
	 * Usage : red.checkIfKeyExists(key) 
	 *   For : red is a RedisServices class key is point to the data in redis 
	 *  After: returns true if the key is associated with data in redis
	 */
	public boolean checkIfKeyExists(String key) {
		RedisConnection con = this.redisConn.getConnection();
		boolean exists = con.exists(key.getBytes());
		con.close();
		return exists;
	}

	/**
	 * Usage : red.checkIfKeyExists(key) 
	 *   For : red is a RedisServices class key is point to the data in redis 
	 *  After: returns a json object of a form {username:, password: , email: }
	 */
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
	 * Usage : red.destroyData(key) 
	 *   For : red is a RedisServices class 
	 *         key is point  to the data in redis 
	 *  After: Remove all the data that the key is pointing to (!CONFIRM IF DATA EXISTS FIRST !!!)
	 */
	public void destroyData(String key) {
		RedisConnection con = this.redisConn.getConnection();
		con.del(key.getBytes());
		con.close();
	}

}
