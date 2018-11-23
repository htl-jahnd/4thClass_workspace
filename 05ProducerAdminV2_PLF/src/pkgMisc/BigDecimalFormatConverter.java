package pkgMisc;

import java.math.BigDecimal;

import javafx.util.StringConverter;

public class BigDecimalFormatConverter extends StringConverter<BigDecimal>
{
    @Override
    public String toString(BigDecimal bd)
    {
	String retVal = "";
	if (bd != null)
	    retVal = bd.toString();
	return retVal;
    }

    @Override
    public BigDecimal fromString(String string)
    {
	BigDecimal retVal = null;
	if (string != null && !string.isEmpty())
	{
	    retVal = new BigDecimal(string);
	}
	return retVal;
    }

}
