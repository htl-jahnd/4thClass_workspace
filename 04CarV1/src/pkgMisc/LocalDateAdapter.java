package pkgMisc;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate>
{
    private static final String PATTERN = "dd.MM.uuuu";

    @Override
    public JsonElement serialize(LocalDate date, Type typeOfSource, JsonSerializationContext jsc)
    {
	return new JsonPrimitive(date.format(DateTimeFormatter.ofPattern(PATTERN)));
    }

    @Override
    public LocalDate deserialize(JsonElement je, Type typeofSource, JsonDeserializationContext jdc)
    {
	return LocalDate.parse(je.getAsString(), DateTimeFormatter.ofPattern(PATTERN));
    }

}
