import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;

public class XEvent extends EventObject {

    private ArrayList<EventListener> listenerList = new ArrayList<>();

    public XEvent(Object source) {
        super(source);
    }

    public void addListener(XEventListener xlistener){
        listenerList.add(XEventListener.class xlistener);
    }

    public void removeXListener (XEventListener listener) {
        listenerList.remove (XEventListener.class listener);
    }

}
