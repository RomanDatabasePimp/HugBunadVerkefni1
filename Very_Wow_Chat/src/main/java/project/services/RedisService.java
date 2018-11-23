package project.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.persistance.repositories.RedisRepository;

/**
 * This class uses the Redis repository to fulfill all Redis based requests by
 * our application
 */
@Service
public class RedisService {

	@Autowired
	private RedisRepository redisRepository;

	/**
	 * Checks if user name <code>username</code> exists in Redis database.
	 * 
	 * @param username
	 * @return <code>True</code> if user name exists in Redis database, otherwise
	 *         <code>False</code>
	 */
	public boolean userNameExists(String username) {
		return this.redisRepository.checkIfKeyExists(username);
	}

	/**
	 * Usage : Redser.insertUser(key, data) For : Redser is RedisService class key
	 * is the pointer to where the data is stored data is the userdata in NOTE THIS
	 * HAS TO BE IN JSON FORM After: inserts the user into the redis repo and
	 * returns the key
	 */
	public String insertUser(String key, JSONObject data) {
		this.redisRepository.insertData(key, data.toString());
		return key;
	}

	/**
	 * Usage : Redser.getAndDestroyData(key) For : Redser is RedisService class key
	 * String is the pointer to where the data is stored After: fetches the data
	 * where the key is pointing to and then removes it from redis
	 */
	public JSONObject getAndDestroyData(String key) {
		JSONObject data = this.redisRepository.getData(key);
		this.redisRepository.destroyData(key);
		return data;
	}

	/** pretty simple getters and setters here i dont think these need explaining */
	public void insertString(String key, String string) {
		redisRepository.insertString(key, string);
	}

	public String getString(String key) {
		return redisRepository.getString(key);
	}

	public String getAndDestroyString(String key) {
		String string = redisRepository.getString(key);
		redisRepository.destroyData(key);
		return string;
	}

}
