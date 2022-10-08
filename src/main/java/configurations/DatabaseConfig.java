package configurations;

public class DatabaseConfig {
	public static final String DRIVER       = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	public static final String DATABASE_URL = "jdbc:sqlserver://localhost:1433;" +
	                                          "databaseName=enderecos;" +
	                                          "encrypt=false";
	public static final String DB_PASSWORD  = "123456";
	public static final String DB_USER      = "sa";
}
