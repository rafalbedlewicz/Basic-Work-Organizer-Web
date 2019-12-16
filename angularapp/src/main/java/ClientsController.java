import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;
import org.sql2o.Connection;
import org.sql2o.Sql2oException;

import com.fasterxml.jackson.core.type.TypeReference;

import spark.Route;

public class ClientsController {
	
	private static Logger logger = LogManager.getLogger("ClientsController");
	
	public static DatabaseClientsSelectData getClientsNamesFromDb() {	
		try {
			Connection conn = DatabaseController.sql2o.open();
			List<Client> clients = conn.createQuery("select * from clients").executeAndFetch(Client.class);
			conn.close();
			
	        return new DatabaseClientsSelectData(clients);
		}
		catch (Sql2oException ex) {
			logger.error("SQLException: " + ex.getMessage());
			logger.error("SQLStackTrace: " + ex.getStackTrace());
		}
		
        return new DatabaseClientsSelectData();
    }
	
	public static long saveClientToDb(Map<String, String> requestParams) {
		
		String insertSql = 
				"insert into clients(name, lastname, phone, more, unwanted) " +
				"values (:nameParam, :lastnameParam, :phoneParam, :moreParam, :unwantedParam)";
		
		try {
			Connection conn = DatabaseController.sql2o.open();
			
			long key = conn.createQuery(insertSql)
		    	.addParameter("nameParam", requestParams.getOrDefault("name", ""))
		    	.addParameter("lastnameParam", requestParams.getOrDefault("lastname", ""))
		    	.addParameter("phoneParam", requestParams.getOrDefault("phone", ""))
		    	.addParameter("moreParam", requestParams.getOrDefault("more", ""))
		    	.addParameter("unwantedParam", requestParams.getOrDefault("unwanted", "0").equals("true") ? "1" : "0")
		    	.executeUpdate().getKey(Long.class);
			
			conn.close();
	        return key;
	        
		}
		catch (Sql2oException ex) {
			logger.error("SQLException: " + ex.getMessage());
			logger.error("SQLStackTrace: " + ex.getStackTrace());
		}
		
        return -1;
    }
	
	public static boolean deleteClientFromDb(String clientId)  {
		
		String deleteSql = "delete from clients where id = " + clientId;
		
		Connection conn = null;
		
		try {
			  
			conn = DatabaseController.sql2o.open();
			conn.createQuery(deleteSql).executeUpdate();
			conn.close();
				  
			return true;
		}
		catch (Sql2oException ex) {
			
			if(conn != null) {
				conn.close();
			}
			
			logger.error("SQLException in deleteClientFromDb: " + ex.getMessage());
			logger.error("SQLStackTrace: " + ex.getStackTrace());
		}
			
	    return false;
	}	  
	
	public static boolean updateClientInDb(String clientId, Map<String, String> requestParams)  {
		
		String updateSql = "update clients set";
		boolean anyValueAdded = false;
		String currentKey = "";
		String currentValue = "";
		
		for (Map.Entry<String, String> entry : requestParams.entrySet()) {
			
			currentKey = entry.getKey();
			currentValue = entry.getValue();
			
		    if(currentKey.equals("name") || currentKey.equals("lastname") || currentKey.equals("phone") || currentKey.equals("more") || currentKey.equals("unwanted")) {
		    	updateSql += (anyValueAdded ? ", " : " ") + currentKey + " = \"" + currentValue + "\"";
		    	anyValueAdded = true;
		    }
		}	
		
		updateSql += " where id = " + clientId;
		
		Connection conn = null;

		try {
			
			conn = DatabaseController.sql2o.open();
		    conn.createQuery(updateSql).executeUpdate();
		    
		    return true;
		}
		catch (Sql2oException ex) {
			
			if(conn != null) {
				conn.close();
			}
			
			logger.error("SQLException in editClientFromDb: " + ex.getMessage());
			logger.error("SQLStackTrace: " + ex.getStackTrace());
		}
			
	    return false;
	}	  
	
	public static Route onGetClients = (request, response) -> {
		
		DatabaseClientsSelectData databaseClientsSelectData = ClientsController.getClientsNamesFromDb();
		
		if(databaseClientsSelectData.isSelectingSuccessful()) {
			response.status(200);
	        response.type("application/json");
	        return Utils.mapper.writeValueAsString(databaseClientsSelectData.getClientsList());
		}
		else {
			response.status(500);
	        response.type("application/json");
	        return Utils.mapper.writeValueAsString(new ResponseError("Błąd pobierania pracowników"));
		}
    };
    
    public static Route onPostClients = (request, response) -> {
    	
    	response.type("application/json");
    	long newlyAddedClientId = saveClientToDb(Utils.mapper.readValue(request.body(), new TypeReference<Map<String,String>>() {}));
    	
    	if(newlyAddedClientId != -1) {
    		response.status(200);
    		return newlyAddedClientId;
    	}
    	else {
    		response.status(500);
    		return Utils.mapper.writeValueAsString(new ResponseError("Błąd dodawania klienta"));
    	}
    };
    
	public static Route onDeleteClients = (request, response) -> {

		response.type("application/json");

    	if(ClientsController.deleteClientFromDb(request.params(":id"))) {
    		response.status(200);
    		return true;
    	}
    	else {
    		response.status(500);
    		return false;
    	}
    };
    
    public static Route onEditClients = (request, response) -> {

		response.type("application/json");

    	if(ClientsController.updateClientInDb(request.params(":id"), Utils.mapper.readValue(request.body(), new TypeReference<Map<String,String>>() {}))) {
    		response.status(200);
    		return true;
    	}
    	else {
    		response.status(500);
    		return false;
    	}
    };
}
