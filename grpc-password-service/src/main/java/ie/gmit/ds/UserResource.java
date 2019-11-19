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

@Path("/user")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class UserResource {

	private HashMap<Integer, User> userMap = new HashMap<>();
	private boolean loginTrue;

	PasswordClient client = new PasswordClient("localhost", 50551);
	
	public UserResource() {
		User testUser = new User(1, "Lala", "lala@gmail.com", "super");
		userMap.put(testUser.getUserId(), testUser);
	}
	
	@GET
	public List<User> getUsers() {
	    return new ArrayList<User>(userMap.values());
	}
	
	@POST
	public Response addUser(User user) {
		
		System.out.println(user.getUserId());
		System.out.println(user.getUserName());
		System.out.println(user.getEmail());
		System.out.println(user.getPassword());
		
		int userId = user.getUserId();
		
		if(userId == 0) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		User newUser = client.makePassword(user);
		System.out.println("After client.makepassword");
		userMap.put(newUser.getUserId(), newUser);
		return Response.ok(user).build();
	}
	
	@Path("/{userId}")
	@GET
	public User getUserById(@PathParam("userId") int id) {
		return userMap.get(id);
	}
	
	@Path("/{userId}")
	@PUT
	public Response changeUser(User user, @PathParam("userId") int id) {
		if(user == null || userMap.get(id) == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		else {
			userMap.remove(id);
			User newUserPut = client.makePassword(user);
			userMap.put(newUserPut.getUserId(), newUserPut);
			return Response.ok(user).build();
		}
		
	}
	
	@Path("/{userId}")
	@DELETE
	public Response deleteUser(@PathParam("userId") int id){
		if(userMap.get(id) == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		else {
			userMap.remove(id);
			return Response.ok().build();
		}
	}
	
	@Path("/{userId}/login/{password}")
	@POST
	public Response loginUser(@PathParam("userId") int id, @PathParam("password") String password){
		
		User checkUser = userMap.get(id);
		
		loginTrue = client.validate(password, checkUser);
		
		System.out.println("LOGINTRUE: " + loginTrue);
		
		if(loginTrue) {
			return Response.ok().build();
		}
		else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
}
