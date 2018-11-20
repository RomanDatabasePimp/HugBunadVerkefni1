package project.services;

import java.security.SecureRandom;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.RandomStringUtils;
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
	
	private static final String PASSWORD_ALPHABET_1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";
	
	// "comfortable" alphabet to type in.
	private static final String PASSWORD_ALPHABET_2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&*()-_=+[{]}|;:,<.>?";
	
	private static final char[] PASSWORD_ALPHABET_1_CHARS = (new String(PASSWORD_ALPHABET_1)).toCharArray();
	private static final char[] PASSWORD_ALPHABET_2_CHARS = (new String(PASSWORD_ALPHABET_2)).toCharArray();

	@PostConstruct
	public void init() {
		spw = password;
		ss = salt;
	}
	
	public static String getStrongRandomPassword(int n) {
		
		
		final char[] alphabet = PASSWORD_ALPHABET_2_CHARS;
		
		String s = RandomStringUtils.random( n, 0, alphabet.length-1, false, false, alphabet, new SecureRandom() );
		return s;
	}

	
	public static String getRandomHexString(int n) {

		SecureRandom r = new SecureRandom();
		StringBuffer sb = new StringBuffer();
		while (sb.length() < n) {
			sb.append(Integer.toHexString(r.nextInt()));
		}

		return sb.toString().substring(0, n);
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
