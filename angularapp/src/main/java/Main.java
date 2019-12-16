import static spark.Spark.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import com.fasterxml.jackson.databind.ObjectMapper;

//http://localhost:4567/

public class Main {

    public static void main(String[] args) {
    	
    	runServerInitActions();
    	
        get("/hello", (request, response) -> "Hello World " + (new Random()).nextInt(500));
        
        get("/users", UsersController.onGetUsers);
        
        get("/employees", EmployeesController.onGetEmployees); 
        
        post("/employees", EmployeesController.onPostEmployees);
        
        delete("/employees/:id", EmployeesController.onDeleteEmployees);
        
        patch("/employees/:id", EmployeesController.onEditEmployees);
        
        get("/clients", ClientsController.onGetClients); 
        
        post("/clients", ClientsController.onPostClients);
        
        delete("/clients/:id", ClientsController.onDeleteClients);
        
        patch("/clients/:id", ClientsController.onEditClients);
        
        get("/events", EventsController.onGetEvents);
        
        post("/events", EventsController.onPostEvents);
        
        delete("/events/:id", EventsController.onDeleteEvents);
        
        patch("/events/:id", EventsController.onEditEvents);
        
        get("*", (request, response) -> {
            response.status(404);
            response.type("application/json");
            return "The " + request.url() + "endpoint does not exist";
        });
    }
    
    private static void runServerInitActions() {
    	
    	ServerInitiallizationController.enableCORS(
    		ServerConfigurationData.CORS_ORIGIN, 
    		ServerConfigurationData.CORS_METHODS, 
    		ServerConfigurationData.CORS_HEADERS
    	);
    }
}