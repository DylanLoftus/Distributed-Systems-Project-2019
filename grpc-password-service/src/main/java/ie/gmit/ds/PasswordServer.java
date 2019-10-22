package ie.gmit.ds;

import java.io.IOException;
import java.util.logging.Logger;

import io.grpc.Server;
import io.grpc.ServerBuilder;

// This class implements a server that talks to the client.
public class PasswordServer {
	
	// Setup a server.
	private Server grpcServer;
    private static final Logger logger = Logger.getLogger(PasswordServer.class.getName());
    // A port to listen on.
    private static final int PORT = 50551;
    
    // Build the server that runs with the Password service and listens on port 50551.
    private void start() throws IOException {
        grpcServer = ServerBuilder.forPort(PORT)
                .addService(new PasswordServiceImpl())
                .build()
                .start();
        logger.info("Server started, listening on " + PORT);

    }

    // Method to shutdown the server.
    private void stop() {
        if (grpcServer != null) {
            grpcServer.shutdown();
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (grpcServer != null) {
            grpcServer.awaitTermination();
        }
    }
    
    // Create a server object and run it.
    public static void main(String[] args) throws IOException, InterruptedException {
        final PasswordServer passServer = new PasswordServer();
        passServer.start();
        passServer.blockUntilShutdown();
    }
	
}
