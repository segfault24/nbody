package atsb.nbody;

import java.util.ArrayList;

/**
 * Performs a n-body simulation of gravitational bodies.
 * 
 * TODO: implement RK4 integration option
 * TODO: implement Barnes-Hut algorithm option
 * TODO: convert recursive Barnes-Hut to iterative
 * TODO: allow for dynamic addition (and removal?) of Body's
 * TODO: add optional body aggregation on close encounters
 * TODO: parallelize the computations via traditional threads or JOCL
 * 
 * @author austin
 */
public class NBodySimulation {

	public static final int METHOD_BRUTE = 0;
	public static final int METHOD_BARNES_HUT = 1;

	public static final int INTEGRATION_EULER = 0;
	public static final int INTEGRATION_RK4 = 1;

	public static final double G = 6.673e-11; // gravity constant

	private ArrayList<Body> bodies; // all of our bodies
	private ArrayList<Body> temp; // temporary storage during calculations
	private int n = 0; // number of bodies (automatically set during setBodies)
	private long s = 1; // step size in seconds
	private int k = 1; // internally reduces time step
	private double e = 0; // singularity softener

	private OctTree tree;
	private double threshold = 0;

	private int method = METHOD_BRUTE;
	private int integration = INTEGRATION_EULER;

	public NBodySimulation() {
	}

	public void setBodies(ArrayList<Body> bodies) {
		this.bodies = bodies;
		this.n = bodies.size();

		// create the temporary results array and fill it to force memory
		// allocation
		temp = new ArrayList<Body>();
		for (int i = 0; i < n; i++) {
			temp.add(new Body());
		}
	}

	public ArrayList<Body> getBodies() {
		return bodies;
	}

	public int getSize() {
		return n;
	}

	public void setTimeStep(long s) {
		this.s = s;
	}

	public long setTimeStep() {
		return s;
	}

	public void setInterpolation(int k) {
		this.k = k;
	}

	public int getInterpolation() {
		return k;
	}

	public void setSoftener(double e) {
		this.e = e;
	}

	public double getSoftener() {
		return e;
	}

	public void setMethod(int method) {
		this.method = method;
	}

	public int getMethod() {
		return method;
	}

	public void setIntegration(int integration) {
		this.integration = integration;
	}

	public int getIntegration() {
		return integration;
	}

	public void setBucketSize(int i) {
		if(tree!=null) {
			this.tree.setBucketSize(i);
		}
	}
	
	public int getBucketSize() {
		if(tree!=null) {
			return this.tree.getBucketSize();
		} else {
			return 0;
		}
	}

	public OctTree getOctTree() {
		return tree;
	}

	public synchronized void step() {
		if (integration != INTEGRATION_EULER) {
			System.err.println("Only Euler integration is currently supported.");
			return;
		}

		switch (method) {
		case METHOD_BRUTE:
			stepBrute();
			break;
		case METHOD_BARNES_HUT:
			stepBarnesHut();
			break;
		default:
			return;
		}
	}

	private void stepBrute() {
		// main computation loop
		for (int i = 0; i < k; i++) {
			for (int a = 0; a < n; a++) {
				Body aa = bodies.get(a);
				Vector3d u = new Vector3d(0, 0, 0);

				for (int b = 0; b < n; b++) {
					if (a == b) {
						continue;
					}

					Body bb = bodies.get(b);

					// vector between body a and body b
					Vector3d r = new Vector3d(aa.p);
					r.sub(bb.p);

					double w = -bb.m / ((r.lengthSquared() + e * e) * r.length());

					r.scale(w);
					u.add(r);
				}
				double dt = ((double) s) / ((double) k);
				double q = G * dt;

				// copy over mass
				temp.get(a).m = aa.m;

				// update positions from the last iteration (into temp array)
				temp.get(a).p.x = aa.p.x + aa.v.x * dt;
				temp.get(a).p.y = aa.p.y + aa.v.y * dt;
				temp.get(a).p.z = aa.p.z + aa.v.z * dt;

				// update velocities with new accel (into temp array)
				temp.get(a).v.x = aa.v.x + q * u.x;
				temp.get(a).v.y = aa.v.y + q * u.y;
				temp.get(a).v.z = aa.v.z + q * u.z;
			}

			// swap the arrays
			ArrayList<Body> asdf = temp;
			temp = bodies;
			bodies = asdf;
		}
	}

	private Vector3d getModForce(Body b, OctNode n) {
		Vector3d u = new Vector3d(0, 0, 0);
		
		Vector3d d = new Vector3d(b.p);
		d.sub(n.getCenterOfMass());
		double r2 = d.lengthSquared();
		
		if(r2 > threshold*threshold) { // TODO: make this an angle
			// far enough to treat as single body
			
			// vector between body b and node n
			Vector3d r = new Vector3d(b.p);
			r.sub(n.getCenterOfMass());

			double w = -n.getTotalMass() / ((r.lengthSquared() + e * e) * r.length());

			r.scale(w);
			u.add(r);
			
		} else if(n.getChildren().size() > 0) {
			// too close, try the child sectors
			for(int i=0; i<n.getChildren().size(); i++) {
				u.add(getModForce(b, n.getChildren().get(i)));
			}
		} else {
			// at the lowest sector, direct calc
			for(int i=0; i<n.getBodies().size(); i++) {
				if(b == n.getBodies().get(i)) {
					continue;
				}
				//u.add(G*b.m/dist_squared(a, b));

				Body bb = n.getBodies().get(i);

				// vector between body a and body b
				Vector3d r = new Vector3d(b.p);
				r.sub(bb.p);

				double w = -bb.m / ((r.lengthSquared() + e * e) * r.length());

				r.scale(w);
				u.add(r);
				
			}
		}
		return u;
	}

	private void stepBarnesHut() {
		// TODO: fix all this temporary shit

		// generate the octree
		tree = new OctTree(-1000, -1000, -1000, 1000, 1000, 1000);
		tree.setBodies(bodies);
		tree.setBucketSize(10);
		tree.generateTree();

		// main computation loop
		for (int i = 0; i < k; i++) {
			for (int a = 0; a < n; a++) {
				Body aa = bodies.get(a);
				Vector3d u = new Vector3d(0, 0, 0);

				u.add(getModForce(aa, tree.getRoot()));

				double dt = ((double) s) / ((double) k);
				double q = G * dt;

				// copy over mass
				temp.get(a).m = aa.m;

				// update positions from the last iteration (into temp array)
				temp.get(a).p.x = aa.p.x + aa.v.x * dt;
				temp.get(a).p.y = aa.p.y + aa.v.y * dt;
				temp.get(a).p.z = aa.p.z + aa.v.z * dt;

				// update velocities with new accel (into temp array)
				temp.get(a).v.x = aa.v.x + q * u.x;
				temp.get(a).v.y = aa.v.y + q * u.y;
				temp.get(a).v.z = aa.v.z + q * u.z;
			}

			// swap the arrays
			ArrayList<Body> asdf = temp;
			temp = bodies;
			bodies = asdf;
		}
	}
}
