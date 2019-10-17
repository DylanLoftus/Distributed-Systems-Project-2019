package ie.gmit.ds;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Logger;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.google.protobuf.BoolValue;

import io.grpc.stub.StreamObserver;

public class PasswordServiceImpl extends PasswordServiceGrpc.PasswordServiceImplBase  {
	
	private ArrayList<PasswordCreateRequest> passwordData;
	private static final int ITERATIONS = 10000;
	private static final int KEY_LENGTH = 256;
	private static final Logger logger =
            Logger.getLogger(PasswordServiceImpl.class.getName());
	private static final Random RANDOM = new SecureRandom();
	
	public PasswordServiceImpl() {
		passwordData = new ArrayList<>();
    }

	@Override
	public void hash(PasswordCreateRequest request, StreamObserver<PasswordCreateResponse> responseObserver) {
		passwordData.add(request);
        logger.info("Added new item: " + request);
    
	}
	
	
	
	@Override
	public void validate(PasswordValidateRequest request, StreamObserver<BoolValue> responseObserver) {
		super.validate(request, responseObserver);
	}

	public static byte[] makeSalt() {
        byte[] salt = new byte[32];
        RANDOM.nextBytes(salt);
        return salt;
    }
	
	public static String makeRandomPassword(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int c = RANDOM.nextInt(62);
            if (c <= 9) {
                sb.append(String.valueOf(c));
            } else if (c < 36) {
                sb.append((char) ('a' + c - 10));
            } else {
                sb.append((char) ('A' + c - 36));
            }
        }
        return sb.toString();
    }
	
	public static byte[] hashPass(char[] password, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }
	
	public static void main(String[] args) {
		String randomPass = makeRandomPassword(10);
        byte[] salt = makeSalt();
        char[] passArray = randomPass.toCharArray();
        
        byte[] pwdHash = hashPass(passArray, salt);
        
        System.out.println(randomPass);
        
        System.out.println(Arrays.toString(salt));
        
        System.out.println(Arrays.toString(pwdHash));
	}
	
}
