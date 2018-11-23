package pkgData;

import java.time.LocalDate;

import pkgMisc.ProductStates;

public class Product
{
    private int id;
    private String name;
    private int onStock;
    private LocalDate onMarket;
    private int producerId;
    private ProductStates state;

    public Product(int id, String name, int onStock, LocalDate onMarket, int idProducer, ProductStates state)
    {
	super();
	this.id = id;
	this.name = name;
	this.onStock = onStock;
	this.onMarket = onMarket;
	this.producerId = idProducer;
	this.state = state;
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

}
