package pkgData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import pkgMisc.ProductStates;

public class Database
{

    // "jdbc:oracle:thin:@192.168.128.152:1521:ora11g";
    // "dbc:oracle:thin:@212.152.179.117:1521:ora11g";
    private static String connectionString;
    public static final String CONNECTION_STRING_INTERN = "jdbc:oracle:thin:@192.168.128.152:1521:ora11g";
    public static final String CONNECTION_STRING_EXTERN = "jdbc:oracle:thin:@212.152.179.117:1521:ora11g";
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
	String select = "SELECT p.id, p.name, p.sales, TO_CHAR(MIN(pc.onMarket),'DD.MM.YYYY') oldest,TO_CHAR(MAX(pc.onMarket),'DD.MM.YYYY') newest,  Count(pc.id) cnt "
		+ " FROM producers p inner join products pc on p.id = pc.id_pc " + " group by p.id, p.name, p.sales ";
	PreparedStatement stmt = conn.prepareStatement(select);
	ResultSet rs = stmt.executeQuery();
	while (rs.next())
	{

	    LocalDate newestLC = LocalDate.parse(rs.getString("newest"), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	    LocalDate oldestLC = LocalDate.parse(rs.getString("oldest"), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	    prods.add(new Producer(rs.getInt("id"), rs.getString("name"), rs.getBigDecimal("sales"), newestLC, oldestLC,
		    rs.getInt("cnt")));
	}
	collProducers.clear();
	collProducers.addAll(prods);
    }

    public void selectProducts(Producer p) throws SQLException
    {
	ArrayList<Product> prods = new ArrayList<Product>();
	String select = "SELECT id, name, onStock, TO_CHAR(onMarket,'DD.MM.YYYY') onmarket, id_pc FROM products where id_pc=?";
	PreparedStatement stmt = conn.prepareStatement(select);
	stmt.setInt(1, p.getId());
	ResultSet rs = stmt.executeQuery();
	while (rs.next())
	{
	    LocalDate onMarketLC = LocalDate.parse(rs.getString("onmarket"), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	    prods.add(new Product(rs.getInt("id"), rs.getString("name"), rs.getInt("onStock"), onMarketLC, rs.getInt("id_pc"),
		    ProductStates.NOT_CHANGED));
	}
	collProducts.clear();
	collProducts.addAll(prods);
	Producer tmp = collProducers.stream().filter(prod -> prod.getId() == p.getId()).findAny().get();
	tmp.getCollProducts().clear();
	tmp.getCollProducts().addAll(prods);
    }

    public void insertProductInDatabase(Product newProduct) throws Exception
    {
	String insert = "INSERT INTO products VALUES (seqProduct.nextVal,?,?,?,TO_DATE(?,'DD.MM.YYYY'))";
	PreparedStatement stmt = conn.prepareStatement(insert);
	stmt.setString(1, newProduct.getName());
	stmt.setInt(2, newProduct.getIdProducerId());
	stmt.setInt(3, newProduct.getOnStock());
	stmt.setString(4, newProduct.getOnMarket().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

	stmt.executeUpdate();
	commit();
    }

    public void updateProductInDatabase(Product toUpdate) throws Exception
    {
	String update = "UPDATE products SET name = ?, onstock = ? where id = ?";
	PreparedStatement st = conn.prepareStatement(update, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

	st.setString(1, toUpdate.getName());
	st.setInt(2, toUpdate.getOnStock());
	st.setInt(3, toUpdate.getId());

	st.executeUpdate();
	commit();
    }

    public void addProduct(Product newProduct, Producer prod)
    {
	collProducts.add(newProduct);
	prod.getCollProducts().add(newProduct);
    }

    public void updateProduct(Product toUpdate)
    {
	Product p = collProducts.stream().filter(prod -> prod.getId() == toUpdate.getId()).findAny().get();
	if (p != null)
	{
	    p = toUpdate;
	}
    }

    public int getNextProductId() throws SQLException
    {
	int ret = 0;
	String select = "select seqProduct.nextVal from dual";
	PreparedStatement stmt = conn.prepareStatement(select);
	ResultSet rs = stmt.executeQuery();
	while (rs.next())
	{
	    ret = rs.getInt(1);
	}
	return ret;
    }

}
