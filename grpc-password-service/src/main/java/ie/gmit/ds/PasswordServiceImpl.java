package ie.gmit.ds;

import java.util.Arrays;
import java.util.logging.Logger;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;

import io.grpc.stub.StreamObserver;

// This class implements the Password Service.
// This class also uses methods from the Passwords class.
// https://gist.github.com/john-french/9c94d88f34b2a4ccbe55af6afb083674
public class PasswordServiceImpl extends PasswordServiceGrpc.PasswordServiceImplBase  {
	
	public PasswordServiceImpl() {
		
    }

	// Overridden and implemented methods.
	
	@Override
	public void hash(PasswordCreateRequest request, StreamObserver<PasswordCreateResponse> responseObserver) {
		// Create a byte array use the passwords class to make salt.
		byte[] salt = Passwords.getNextSalt();
		// Using the request get the password from the user.
		String password = request.getPassword();
		// Use passwords class to hash the password the user provided.
		// Pass the user's password and the salt to the method and store in byte array.
		byte[] hashedPassword = Passwords.hash(password.toCharArray(), salt);
		
		// Convert the byte arrays to bytestrings.
		ByteString bsSalt = ByteString.copyFrom(salt);
		ByteString bsHashPassword = ByteString.copyFrom(hashedPassword);
		
		// Get the user's ID.
		int userID = request.getUserId();
		
		// Make the response sending back the hashed password, the user ID and the salt.
		PasswordCreateResponse reply = PasswordCreateResponse.newBuilder().setHashedPassword(bsHashPassword).setUserId(userID).setSalt(bsSalt).build();
		
		//Send the response and finish.
		responseObserver.onNext(reply);
        responseObserver.onCompleted();
	}
	
	@Override
	public void validate(PasswordValidateRequest request, StreamObserver<BoolValue> responseObserver) {
		
		// Take in the user's hashed password from the request and hash it using the Passwords class.
		byte[] userHashedPassword = Passwords.hash(request.getPassword().toCharArray(), request.getSalt().toByteArray()); 
		
		
		// Convert the byte array to a byte string.
		ByteString bsUserHashedPassword = ByteString.copyFrom(userHashedPassword);
		
		// If the the user's hashed password matches the newly hashed password in the request return true, otherwise return false.
		
		if(Arrays.equals(request.getHashedPassword().toByteArray(), bsUserHashedPassword.toByteArray())) {
			System.out.println("TRUE");
			responseObserver.onNext(BoolValue.newBuilder().setValue(true).build());
		}else {
			System.out.println("FALSE");
			responseObserver.onNext(BoolValue.newBuilder().setValue(false).build());
		}
		
		// Finish response.
        responseObserver.onCompleted();
	}
	
	
}
