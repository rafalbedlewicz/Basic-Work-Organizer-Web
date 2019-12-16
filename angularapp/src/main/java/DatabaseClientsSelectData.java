import java.util.List;

public class DatabaseClientsSelectData {

	private boolean selectingSuccessful;
	private List<Client> clientsList;

	public DatabaseClientsSelectData() {
		selectingSuccessful = false;
	}
	
	public DatabaseClientsSelectData(List<Client> clientsList) {
		this.clientsList = clientsList;
		selectingSuccessful = true;
	}
	
	public boolean isSelectingSuccessful() {
		return selectingSuccessful;
	}
	
	public List<Client> getClientsList() {
		return clientsList;
	}
}
