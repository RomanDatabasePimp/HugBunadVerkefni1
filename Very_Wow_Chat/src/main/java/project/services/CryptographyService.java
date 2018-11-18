package project.services;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.stereotype.Service;

@Service
public class CryptographyService {

	@Value("${cryptography.storage.password}")
	private String password;

	@Value("${cryptography.storage.salt}")
	private String salt;
	
	private static String spw;
	private static String ss;
	
	@PostConstruct
	public void init() {
		spw = password;
		ss = salt;
	}
	
	

	/**
	 * Encrypts plaintext and returns its ciphertext.
	 * 
	 * @param plaintext
	 * @return ciphertext
	 */
	public static String getCiphertext(String plaintext) {
		return Encryptors.text(spw, ss).encrypt(plaintext);
	}

	/**
	 * Decrypts ciphertext and returns in plaintext.
	 * 
	 * @param ciphertext
	 * @return plaintext
	 */
	public static String getPlaintext(String ciphertext) {
		return Encryptors.text(spw, ss).decrypt(ciphertext);
	}
}
