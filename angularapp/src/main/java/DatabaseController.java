import org.sql2o.Sql2o;

public class DatabaseController {

	static Sql2o sql2o = new Sql2o(
			"jdbc:mysql://" + 
			ServerConfigurationData.DB_HOST + ":" + 
			ServerConfigurationData.DB_PORT + "/" + 
			ServerConfigurationData.DB_NAME, 
			ServerConfigurationData.DB_USER,
			ServerConfigurationData.DB_PASSWORD
		);
}
