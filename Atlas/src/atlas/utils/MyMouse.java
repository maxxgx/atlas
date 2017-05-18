package atlas.utils;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EventListener;

public interface MyMouse extends EventListener, MouseListener {

    /**
     * Invoked when the mouse button has been clicked (pressed and released) on
     * a component.
     */
    public void mouseClicked(MouseEvent e);

}
