package atlas.utils;

/**
 * This clock starts from any given non-negative time and advances by an offset only when told to do so.
 *
 */
public class Counter implements java.io.Serializable {    
    
    private static final long serialVersionUID = 3577397934458447332L;
    
    private long counter;
    
    /**
     * Default constructor
     */
    public Counter(){
        this.counter = 0L;
    }
    
    /**
     * Construct a offset Counter
     * 
     * @param offset
     *           
     */
    public Counter(long offset) {
        super();
        this.counter = offset;
    }

  
    public long get() {
        return this.counter;
    }

    
    public void increment(long delta) {
//        if(delta < 0){
//            throw new IllegalArgumentException();
//        }
        this.counter += delta;
    }
    
  
    public String toString(){
        return new StringBuilder().append(this.counter).toString();
    }
}
