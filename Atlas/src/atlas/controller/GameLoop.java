package atlas.controller;

import java.util.Optional;

import atlas.model.Body;
import atlas.model.Model;
import atlas.utils.Units;
import atlas.view.View;

/**
 * The GameLoop is the main Thread of Simulation, that synchronizes Model and
 * View
 * 
 * @author andrea
 */

public class GameLoop extends Thread {
	private volatile int speed;
	private volatile long precision;
	private final static int FPS = 50;
	private final static int SKIP_TICKS = 1000 / FPS;
	private final static int MAX_FRAMESKIP = 10;
	private final static int STANDARD_PRECISION = Units.DAY_SEC.getValue()/2;
	private volatile int loop;
	private long next_game_tick;
	private StatusSim status;
	private Model model;
	private View view;
	private Optional<Body> nextBodyToAdd = Optional.empty();
    

    /**
     * Creates new GameLoop and sets the status of Simulation to Stop
     * 
     * @param v
     *            The ViewInterface object
     */
    public GameLoop(Model model) {
        this.model = model;
        this.status = StatusSim.RUNNING;
        this.speed = 20;
        this.precision = STANDARD_PRECISION;

    }

    /**
     * SKIP_TICK sets the cycle of gameloop. If the cycle is slow to render all
	 * bodies, it skip some frames (decrease frame rate) and it does more
	 * consecutive updates, or else it sleep to lock fps bound.
     */

    public void run() {
        while (!status.equals(StatusSim.EXIT)) {
            this.next_game_tick = System.currentTimeMillis();
            while (status.equals(StatusSim.RUNNING)) {
                long lastFrame = System.currentTimeMillis();
                this.loop = 0;
                while ((System.currentTimeMillis() > this.next_game_tick) && (this.loop < MAX_FRAMESKIP)) {
					synchronized (this.model.getBodiesToRender()) {
						for (int i = 0; i < speed; i++) {
							this.model.updateSim(precision / FPS);
						}
					}
                    this.next_game_tick += SKIP_TICKS;
                    this.loop++;
                }
                /* sleep for 1 ms if too fast (dormo fino a il tempo dall'ultimo
                 frame renderizzato sia uguale a skip ticks, per non
                 velocizzare la simulazione)*/
                while (System.currentTimeMillis() - lastFrame < SKIP_TICKS) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // quando sono in pausa passo al model lo Status
                }
                // timeSLF = time since last frame
                long timeSLF = System.currentTimeMillis() - lastFrame;
                // rendering();
                long FPS = 1000 / timeSLF;
                
                synchronized (this.model.getBodiesToRender()) {					
                	if(this.nextBodyToAdd.isPresent()) {
                		this.model.addBody(this.nextBodyToAdd.get());
                		this.nextBodyToAdd = Optional.empty(); 
                	}
				}
                
                this.view.render(this.model.getBodiesToRender(), model.getTime(), (int)FPS);
            }
            
            this.view.render(this.model.getBodiesToRender(), model.getTime(), GameLoop.FPS);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method sets the Status
     * 
     * @param status
     *            StatusSim object
     */
    private synchronized void setStatus(StatusSim status) {
        this.status = status;
    }

    /**
     * This method sets the Status to Running calling setStatus()
     */
    public void setRunning() {
        if (!this.status.equals(StatusSim.RUNNING)) {
            this.setStatus(StatusSim.RUNNING);
        }
    }

    /**
     * This method sets the Status to Stopped calling setStatus()
     */
    public void setStopped() {
        if (!this.status.equals(StatusSim.STOPPED)) {
            this.setStatus(StatusSim.STOPPED);
        }
    }

    /**
     * This method sets the Status to Stopped calling setStatus()
     */
    public void setExit() {
        this.setStatus(StatusSim.EXIT);
    }

    /**
     * This method returns the current Status
     */
    public synchronized StatusSim getStatus() {
        return this.status;
    }

    public void setView(View v) {
        this.view = v;
    }
     
    /**
     * This sets the precision and speed
     */
    public void setValue(final long precision, final int speed) {
        this.precision = precision;
        this.speed = speed;
    }
    
    public int getSpeed() {
        return this.speed;
    }
    
    public long getUnit() {
        return this.precision;
    }
    
    public void setModel(Model model) {
        this.model = model;
    }
    
    /**
     * This sets next body to add
     */
    public void setNextBodyToAdd(Body body) {
        this.nextBodyToAdd = Optional.of(body);
    }

}
