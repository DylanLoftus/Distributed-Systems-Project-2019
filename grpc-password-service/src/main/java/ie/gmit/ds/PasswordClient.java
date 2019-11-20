package ie.gmit.ds;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
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
	private static final Logger logger = Logger.getLogger(PasswordClient.class.getName());
	private final ManagedChannel channel;
	private final PasswordServiceGrpc.PasswordServiceStub asyncPasswordService;
	private final PasswordServiceGrpc.PasswordServiceBlockingStub syncPasswordService;

	public ByteString hashedPassword;
	public ByteString salt;

	public ByteString getSalt() {
		return salt;
	}

	public ByteString getHashedPassword() {
		return hashedPassword;
	}

	// Constructor
	public PasswordClient(String host, int port) {
		channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
		syncPasswordService = PasswordServiceGrpc.newBlockingStub(channel);
		asyncPasswordService = PasswordServiceGrpc.newStub(channel);
	}

	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}

	// Takes in a user object and hashes the users password
	public void makePassword(int userId, String passsword) {
		StreamObserver<PasswordCreateResponse> responseObserver = new StreamObserver<PasswordCreateResponse>() {

			@Override
			public void onNext(PasswordCreateResponse value) {
				// Take in the ByteStrings
				hashedPassword = value.getHashedPassword();
				salt = value.getSalt();

			}

			@Override
			public void onError(Throwable t) {
			}

			@Override
			public void onCompleted() {
			}

		};
		
		// Generate the request object.
		PasswordCreateRequest request = PasswordCreateRequest.newBuilder().setUserId(userId).setPassword(passsword).build();
		// Call the asyncPasswordService's hash method.
		asyncPasswordService.hash(request, responseObserver);

		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	// Used to validate a password with an already hashedPassword
	public boolean validate(String password, ByteString hashedPassword, ByteString salt) {

		PasswordValidateRequest request = PasswordValidateRequest.newBuilder().setHashedPassword(hashedPassword).setPassword(password).setSalt(salt).build();
	
		BoolValue boolValue = syncPasswordService.validate(request);
		System.out.println(boolValue.getValue());
			
		return boolValue.getValue();
		
	}

}
