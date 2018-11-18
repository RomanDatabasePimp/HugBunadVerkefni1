package project.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.stereotype.Service;

@Service
public class CryptographyService {

	@Value("${cryptography.storage.password}")
	private String password;

	@Value("${cryptography.storage.salt}")
	private String salt;

	/**
	 * Encrypts plaintext and returns its ciphertext.
	 * 
	 * @param plaintext
	 * @return ciphertext
	 */
	public String getCiphertext(String plaintext) {
		return Encryptors.text(password, salt).encrypt(plaintext);
	}

	/**
	 * Decrypts ciphertext and returns in plaintext.
	 * 
	 * @param ciphertext
	 * @return plaintext
	 */
	public String getPlaintext(String ciphertext) {
		return Encryptors.text(password, salt).decrypt(ciphertext);
	}
}
