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

	private static final Logger logger =
            Logger.getLogger(PasswordClient.class.getName());
    private final ManagedChannel channel;
    private final PasswordServiceGrpc.PasswordServiceStub asyncPasswordService;
    private final PasswordServiceGrpc.PasswordServiceBlockingStub syncPasswordService;

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
    
    private void makePassword(String password, int userID) {
		StreamObserver<PasswordCreateResponse> responseObserver = new StreamObserver<PasswordCreateResponse>() {

			@Override
			public void onNext(PasswordCreateResponse value) {
				System.out.println(value.getHashedPassword().toByteArray());
				System.out.println(value.getUserId());
				System.out.println(value.getSalt().toByteArray());
				hashedPassword = value.getHashedPassword();
				salt = value.getSalt();
				System.out.println("before");
				validate(hashedPassword,salt);
				System.out.println("after");
			}

			@Override
			public void onError(Throwable t) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onCompleted() {
			}


		};
		
		
		
		PasswordCreateRequest request = PasswordCreateRequest.newBuilder().setUserId(userID).setPassword(password).build();
		asyncPasswordService.hash(request, responseObserver);
		
	}
    
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
    	
    	PasswordClient client = new PasswordClient("localhost", 50551);
        
    	System.out.println("Enter password: ");
    	String password = console.next();
    	
    	System.out.println("Enter user ID: ");
    	int userId = console.nextInt();
    	
        try {
            client.makePassword(password, userId);
        } finally {
            // Don't stop process, keep alive to receive async response
            Thread.currentThread().join();
        }
    }

}
