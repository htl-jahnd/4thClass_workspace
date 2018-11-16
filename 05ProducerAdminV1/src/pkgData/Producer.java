package pkgData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

public class Producer
{

    private int id;
    private String name;
    private BigDecimal sales;
    private LocalDate newest;
    private LocalDate oldest;
    private int cnt;
    private ArrayList<Product> collProducts;

    public Producer(int id, String name, BigDecimal sales, LocalDate newest, LocalDate oldest, int cnt)
    {
	super();
	this.id = id;
	this.name = name;
	this.sales = sales;
	this.newest = newest;
	this.oldest = oldest;
	this.cnt = cnt;
    }

    @Override
    public String toString()
    {
	return "Producer [id=" + id + ", name=" + name + ", sales=" + sales + ", newest=" + newest + ", oldest="
		+ oldest + ", cnt=" + cnt + "]";
    }

    public LocalDate getNewest()
    {
	return newest;
    }

    public void setNewest(LocalDate newest)
    {
	this.newest = newest;
    }

    public LocalDate getOldest()
    {
	return oldest;
    }

    public void setOldest(LocalDate oldest)
    {
	this.oldest = oldest;
    }

    public int getCnt()
    {
	return cnt;
    }

    public void setCnt(int cnt)
    {
	this.cnt = cnt;
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

    public BigDecimal getSales()
    {
	return sales;
    }

    public void setSales(BigDecimal sales)
    {
	this.sales = sales;
    }

    public ArrayList<Product> getCollProducts()
    {
	return collProducts;
    }

    public void setCollProducts(ArrayList<Product> collProducts)
    {
	this.collProducts = collProducts;
    }

}
