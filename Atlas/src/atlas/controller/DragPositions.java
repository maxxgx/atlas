package atlas.controller;

import java.awt.MouseInfo;
import java.util.NoSuchElementException;

import atlas.utils.Pair;
import atlas.view.ViewImpl;

/**
 * This thread sets the position of body to move. 
 * 
 * @author andrea
 */

public class DragPositions extends Thread {

    private double scale;
    private Pair<Double, Double> reference;
    private static final int step = 33;
    private boolean bool = true;

    public DragPositions(double scale, Pair<Double, Double> reference) {
        this.scale = scale;
        this.reference = reference;
    }

    public void run() {
        while (bool) {
            double actualScale = this.scale;
            long last = System.currentTimeMillis();
            while (System.currentTimeMillis() - last < step) {
            	try{
                ViewImpl.getView().getSelectedBody().get().setPosX((MouseInfo.getPointerInfo().getLocation().getX() - 20
                        - ViewImpl.getView().getRenderScreenOrig().getX() - this.reference.getX()) / actualScale);
                ViewImpl.getView().getSelectedBody().get().setPosY((MouseInfo.getPointerInfo().getLocation().getY() - 5
                        - ViewImpl.getView().getRenderScreenOrig().getY() - this.reference.getY()) / -actualScale);
            	} catch (NoSuchElementException e) {
            		
            	}
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    

    public void setScale(Double scale) {
        this.scale = scale;
    }

    public void stopEdit() {
        this.bool = false;
    }

}
