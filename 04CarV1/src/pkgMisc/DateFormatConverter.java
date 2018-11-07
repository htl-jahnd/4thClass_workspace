package pkgMisc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.util.StringConverter;

public class DateFormatConverter extends StringConverter<LocalDate>
{
	private static final String PATTERN = "dd.MM.yyyy";

	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(PATTERN);

	@Override
	public String toString(LocalDate date)
	{
		String retVal = "";
		if (date != null)
			retVal = dateFormatter.format(date);
		return retVal;
	}

	@Override
	public LocalDate fromString(String string)
	{
		LocalDate retVal = null;
		if (string != null && !string.isEmpty())
		{
			retVal = LocalDate.parse(string, dateFormatter);
		}
		return retVal;
	}

	public static String getPattern()
	{
		return PATTERN;
	}
}
