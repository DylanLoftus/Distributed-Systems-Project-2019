package ie.gmit.ds;

import io.grpc.stub.StreamObserver;

public class PasswordServiceImpl extends PasswordServiceGrpc.PasswordServiceImplBase  {

	@Override
	public void hash(PasswordCreateRequest request, StreamObserver<PasswordCreateResponse> responseObserver) {
		
		
	}

	@Override
	public void validate(PasswordValidateRequest request, StreamObserver<PasswordValidateResponse> responseObserver) {
		
		
	}

	
	
}
