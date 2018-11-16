package pkgData;

import java.time.LocalDate;

public class Product
{
    private int id;
    private String name;
    private int onStock;
    private LocalDate onMarket;
    private int idProducer;

    public Product(int id, String name, int onStock, LocalDate onMarket, int idProducer)
    {
	super();
	this.id = id;
	this.name = name;
	this.onStock = onStock;
	this.onMarket = onMarket;
	this.idProducer = idProducer;
    }

    @Override
    public String toString()
    {
	return "Product [id=" + id + ", name=" + name + ", onStock=" + onStock + ", onMarket=" + onMarket
		+ ", idProducer=" + idProducer + "]";
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

    public int getIdProducer()
    {
	return idProducer;
    }

    public void setIdProducer(int idProducer)
    {
	this.idProducer = idProducer;
    }

}
