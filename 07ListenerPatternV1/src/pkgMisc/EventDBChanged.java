package pkgMisc;

import java.util.EventObject;


public class EventDBChanged extends EventObject
{

    private final EventDBType type;
    
    public EventDBChanged(Object source)
    {
	super(source);
	type = EventDBType.UNDEFINED;
    }

    public EventDBChanged(Object source, EventDBType type)
    {
	super(source);
	this.type =type;
    }

    public EventDBType getType()
    {
        return type;
    }


}
