package project.services;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import project.persistance.repositories.RedisRepository;

/* This class uses the redisrepository to fufill all redis based requests by our app */
@Service
public class RedisService {
  private final RedisRepository RedisRepository; // get our Repo
  public RedisService ( ) { this.RedisRepository = new RedisRepository(); }
  
  
  /* Usage : Redser.userNameExists(username) 
   *  For  : Redser is RedisService class
   *         username is a string
   *  After: return true if the usernamename Exists in the redis database*/
  public boolean userNameExists(String username) { return this.RedisRepository.checkIfKeyExists(username); }
  
  /* Usage : Redser.insertUser(key, data) 
   *  For  : Redser is RedisService class
   *         key  is the pointer to where the data is stored
   *         data is the userdata in NOTE THIS HAS TO BE IN JSON FORM
   *  After: inserts the user into the redis repo and returns the key */
  public String insertUser(String key,JSONObject data) {
    this.RedisRepository.insertData(key, data.toString());
	return key;
  }
  
  /* Usage : Redser.getAndDestroyData(key) 
   *  For  : Redser is RedisService class
   *         key String is the pointer to where the data is stored
   *  After: fetches the data where the key is pointing to and then removes it from redis  */
  public JSONObject getAndDestroyData(String key) {
    JSONObject data = this.RedisRepository.getData(key);
    this.RedisRepository.destroyData(key);
	return data;
  }
  
}
