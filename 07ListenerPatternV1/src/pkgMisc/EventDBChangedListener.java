package pkgMisc;

import java.util.EventListener;

public interface EventDBChangedListener extends EventListener
{
    public void handleEventDBChangedEvent(EventDBChanged event);
}
