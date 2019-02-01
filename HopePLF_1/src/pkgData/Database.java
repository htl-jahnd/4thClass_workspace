package pkgData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class Database
{
    private static String connectionString;
    public static final String CONNECTION_STRING_INTERN = "jdbc:oracle:thin:@192.168.128.152:1521:ora11g";
    public static final String CONNECTION_STRING_EXTERN = "jdbc:oracle:thin:@212.152.179.117:1521:ora11g";
    private static final String USER = "d4b12";
    private static final String PWD_DB = "d4b";
    private static Database instance = null;
    private static Connection conn = null;
    private static boolean isConnectionSet = false;
    private static ArrayList<Product> collProducts;
    private static ArrayList<Order> collOrdersToCommit;
    private static ArrayList<Order> collOrders;

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
	    collOrdersToCommit = new ArrayList<Order>();
	    collOrders= new ArrayList<Order>();
	}
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

    public static Collection<Order> getOrdersToCommit()
    {
	return collOrdersToCommit;
    }

    public static Collection<Product> getProducts()
    {
	return collProducts;
    }

    public static Collection<Order> getOrders()
    {
	return collOrders;
    }
    
    public void addOrderToCommit(Order od) {
	collOrdersToCommit.add(od);
    }
    
    public void insertOrder(Order od) throws SQLException
    {
	String insert = "INSERT INTO orders VALUES(?, ?, ?)";
	PreparedStatement stmt = conn.prepareStatement(insert);
	stmt.setInt(1, od.getId());
	stmt.setInt(2, od.getProduct().getId());
	stmt.setInt(3, od.getQuantity());

	stmt.executeUpdate();
	od.getProduct().setQuantity(od.getProduct().getQuantity()-od.getQuantity());
	updateProduct(od.getProduct());
	
    }
    
    private void updateProduct(Product product) throws SQLException
    {
	String update = "UPDATE products SET qty = ? WHERE id = ?";
	PreparedStatement st = conn.prepareStatement(update, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

	st.setInt(1, product.getQuantity());
	st.setInt(2, product.getId());

	st.executeUpdate();
    }

    public void selectProducts() throws SQLException {
	ArrayList<Product> prods = new ArrayList<Product>();
	String select = "SELECT * FROM products ORDER BY id DESC, name";
	PreparedStatement stmt = conn.prepareStatement(select);
	ResultSet rs = stmt.executeQuery();
	while (rs.next())
	{
	    prods.add(new Product(rs.getInt(1), rs.getString(2), rs.getInt(3)));
	}
	collProducts.clear();
	collProducts.addAll(prods);
    }
    
    private ArrayList<Order> selectOrders(Product p) throws SQLException {
	ArrayList<Order> ords = new ArrayList<Order>();
	String select = "SELECT orders.id, orders.qty FROM orders INNER JOIN products ON orders.pid = products.id WHERE products.id = ? ORDER BY orders.id";
	PreparedStatement stmt = conn.prepareStatement(select);
	stmt.setInt(1, p.getId());
	ResultSet rs = stmt.executeQuery();
	while (rs.next())
	{
	    ords.add(new Order(rs.getInt(1), p, rs.getInt(2)));
	}
	return ords;
    }
    
    public void doSelectAllOrders() throws SQLException {
	ArrayList<Order> tmp = new ArrayList<>();
	for(Product p : collProducts) {
	    tmp.addAll(selectOrders(p));
	}
	collOrders.clear();
	collOrders.addAll(tmp);
    }
    
}
