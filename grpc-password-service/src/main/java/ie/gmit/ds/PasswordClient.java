package ie.gmit.ds;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

public class PasswordClient {

	// Instance variables
	private static final Logger logger =
            Logger.getLogger(PasswordClient.class.getName());
    private final ManagedChannel channel;
    private final PasswordServiceGrpc.PasswordServiceStub asyncPasswordService;
    private final PasswordServiceGrpc.PasswordServiceBlockingStub syncPasswordService;
    
    public ByteString hashedPassword;
    public ByteString salt;
    
    public byte[] hashedPwBArray;
    public byte[] saltBArray;
    
    public String hashedString, saltString;
    
    public User userLocal;

    // Constructor
    public PasswordClient(String host, int port) {
        channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        syncPasswordService = PasswordServiceGrpc.newBlockingStub(channel);
        asyncPasswordService = PasswordServiceGrpc.newStub(channel);
    }
    
    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
    
    // Takes in a user object and hashes the users password
    public User makePassword(User user) {
		StreamObserver<PasswordCreateResponse> responseObserver = new StreamObserver<PasswordCreateResponse>() {
			
			@Override
			public void onNext(PasswordCreateResponse value) {
				// Take in the ByteStrings
				hashedPassword = value.getHashedPassword();
				salt = value.getSalt();
				
				// Convert them to byte[]'s
				hashedPwBArray = hashedPassword.toByteArray();
				saltBArray = salt.toByteArray();
				
				// Convert to String
				hashedString = new String(hashedPwBArray);
				saltString = new String(saltBArray);
				
			}

			@Override
			public void onError(Throwable t) {
			}

			@Override
			public void onCompleted() {
			}


		};
		
		// Generate the request object.
		PasswordCreateRequest request = PasswordCreateRequest.newBuilder().setUserId(user.getUserId()).setPassword(user.getPassword()).build();
		// Call the asyncPasswordService's hash method.
		asyncPasswordService.hash(request, responseObserver);
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Create a new user with the hashed password and salt.
		userLocal = new User(user.getUserId(), user.getUserName(), user.getEmail(), hashedString.toString(), saltString.toString());
		// Return that user.
		return userLocal;
		
	}
    
    // Used to validate a password with an already hashedPassword
	public boolean validate(String password, String hashedPassword, String salt) {
		
		// Get the hashed password from the user that you are trying to login to.
		// Make it into a ByteString.
		byte[] b = hashedPassword.getBytes();
		ByteString hashPwBs = ByteString.copyFrom(b);
		
		// Do the same with the User's salt.
		byte[] s = salt.getBytes();
		ByteString saltPwBs = ByteString.copyFrom(s);
		
		// Generate the request object.
		// Send it the User's hashed password and salt.
		// Also send it the password used in the login URL.
		PasswordValidateRequest request = PasswordValidateRequest.newBuilder().setHashedPassword(hashPwBs).setPassword(password).setSalt(saltPwBs).build();
		// Create a BoolValue to store the result of the syncPasswordService's validate method.
		BoolValue validateBoolean = syncPasswordService.validate(request);
		
		System.out.println(validateBoolean.getValue());
		
		// Return the boolean value (True/False).
		return validateBoolean.getValue();
		
	}

}
