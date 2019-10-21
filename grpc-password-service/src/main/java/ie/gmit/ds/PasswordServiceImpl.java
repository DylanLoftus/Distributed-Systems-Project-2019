package ie.gmit.ds;

import java.util.Arrays;
import java.util.logging.Logger;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;

import io.grpc.stub.StreamObserver;

public class PasswordServiceImpl extends PasswordServiceGrpc.PasswordServiceImplBase  {
	
	
	private static final Logger logger =
            Logger.getLogger(PasswordServiceImpl.class.getName());
	
	public PasswordServiceImpl() {
		
    }

	@Override
	public void hash(PasswordCreateRequest request, StreamObserver<PasswordCreateResponse> responseObserver) {
		System.out.println("PASSWORDCREATE");
		byte[] salt = Passwords.getNextSalt();
		String password = request.getPassword();
		byte[] hashedPassword = Passwords.hash(password.toCharArray(), salt);
		
		ByteString bsSalt = ByteString.copyFrom(salt);
		ByteString bsHashPassword = ByteString.copyFrom(hashedPassword);
		
		int userID = request.getUserId();
		
		PasswordCreateResponse reply = PasswordCreateResponse.newBuilder().setHashedPassword(bsHashPassword).setUserId(userID).setSalt(bsSalt).build();
		responseObserver.onNext(reply);
        responseObserver.onCompleted();
	}
	
	@Override
	public void validate(PasswordValidateRequest request, StreamObserver<BoolValue> responseObserver) {

        System.out.println("SERVER VALIDATION");
		byte[] userHashedPassword = Passwords.hash(request.getPassword().toCharArray(), request.getSalt().toByteArray()); 
		
		ByteString bsUserHashedPassword = ByteString.copyFrom(userHashedPassword);
		
		if(Arrays.equals(request.getHashedPassword().toByteArray(), bsUserHashedPassword.toByteArray())) {
			System.out.println("TRUE");
			responseObserver.onNext(BoolValue.newBuilder().setValue(true).build());
		}else {
			System.out.println("FALSE");
			responseObserver.onNext(BoolValue.newBuilder().setValue(false).build());
		}

        responseObserver.onCompleted();
	}
	
	
}
