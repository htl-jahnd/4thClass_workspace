package pkgData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class Database
{

    private static String connectionString;
    private static final String USER = "d4b12";
    private static final String PWD_DB = "d4b";
    private static Database instance = null;
    private static Connection conn = null;
    private static boolean isConnectionSet = false;
    private static ArrayList<Producer> collProducers;
    private static ArrayList<Product> collProducts;

    private Database()
    {

    }

    public static Database newInstance(String connString) throws Exception // TODO könnte mit "reconnect"
									   // zusammengefasst
    // werden
    {
	if (instance == null)
	{
	    instance = new Database();
	    connectionString = connString;
	    createConnection();
	    collProducts = new ArrayList<Product>();
	    collProducers = new ArrayList<Producer>();
	}
	return instance;
    }

    public static Database reconnect(String connString) throws Exception
    {
	if (instance == null)
	{
	    instance = new Database();
	}
	connectionString = connString;
	createConnection();
	collProducts = new ArrayList<Product>();
	collProducers = new ArrayList<Producer>();
	return instance;
    }

    private static void createConnection() throws Exception
    {
	if (conn == null)
	{
	    DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
	}
	DriverManager.setLoginTimeout(3);
	conn = DriverManager.getConnection(connectionString, USER, PWD_DB);
	conn.setAutoCommit(false);
	isConnectionSet = true;

    }

    public void commit() throws Exception
    {
	conn.commit();
    }

    public void rollback() throws Exception
    {
	conn.rollback();
    }

    public void closeConnection() throws Exception
    {
	conn.close();
    }

    public static boolean isConnectionSet()
    {
	return isConnectionSet;
    }

    public static ArrayList<Producer> getCollProducers()
    {
	return collProducers;
    }

    public static void setCollProducers(ArrayList<Producer> collProducers)
    {
	Database.collProducers = collProducers;
    }

    public static ArrayList<Product> getCollProducts()
    {
	return collProducts;
    }

    public static void setCollProducts(ArrayList<Product> colProducts)
    {
	Database.collProducts = colProducts;
    }

    public static Collection<Product> getProducts()
    {
	return collProducts;
    }

    public static Collection<Producer> getProducers()
    {
	return collProducers;
    }

    public void selectProducers() throws SQLException
    {
	ArrayList<Producer> prods = new ArrayList<Producer>();
	String select = "SELECT p.id, p.name, p.sales, TO_CHAR(MIN(pc.onMarket),'DD.MM.YYYY') oldest,TO_CHAR(MAX(pc.onMarket),'DD.MM.YYYY') newest,  Count(pc.id) "
		+ " FROM producers p inner join products pc on p.id = pc.id_pc " + " group by p.id, p.name, p.sales ";
	PreparedStatement stmt = conn.prepareStatement(select);
	ResultSet rs = stmt.executeQuery();
	while (rs.next())
	{
	    Date newest = rs.getDate("newest");
	    Date oldest = rs.getDate("oldest");
	    LocalDate newestLC = newest.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	    LocalDate oldestLC = oldest.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	    prods.add(
		    new Producer(rs.getInt(0), rs.getString(1), rs.getBigDecimal(2), newestLC, oldestLC, rs.getInt(5)));
	}
	collProducers.clear();
	collProducers.addAll(prods);
    }
    
    public void selectProducts(Producer p) throws SQLException {
	ArrayList<Product> prods = new ArrayList<Product>();
	String select = "SELECT id, name, id_pc, onStock, TO_CHAR(onMarket,'DD.MM.YYYY') onmarket FROM products where id_pc=?";
	PreparedStatement stmt = conn.prepareStatement(select);
	ResultSet rs = stmt.executeQuery();
	while (rs.next())
	{
	    Date onMarket = rs.getDate("onmarket");
	    LocalDate onMarketLC = onMarket.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	    prods.add(
		    new Product(rs.getInt(0), rs.getString(1), rs.getInt(2), onMarketLC, rs.getInt(5)));
	}
	collProducts.clear();
	collProducts.addAll(prods);
	Producer tmp = collProducers.stream().filter(prod -> prod.getId() ==p.getId()).findAny().get();
	tmp.getCollProducts().clear();
	tmp.getCollProducts().addAll(prods);
    }

}
