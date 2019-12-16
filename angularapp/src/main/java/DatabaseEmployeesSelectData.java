import java.util.List;

public class DatabaseEmployeesSelectData {

	private boolean selectingSuccessful;
	private List<Employee> employeesList;

	public DatabaseEmployeesSelectData() {
		selectingSuccessful = false;
	}
	
	public DatabaseEmployeesSelectData(List<Employee> employeesList) {
		this.employeesList = employeesList;
		selectingSuccessful = true;
	}
	
	public boolean isSelectingSuccessful() {
		return selectingSuccessful;
	}
	
	public List<Employee> getEmployeesList() {
		return employeesList;
	}
}
