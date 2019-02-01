package pkgData;

public class Order
{
    private int id;
    private Product product;
    private int quantity;

    public Order(int id, Product product, int quantity)
    {
	super();
	this.id = id;
	this.product = product;
	this.quantity = quantity;
    }

    public int getId()
    {
	return id;
    }

    public void setId(int id)
    {
	this.id = id;
    }

    public Product getProduct()
    {
	return product;
    }

    public void setProduct(Product product)
    {
	this.product = product;
    }

    public int getQuantity()
    {
	return quantity;
    }

    public void setQuantity(int quantity)
    {
	this.quantity = quantity;
    }

    @Override
    public String toString()
    {
	return "Order [id=" + id + ", product=" + product.getName() + ", quantity=" + quantity + "]";
    }

    
}
