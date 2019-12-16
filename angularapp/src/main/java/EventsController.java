import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.text.*;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;
import org.sql2o.Connection;
import org.sql2o.Sql2oException;

import com.fasterxml.jackson.core.type.TypeReference;

import spark.Route;

public class EventsController {
	
	private static Logger logger = LogManager.getLogger("EventsController");
	
	public static DatabaseEventsSelectData getEventsNamesFromDb() {
		
		try {
			Connection conn = DatabaseController.sql2o.open();
			List<Event> events = conn.createQuery("select * from events").executeAndFetch(Event.class);
			conn.close();
			
	        return new DatabaseEventsSelectData(events);
		}
		catch (Sql2oException ex) {
			logger.error("SQLException in getEventsNamesFromDb: " + ex.getMessage());
			logger.error("SQLStackTrace: " + ex.getStackTrace());
		}
		
        return new DatabaseEventsSelectData();
    }
	
	public static long saveEventToDb(Map<String, String> requestParams) {
		
		String insertSql = 
				"insert into events(title, startDate, endDate, description, employee, client) " +
				"values (:titleParam, :startDateParam, :endDateParam, :descriptionParam, :employeeParam, :clientParam)";
		
		try {
			Connection conn = DatabaseController.sql2o.open();
			
			long key = conn.createQuery(insertSql)
		    	.addParameter("titleParam", requestParams.getOrDefault("title", ""))
		    	.addParameter("startDateParam", Utils.paramDateStringToDbDate(requestParams.getOrDefault("startsAt", "")))
		    	.addParameter("endDateParam", Utils.paramDateStringToDbDate(requestParams.getOrDefault("endsAt", "")))
		    	.addParameter("descriptionParam", requestParams.getOrDefault("description", ""))
		    	.addParameter("employeeParam", requestParams.getOrDefault("employeeId", ""))
		    	.addParameter("clientParam", requestParams.getOrDefault("clientId", ""))
		    	.executeUpdate().getKey(Long.class);
			
			conn.close();
	        return key;
	        
		}
		catch (Sql2oException ex) {
			logger.error("SQLException in saveEventToDb: " + ex.getMessage());
			logger.error("SQLStackTrace: " + ex.getStackTrace());
		}
		
        return -1;
    }
	
	public static boolean deleteEventFromDb(String EventId)  {
		
		String deleteSql = "delete from Events where id = " + EventId;
		
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
			
			logger.error("SQLException in deleteEventFromDb: " + ex.getMessage());
			logger.error("SQLStackTrace: " + ex.getStackTrace());
		}
			
	    return false;
	}	  
	
	public static boolean updateEventInDb(String EventId, Map<String, String> requestParams)  {
		
		String updateSql = "update events set";
		boolean anyValueAdded = false;
		String currentKey = "";
		String currentValue = "";
		
		for (Map.Entry<String, String> entry : requestParams.entrySet()) {
			
			currentKey = entry.getKey();
			currentValue = entry.getValue();
			
		    if(currentKey.equals("title") || currentKey.equals("startDate") || currentKey.equals("endDate") || currentKey.equals("description") || currentKey.equals("employee") || currentKey.equals("client")) {
		    	updateSql += (anyValueAdded ? ", " : " ") + currentKey + " = \"" + currentValue + "\"";
		    	anyValueAdded = true;
		    }
		}	
		
		updateSql += " where id = " + EventId;
		
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
			
			logger.error("SQLException in editEventFromDb: " + ex.getMessage());
			logger.error("SQLStackTrace: " + ex.getStackTrace());
		}
			
	    return false;
	}	  
	
	public static Route onGetEvents = (request, response) -> {
		
		DatabaseEventsSelectData databaseEventsSelectData = EventsController.getEventsNamesFromDb();
		
		if(databaseEventsSelectData.isSelectingSuccessful()) {
			response.status(200);
	        response.type("application/json");
	        return Utils.mapper.writeValueAsString(databaseEventsSelectData.getEventsList());
		}
		else {
			response.status(500);
	        response.type("application/json");
	        return Utils.mapper.writeValueAsString(new ResponseError("B³¹d pobierania wydarzeñ"));
		}
    };
    
    public static Route onPostEvents = (request, response) -> {
    	
    	response.type("application/json");
    	long newlyAddedEventId = saveEventToDb(Utils.mapper.readValue(request.body(), new TypeReference<Map<String,String>>() {}));
    	
    	if(newlyAddedEventId != -1) {
    		response.status(200);
    		return newlyAddedEventId;
    	}
    	else {
    		response.status(500);
    		return Utils.mapper.writeValueAsString(new ResponseError("B³¹d dodawania wydarzenia"));
    	}
    };
    
	public static Route onDeleteEvents = (request, response) -> {

		response.type("application/json");

    	if(EventsController.deleteEventFromDb(request.params(":id"))) {
    		response.status(200);
    		return true;
    	}
    	else {
    		response.status(500);
    		return false;
    	}
    };
    
    public static Route onEditEvents = (request, response) -> {

		response.type("application/json");

    	if(EventsController.updateEventInDb(request.params(":id"), Utils.mapper.readValue(request.body(), new TypeReference<Map<String,String>>() {}))) {
    		response.status(200);
    		return true;
    	}
    	else {
    		response.status(500);
    		return false;    		
    	}
    };
}
