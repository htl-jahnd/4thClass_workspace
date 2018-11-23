package pkgData;

import java.math.BigDecimal;

public class Order
{

    private int id;
    private int quantity;
    private BigDecimal price;
    private Product productToOrder;

    public Order(int id, int quantity, BigDecimal price, Product productToOrder)
    {
	super();
	this.id = id;
	this.quantity = quantity;
	this.price = price;
	this.productToOrder = productToOrder;
    }

    @Override
    public String toString()
    {
	return "Order [id=" + id + ", quantity=" + quantity + ", price=" + price + ", productToOrder=" + productToOrder
		+ "]";
    }

    public int getId()
    {
	return id;
    }

    public void setId(int id)
    {
	this.id = id;
    }

    public int getQuantity()
    {
	return quantity;
    }

    public void setQuantity(int quantity)
    {
	this.quantity = quantity;
    }

    public BigDecimal getPrice()
    {
	return price;
    }

    public void setPrice(BigDecimal price)
    {
	this.price = price;
    }

    public Product getProductToOrder()
    {
	return productToOrder;
    }

    public void setProductToOrder(Product productToOrder)
    {
	this.productToOrder = productToOrder;
    }

}
