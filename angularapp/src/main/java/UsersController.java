import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sql2o.Connection;
import org.sql2o.Sql2oException;

import spark.Route;

public class UsersController {

	private static Logger logger = LogManager.getLogger("UsersController");
	
	public static List<String> getUsersNamesFromDb() {
		
		try {
			Connection conn = DatabaseController.sql2o.open();
			List<String> users = conn.createQuery("select * from test").executeAndFetch(String.class);
			conn.close();
	        return users;
		}
		catch (Sql2oException ex) {
			logger.error("SQLException: " + ex.getMessage());
			logger.error("SQLStackTrace: " + ex.getStackTrace());
		}
		
        return new ArrayList<String>();
    }
	
	public static Route onGetUsers = (request, response) -> {
        response.status(200);
        response.type("application/json");
        return Utils.mapper.writeValueAsString(UsersController.getUsersNamesFromDb());
    };
}
