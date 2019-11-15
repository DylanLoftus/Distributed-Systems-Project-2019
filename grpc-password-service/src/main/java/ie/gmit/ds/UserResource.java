package ie.gmit.ds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

	private HashMap<Integer, User> userMap = new HashMap<>();

	public UserResource() {
		User testUser = new User(1, "Lala", "lala@gmail.com", "super");
		User testUser2 = new User(2, "Lala2", "lala2@gmail.com", "super2");
		User testUser3 = new User(3, "Lala3", "lala3@gmail.com", "super3");
		User testUser4 = new User(4, "Lala4", "lala4@gmail.com", "super4");
		userMap.put(testUser.getUserId(), testUser);
		userMap.put(testUser2.getUserId(), testUser2);
		userMap.put(testUser3.getUserId(), testUser3);
		userMap.put(testUser4.getUserId(), testUser4);
	}
	
	@GET
	public List<User> getUsers() {
	    return new ArrayList<User>(userMap.values());
	}
	
	@Path("/{userId}")
	@GET
	public User getUserById(@PathParam("userId") int id) {
		
		return userMap.get(id);
	}
	
}
