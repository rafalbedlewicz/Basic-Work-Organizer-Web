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

public class EmployeesController {
	
	private static Logger logger = LogManager.getLogger("EmployeesController");
	
	public static DatabaseEmployeesSelectData getEmployeesNamesFromDb() {
		
		try {
			Connection conn = DatabaseController.sql2o.open();
			List<Employee> employees = conn.createQuery("select * from employees").executeAndFetch(Employee.class);
			conn.close();
			
	        return new DatabaseEmployeesSelectData(employees);
		}
		catch (Sql2oException ex) {
			logger.error("SQLException: " + ex.getMessage());
			logger.error("SQLStackTrace: " + ex.getStackTrace());
		}
		
        return new DatabaseEmployeesSelectData();
    }
	
	public static long saveEmployeeToDb(Map<String, String> requestParams) {
		
		String insertSql = 
				"insert into employees(name, lastname, photo, phone, color) " +
				"values (:nameParam, :lastnameParam, :photoParam, :phoneParam, :colorParam)";
		
		try {
			Connection conn = DatabaseController.sql2o.open();
			
			long key = conn.createQuery(insertSql)
		    	.addParameter("nameParam", requestParams.getOrDefault("name", ""))
		    	.addParameter("lastnameParam", requestParams.getOrDefault("lastname", ""))
		    	.addParameter("photoParam", requestParams.getOrDefault("photo", ""))
		    	.addParameter("phoneParam", requestParams.getOrDefault("phone", ""))
		    	.addParameter("colorParam", requestParams.getOrDefault("color", ""))
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
	
	public static boolean deleteEmployeeFromDb(String employeeId)  {
		
		String deleteSql = "delete from employees where id = " + employeeId;
		
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
			
			logger.error("SQLException in deleteEmployeeFromDb: " + ex.getMessage());
			logger.error("SQLStackTrace: " + ex.getStackTrace());
		}
			
	    return false;
	}	  
	
public static boolean updateEmployeeInDb(String employeeId, Map<String, String> requestParams)  {
		
		String updateSql = "update employees set";
		boolean anyValueAdded = false;
		String currentKey = "";
		String currentValue = "";
		
		for (Map.Entry<String, String> entry : requestParams.entrySet()) {
			
			currentKey = entry.getKey();
			currentValue = entry.getValue();
			
		    if(currentKey.equals("name") || currentKey.equals("lastname") || currentKey.equals("photo") || currentKey.equals("phone") || currentKey.equals("color")) {
		    	updateSql += (anyValueAdded ? ", " : " ") + currentKey + " = \"" + currentValue + "\"";
		    	anyValueAdded = true;
		    }
		}	
		
		updateSql += " where id = " + employeeId;
		
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
			
			logger.error("SQLException in editEmployeeFromDb: " + ex.getMessage());
			logger.error("SQLStackTrace: " + ex.getStackTrace());
		}
			
	    return false;
	}	  
	
	public static Route onGetEmployees = (request, response) -> {
		
		DatabaseEmployeesSelectData databaseEmployeesSelectData = EmployeesController.getEmployeesNamesFromDb();
		
		if(databaseEmployeesSelectData.isSelectingSuccessful()) {
			response.status(200);
	        response.type("application/json");
	        return Utils.mapper.writeValueAsString(databaseEmployeesSelectData.getEmployeesList());
		}
		else {
			response.status(500);
	        response.type("application/json");
	        return Utils.mapper.writeValueAsString(new ResponseError("Błąd pobierania pracowników"));
		}
    };
    
    public static Route onPostEmployees = (request, response) -> {
    	
    	response.type("application/json");
    	long newlyAddedEmployeeId = saveEmployeeToDb(Utils.mapper.readValue(request.body(), new TypeReference<Map<String,String>>() {}));

    	if(newlyAddedEmployeeId != -1) {
    		response.status(200);
    		return newlyAddedEmployeeId;
    	}
    	else {
    		response.status(500);
    		return Utils.mapper.writeValueAsString(new ResponseError("Błąd dodawania pracownika"));
    	}
    };
    
	public static Route onDeleteEmployees = (request, response) -> {

		response.type("application/json");

    	if(EmployeesController.deleteEmployeeFromDb(request.params(":id"))) {
    		response.status(200);
    		return true;
    	}
    	else {
    		response.status(500);
    		return false;
    	}
    };
    
    public static Route onEditEmployees = (request, response) -> {

		response.type("application/json");

    	if(EmployeesController.updateEmployeeInDb(request.params(":id"), Utils.mapper.readValue(request.body(), new TypeReference<Map<String,String>>() {}))) {
    		response.status(200);
    		return true;
    	}
    	else {
    		response.status(500);
    		return false;
    	}
    };
}
