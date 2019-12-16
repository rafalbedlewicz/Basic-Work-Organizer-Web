import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {
	static ObjectMapper mapper = new ObjectMapper();
	
	public static java.sql.Date paramDateStringToDbDate(String paramDate) {
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		java.util.Date javaDate;
		java.sql.Date dbDate;
		try {
			javaDate = formatter.parse(paramDate);
			dbDate = new java.sql.Date(javaDate.getTime());
		} catch (ParseException e) {
			dbDate = new java.sql.Date(0);
		}
		
		return dbDate;
	}
}
