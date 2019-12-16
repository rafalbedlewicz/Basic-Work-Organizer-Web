import java.util.List;

public class DatabaseEventsSelectData {

	private boolean selectingSuccessful;
	private List<Event> eventsList;

	public DatabaseEventsSelectData() {
		selectingSuccessful = false;
	}
	
	public DatabaseEventsSelectData(List<Event> eventsList) {
		this.eventsList = eventsList;
		selectingSuccessful = true;
	}
	
	public boolean isSelectingSuccessful() {
		return selectingSuccessful;
	}
	
	public List<Event> getEventsList() {
		return eventsList;
	}
}
