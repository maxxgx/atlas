package atlas.model.rules;

import atlas.model.BHTree;
import atlas.model.Body;
import atlas.model.Quad;

import java.util.List;

public class AlgorithmBarnesHut extends Algorithm {
    private static final long serialVersionUID = -266156345861451285L;

    public AlgorithmBarnesHut() {
    }

    @Override
    public List<Body> exceuteUpdate(List<Body> bodies, double sec) {
        Quad q = new Quad(0, 0, 2 * 1e18);
        BHTree thetree = new BHTree(q);
        // If the body is still on the screen, add it to the tree
        for (Body b : bodies) {
            if (b.in(q)) thetree.insert(b);
        }
        //Now, use out methods in BHTree to update the forces,
        //traveling recursively through the tree
        for (Body b : bodies) {
            b.resetForce();
            if (b.in(q)) {
                thetree.updateForce(b);
            }
            b.updatePos(sec);
        }
        return bodies;
    }
}
