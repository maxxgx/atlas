package atlas.controller;

import atlas.view.SimEvent;
import atlas.view.View;

/**
 * Interface for Controller
 * 
 * @author andrea
 */

public interface Controller {

    /**
     * This method start the simulation
     */
    public void startSim();

    /**
     * This method stop the simulation
     */
    public void stopSim();

    /**
     * This method close the simulation
     */
    public void exit();

    /**
     * This method captures events from View
     * 
     * @param event
     */
    public void update(SimEvent event);

    /**
     * This method sets the view and the InputManager
     * 
     * @param event
     */
    public void setView(View v);

}
