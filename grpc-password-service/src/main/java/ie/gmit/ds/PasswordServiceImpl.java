package ie.gmit.ds;

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
		try {
            
            //logger.info("Passwords are correct");
            responseObserver.onNext(BoolValue.newBuilder().setValue(true).build());
        } catch (RuntimeException ex) {
            responseObserver.onNext(BoolValue.newBuilder().setValue(false).build());
        }
        responseObserver.onCompleted();
	}
	
	
}
