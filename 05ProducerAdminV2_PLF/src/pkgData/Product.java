package pkgData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import pkgMisc.ProductStates;

public class Product
{
    private int id;
    private String name;
    private int onStock;
    private LocalDate onMarket;
    private int producerId;
    private ProductStates state;
    private int decreasedStock;
    private BigDecimal price;

   

    public Product(int id, String name, int onStock, LocalDate onMarket, int producerId, ProductStates state,
	    int decreasedStock, BigDecimal price)
    {
	super();
	this.id = id;
	this.name = name;
	this.onStock = onStock;
	this.onMarket = onMarket;
	this.producerId = producerId;
	this.state = state;
	this.decreasedStock = decreasedStock;
	this.price = price;
    }

    @Override
    public String toString()
    {
	return "Product [id=" + id + ", name=" + name + ", onStock=" + onStock + ", onMarket=" + onMarket
		+ ", idProducer=" + producerId + ", state=" + state + "]";
    }

    public ProductStates getState()
    {
	return state;
    }

    public void setState(ProductStates state)
    {
	this.state = state;
    }

    public int getId()
    {
	return id;
    }

    public void setId(int id)
    {
	this.id = id;
    }

    public String getName()
    {
	return name;
    }

    public void setName(String name)
    {
	this.name = name;
    }

    public int getOnStock()
    {
	return onStock;
    }

    public void setOnStock(int onStock)
    {
	this.onStock = onStock;
    }

    public LocalDate getOnMarket()
    {
	return onMarket;
    }
    
    public String getOnMarketAsString() {
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, MMM d yyyy");
	return formatter.format(onMarket);
    }

    public void setOnMarket(LocalDate onMarket)
    {
	this.onMarket = onMarket;
    }

    public int getIdProducerId()
    {
	return producerId;
    }

    public void setIdProducer(int idProducer)
    {
	this.producerId = idProducer;
    }

    public int getDecreasedStock()
    {
        return decreasedStock;
    }

    public void setDecreasedStock(int decreasedStock)
    {
        this.decreasedStock = decreasedStock;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

}
