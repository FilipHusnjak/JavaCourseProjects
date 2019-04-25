package hr.fer.zemris.java.hw06.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Objects;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Program used for several duties: 
 * <ul>
 * <li> calculates message digest for specific file using SHA-256 algorithm and
 * checks whether the calculated digest matches the given one
 * <li> encrypts the given files and creates the new one encrypted with specific key
 * and vector initialization using AES crypto algorithm
 * <li> decrypts the given file that was previously encrypted using AES crypto algorithm
 * and creates new file same as original one
 * </ul>
 * 
 * @author Filip Husnjak
 */
public class Crypto {
	
	/**
	 * Defines algorithm used for calculating message digest
	 */
	private static final String ALGORITHM = "SHA-256";
	
	/**
	 * Size of a buffer used for reading files
	 */
	private static final int BUFFER_SIZE = 1024;
	
	/**
	 * Defines algorithm used for encrypting data
	 */
	private static final String CRYPTO_ALGORITHM = "AES";

	/**
	 * Starts the program and determines which action the program should preform:
	 * <ul>
	 * <li> if the first parameter was 'checksha' the program expects the second parameter to be
	 * filepath to the file which message digest is to be checked
	 * <li> if the first parameter was 'encrypt' the program expects two more arguments,
	 * first representing which file should be encrypted and second parameter defining the
	 * wanted name of the encrypted file
	 * <li> if the first parameter was 'decrypt' the program expects two more arguments,
	 * first representing which file should be decrypted and second parameter defining the
	 * wanted name of the decrypted file
	 * </ul>
	 * After that the program writes appropriate message defining the next steps.
	 * 
	 * @param args
	 *        program arguments
	 */
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Wrong number of arguments provided!");
			return;
		}
		try (Scanner sc = new Scanner(System.in)) {
			if (args[0].equals("checksha")) {
				System.out.print("Please provide expected sha-256 digest for " + args[1] + ":\n> ");
				String givenSha = sc.next();
				String sha = getSha(Paths.get(args[1]));
				System.out.println("Digesting completed. Digest of " + args[1] + 
						(sha.equals(givenSha) ? " matches expected digest." : 
							" does not match the expected digest.\r\n" + 
							"Digest was: " + sha));
			} else if (args[0].equals("encrypt") || args[0].equals("decrypt")) {
				if (args.length != 3) {
					System.out.println("Wrong number of arguments for encrypt/decrypt command provided!");
					return;
				}
				System.out.print("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):\n> ");
				String keyText = sc.nextLine();
				System.out.print("Please provide initialization vector as hex-encoded text (32 hex-digits):\n> ");
				String ivText = sc.nextLine();
				SecretKeySpec keySpec = new SecretKeySpec(Util.hextobyte(keyText), CRYPTO_ALGORITHM);
				AlgorithmParameterSpec paramSpec = new IvParameterSpec(Util.hextobyte(ivText));
				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
				cipher.init(args[0].equals("encrypt") ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);
				encryptAndWrite(Paths.get(args[1]), Paths.get(args[2]), cipher);
				System.out.println((args[0].equals("encrypt") ? "Encryption " : "Decryption ") + "completed. Generated file " + args[2] + " based on file " + args[1]);
			} else {
				System.out.println("Invalid command provided!");
			}
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		} catch (InvalidKeyException e) {
			System.out.println("Invalid key provided!");
		} catch (InvalidAlgorithmParameterException e) {
			System.out.println("Invalid parameter given!");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Invalid algorithm selected!");
		} catch (NoSuchPaddingException e) {
			System.out.println("Invalid padding selected!");
		} catch (IOException e) {
			System.out.println("Error uccured while reading file!");
		} catch (IllegalBlockSizeException e) {
			System.out.println("Invalid blocksize provided!");
		} catch (BadPaddingException e) {
			System.out.println("Bad padding!");
		}
	}
	
	/**
	 * Functional interface used for defining an action which should
	 * be performed upon each byte array which was read from some file.
	 * 
	 * @author Filip Husnjak
	 */
	@FunctionalInterface
	private static interface Visitor {
		
		/**
		 * Performs specific action with given byte array and number of relevant elements
		 * in the array.
		 * 
		 * @param buffer
		 *        array that represents data
		 * @param n
		 *        number of elements that are relevant in the given array
		 * @throws IOException if there was a problem processing given data
		 */
		void accept(byte[] buffer, int n) throws IOException;
	}
	
	/**
	 * Visits the given file and calls {@code visitor.accept(buffer, n)} every time
	 * input stream reads data into a buffered array.
	 * 
	 * @param path
	 *        file to be read
	 * @param visitor
	 *        implementation of a {@code Visitor} interface which defines an action
	 *        to be performed
	 * @throws IllegalArgumentException if the given file cannot be opened or if it
	 * 		   does not exist
	 * @throws NullPointerException if the given path or visitor is {@code null}
	 */
	private static void visitFile(Path path, Visitor visitor) {
		Objects.requireNonNull(visitor, "Given Visitor object cannot be null!");
		if (!Files.isReadable(Objects.requireNonNull(path, "Given path cannot be null!"))) {
			throw new IllegalArgumentException("Provided path is invalid or the file is not readable!");
		}
		try (InputStream is = Files.newInputStream(path)) {
			byte[] buffer = new byte[BUFFER_SIZE];
			while (true) {
				int n = is.read(buffer);
				if (n < 1) break;
				visitor.accept(buffer, n);
			}
		} catch (IOException e) {
			throw new IllegalArgumentException("File cannot be opened!");
		}
	}

	/**
	 * Encrypts the given file {@code from} and writes encrypted data to the
	 * specified file {@code to} using the given {@code Cipher} object.
	 * 
	 * @param from
	 *        file to be encrypted
	 * @param to
	 *        file with encrypted data
	 * @param cipher
	 *        cipher object used for encryption
	 * @throws IllegalBlockSizeException if the block size is not legal
	 * @throws BadPaddingException if the specified padding was not legal
	 * @throws IOException if there was a problem while reading or writing to the
	 * 		   file
	 * @throws IllegalArgumentException if the given file {@code from} does not exist
	 * 	       or cannot be read
	 * @throws NullPointerException if the given paths or Cipher are {@code null}
	 */
	private static void encryptAndWrite(Path from, Path to, Cipher cipher) 
			throws IllegalBlockSizeException, BadPaddingException, IOException {
		Objects.requireNonNull(to, "Given path 'to' cannot be null!");
		Objects.requireNonNull(cipher, "Given Cipher object cannot be null!");
		try (OutputStream os = Files.newOutputStream(to)) {
			visitFile(from, (buffer, n) -> os.write(cipher.update(buffer, 0, n)));
			os.write(cipher.doFinal());
		}
	}

	/**
	 * Calculates and returns message digest of the given file using sha-256 algorithm.
	 * 
	 * @param path
	 *        file which digest is to be calculated
	 * @return message digest of the given file using sha-256 algorithm
	 * @throws NoSuchAlgorithmException if the algorithm is not valid
	 * @throws NullPointerException if the given path is {@code null}
	 */
	private static String getSha(Path path) throws NoSuchAlgorithmException {
		var visitor = new Visitor() {
			
			private MessageDigest sha = MessageDigest.getInstance(ALGORITHM);
			
			@Override
			public void accept(byte[] buffer, int n) throws IOException {
				sha.update(buffer, 0, n);
			}
			
		};
		visitFile(path, visitor);
		return Util.byteToHex(visitor.sha.digest());
	}
	
}
