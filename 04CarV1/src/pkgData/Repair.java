package pkgData;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import pkgMisc.DateFormatConverter;

public class Repair implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static int numberOfRepairs = 0;

    private static String DATE_PATTERN = "dd.MM.uuu";

    private final int idRepair;
    private LocalDate dateRepair;
    private String textRepair;
    private BigDecimal amountRepair;

    public Repair(LocalDate dateRepair, String textRepair, BigDecimal amountRepair)
    {

	super();
	numberOfRepairs++;
	this.idRepair = numberOfRepairs;
	this.dateRepair = dateRepair;
	this.textRepair = textRepair;
	this.amountRepair = amountRepair;

    }

    public static String getDATE_PATTERN()
    {
	return DATE_PATTERN;
    }

    @Override
    public String toString()
    {
	return "Repair [idRepair=" + idRepair + ", dateRepair=" + dateRepair + ", textRepair=" + textRepair
		+ ", amountRepair=" + amountRepair + "]";
    }

    // public static void decreaseNumberOfRepairsByOne() {
    // numberOfRepairs--;
    // }
    public static int getNumberOfRepairs()
    {
	return numberOfRepairs;
    }

    public static void setNumberOfRepairs(int numberOfRepairs)
    {
	Repair.numberOfRepairs = numberOfRepairs;
    }

    public int getIdRepair()
    {
	return idRepair;
    }

    public LocalDate getDateRepair()
    {
	return dateRepair;
    }

    public void setDateRepair(LocalDate dateRepair)
    {
	this.dateRepair = dateRepair;
    }

    public String getTextRepair()
    {
	return textRepair;
    }

    public void setTextRepair(String textRepair)
    {
	this.textRepair = textRepair;
    }

    public BigDecimal getAmountRepair()
    {
	return amountRepair;
    }

    public void setAmountRepair(BigDecimal amountRepair)
    {
	this.amountRepair = amountRepair;
    }

}
