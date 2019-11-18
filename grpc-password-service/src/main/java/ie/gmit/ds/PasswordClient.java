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
    
    public ByteString hashedPassword;
    public ByteString salt;
    
    public byte[] hashedPwBArray;
    public byte[] saltBArray;
    
    public String hashedString, saltString;
    
    public User userLocal;
    
    public boolean validateBoolean;
    // Takes in a user object and hashes the users password
    
    public User makePassword(User user) {
		StreamObserver<PasswordCreateResponse> responseObserver = new StreamObserver<PasswordCreateResponse>() {
			
			@Override
			public void onNext(PasswordCreateResponse value) {
//				System.out.println(value.getHashedPassword().toByteArray().toString());
//				System.out.println(value.getUserId());
//				System.out.println(value.getSalt().toByteArray());
				hashedPassword = value.getHashedPassword();
				salt = value.getSalt();
				
				hashedPwBArray = hashedPassword.toByteArray();
				saltBArray = salt.toByteArray();
				
				hashedString = new String(hashedPwBArray);
				saltString = new String(saltBArray);
				
				System.out.println(hashedString.toString());
				System.out.println(saltString.toString());
				
			}

			@Override
			public void onError(Throwable t) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onCompleted() {
				
			}


		};
		
		
		System.out.println(user.getUserId());
		System.out.println(user.getPassword());
		
		PasswordCreateRequest request = PasswordCreateRequest.newBuilder().setUserId(user.getUserId()).setPassword(user.getPassword()).build();
		asyncPasswordService.hash(request, responseObserver);
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Called async");
		userLocal = new User(user.getUserId(), user.getUserName(), user.getEmail(), hashedString.toString(), saltString.toString());
		System.out.println("made local user");
		return userLocal;
		
	}
    
    // Used to validate a password with an already hashedPassword
    
	public Boolean validate(Login login, User user) {

		StreamObserver<BoolValue> responseObserver = new StreamObserver<BoolValue>() {

			@Override
			public void onNext(BoolValue value) {
				System.out.println(value.getValue());
				validateBoolean = value.getValue();
			}

			@Override
			public void onError(Throwable t) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onCompleted() {

			}


		};
		
		System.out.println("HELLO I'M VALIDATE");
		
		String hashValidate = user.getHash();
		byte[] b = hashValidate.getBytes();
		ByteString hashPwBs = ByteString.copyFrom(b);
		
		String saltValidate = user.getSalt();
		byte[] s = saltValidate.getBytes();
		ByteString saltPwBs = ByteString.copyFrom(s);
		
		PasswordValidateRequest request = PasswordValidateRequest.newBuilder().setHashedPassword(hashPwBs).setPassword(login.getPassword()).setSalt(saltPwBs).build();
		System.out.println("CREATED REQUEST");
		asyncPasswordService.validate(request, responseObserver);
		System.out.println("SENT REQUEST");
		
		return validateBoolean;
		
	}

}
