package ie.gmit.ds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.protobuf.BoolValue;

// This class can take in JSON and XML
// It can also send out JSON and XML
@Path("/user")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class UserResource {

	// Instance Variables
	private HashMap<Integer, User> userMap = new HashMap<>();
	private boolean loginTrue;

	// Get an instance of the client.
	PasswordClient client = new PasswordClient("localhost", 50551);
	
	public UserResource() {
		// Dummy user, to test get request.
		User testUser = new User(1, "Lala", "lala@gmail.com", "super");
		userMap.put(testUser.getUserId(), testUser);
	}
	
	// Method that returns a list of users.
	@GET
	public List<User> getUsers() {
	    return new ArrayList<User>(userMap.values());
	}
	
	// Method that adds a suer to the userMap.
	@POST
	public Response addUser(User user) {
		
		// Store userId and check to see if it's not null.
		int userId = user.getUserId();
		
		if(userId == 0) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		// Call on our GRPC service to make a password for our user.
		// Return it into a User Object.
		User newUser = client.makePassword(user);
		// Add to map.
		userMap.put(newUser.getUserId(), newUser);
		// Return 200 response.
		return Response.ok(newUser).build();
	}
	
	// Method to get info on specific user.
	@Path("/{userId}")
	@GET
	public Response getUserById(@PathParam("userId") int id) {
		// If the User doesn't exist.
		if(userMap.get(id) == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		// If the User exists return it.
		else {
			return Response.ok(userMap.get(id)).build();
		}
		
	}
	
	// Method that changes a user's info.
	@Path("/{userId}")
	@PUT
	public Response changeUser(User user, @PathParam("userId") int id) {
		// If the request is bad.
		if(user == null || userMap.get(id) == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		// Remove and add new User details and send a 200 response.
		else {
			userMap.remove(id);
			User newUserPut = client.makePassword(user);
			userMap.put(newUserPut.getUserId(), newUserPut);
			return Response.ok(newUserPut).build();
		}
		
	}
	
	// Method that removes a user from the map.
	@Path("/{userId}")
	@DELETE
	public Response deleteUser(@PathParam("userId") int id){
		// If the User doesn't exist.
		if(userMap.get(id) == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		// If the User exists delete it from the map.
		else {
			userMap.remove(id);
			return Response.ok().build();
		}
	}
	
	// Method that logs a user in.
	@Path("/{userId}/login/{password}")
	@POST
	public Response loginUser(@PathParam("userId") int id, @PathParam("password") String password){
		
		// Get the User we're trying to login.
		User checkUser = userMap.get(id);
		
		System.out.println(checkUser.getUserId());
		System.out.println(password);
		
		
		
		// Send that User and the password entered to the GRPC validate method.
		loginTrue = client.validate(password, checkUser.getHash(), checkUser.getSalt());
		
		// If the result is true, the passwords match.
		if(loginTrue) {
			return Response.ok().build();
		}
		// Passwords don't match.
		else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
}
