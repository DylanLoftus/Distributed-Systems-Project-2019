package ie.gmit.ds;

import java.security.SecureRandom;
import java.util.Random;

import io.grpc.stub.StreamObserver;

public class PasswordServiceImpl extends PasswordServiceGrpc.PasswordServiceImplBase  {
	
	private static final Random RANDOM = new SecureRandom();

	@Override
	public void hash(PasswordCreateRequest request, StreamObserver<PasswordCreateResponse> responseObserver) {
		
	}

	@Override
	public void validate(PasswordValidateRequest request, StreamObserver<PasswordValidateResponse> responseObserver) {
		
		
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
	
	public static void main(String[] args) {
		String randomPass;
		
		PasswordServiceImpl ps = new PasswordServiceImpl();
		
		randomPass = PasswordServiceImpl.makeRandomPassword(5);
		
		ps.hash(randomPass, responseObserver);
		
	}
}
