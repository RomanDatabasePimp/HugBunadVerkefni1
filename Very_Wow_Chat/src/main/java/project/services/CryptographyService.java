package project.services;

import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CryptographyService {

	@Value("${cryptography.storage.password}")
	private String password;

	@Value("${cryptography.storage.salt}")
	private String salt;

	public String test() {



		return "--";
	}

	public String generateSalt() {
		SecureRandom secureRandom = new SecureRandom();
		byte[] saltBytes = new byte[32];
		secureRandom.nextBytes(saltBytes);
		// StandardCharsets.UTF_8

		String salt = Base64.getEncoder().encodeToString(saltBytes);
		return salt;
	}

	/**
	 * 
	 * @param plaintext
	 * @return
	 */
	public String getCiphertext(String plaintext) {
		
		System.out.println("Salt: " + salt);
		

		try {
			
			System.out.println("Password: " + password);
			System.out.println("Salt: " + salt);
			

			byte[] saltBytes = Base64.getDecoder().decode(salt);
			char[] passwordCharArray = password.toCharArray();
			
			/* Derive the key, given password and salt. */
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec(passwordCharArray, saltBytes, 65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

			/* Encrypt the message. */
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secret);
			AlgorithmParameters params = cipher.getParameters();
			// byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
			byte[] ciphertextBytes = cipher.doFinal(plaintext.getBytes("UTF-8"));

			String ciphertext = Base64.getEncoder().encodeToString(ciphertextBytes);

			return ciphertext;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 
	 * @param ciphertext
	 * @return
	 */
	public String getPlaintext(String ciphertext) {
		


		try {
			
			System.out.println("Password: " + password);
			System.out.println("Salt: " + salt);
			
			byte[] saltBytes = Base64.getDecoder().decode(salt);
			char[] passwordCharArray = password.toCharArray();
			
			/* Derive the key, given password and salt. */
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec(passwordCharArray, saltBytes, 65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

			byte[] ciphertextBytes = Base64.getDecoder().decode(ciphertext);

			/* Decrypt the message, given derived key and initialization vector. */
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secret);
			String plaintext = new String(cipher.doFinal(ciphertextBytes), "UTF-8");
			return plaintext;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
