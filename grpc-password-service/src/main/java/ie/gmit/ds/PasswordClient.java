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
    
    // Takes in a user object and hashes the users password
    
    public void makePassword(User user) {
		StreamObserver<PasswordCreateResponse> responseObserver = new StreamObserver<PasswordCreateResponse>() {
			
			@Override
			public void onNext(PasswordCreateResponse value) {
				System.out.println(value.getHashedPassword().toByteArray());
				System.out.println(value.getUserId());
				System.out.println(value.getSalt().toByteArray());
				hashedPassword = value.getHashedPassword();
				salt = value.getSalt();
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
		
	}
    
    // Used to validate a password with an already hashedPassword
    
	private void validate(ByteString hashedPassword, ByteString salt) {

		StreamObserver<BoolValue> responseObserver = new StreamObserver<BoolValue>() {

			@Override
			public void onNext(BoolValue value) {
				System.out.println(value.getValue());
			}

			@Override
			public void onError(Throwable t) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onCompleted() {

			}


		};
		
		System.out.println("I'M IN");
		
		String password = "lala";
		PasswordValidateRequest request = PasswordValidateRequest.newBuilder().setHashedPassword(hashedPassword).setPassword(password).setSalt(salt).build();
		asyncPasswordService.validate(request, responseObserver);
		
	}
	
    public static void main(String[] args) throws Exception {
    	Scanner console = new Scanner(System.in);
    	
    	UserResource resource = new UserResource();
    	
    	Boolean loop = true;
    	
    	System.out.println("WELCOME TO THE USER SERVICE");
    	
    	System.out.println("What operation would you like to do?");
    	
    	System.out.println("Press 1 to create a new user/2 to get info on a user/3 to update a user/4 to delete a user/5 to liset all users/6 to Login/7 to Exit");
    	int input = console.nextInt();
    	
    	while(loop) {
    		
    		switch(input) {
	    		case 1:
	    			// Add user create implementation
	    			System.out.println("Enter user ID");
	    			int userId = console.nextInt();
	    			System.out.println("Enter user name");
	    			String userName = console.next();
	    			System.out.println("Enter user email");
	    			String userEmail = console.next();
	    			System.out.println("Enter user password");
	    			String password = console.next();
	    			
	    			// Hopefully this works.
	    			User createUser = new User(userId, userName, userEmail, password);
	    			resource.addUser(createUser);
	    
	    			break;
	    		case 2:
	    			// Add user info implementation
	    			break;
	    		case 3:
	    			// Add user update implementation
	    			break;
	    		case 4:
	    			// Add user delete implementation
	    			break;
	    		case 5:
	    			// Add list all users implementation
	    			break;
	    		case 6:
	    			// Add login implementation
	    			break;
	    		case 7:
	    			loop = false;
	    			break;
    		}
    		
    		System.out.println("Press 1 to create a new user/2 to get info on a user/3 to update a user/4 to delete a user/5 to liset all users/6 to Login/7 to Exit");
        	input = console.nextInt();
    	}
    	
    	/*
        try {
            client.makePassword(password, userId);
        } finally {
            // Don't stop process, keep alive to receive async response
            Thread.currentThread().join();
        }
        */
    }

}
