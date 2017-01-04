package atsb.nbody;

import java.util.ArrayList;

import javax.vecmath.Point3d;

public class OctNode {
	
	private double lx, ly, lz, ux, uy, uz; // bounding planes
	private ArrayList<Body> bodies; // bodies within the node
	private ArrayList<OctNode> children; // sub nodes
	
	private Point3d centerOfMass;
	private double totalMass;
	
	public OctNode(double lx, double ly, double lz, double ux, double uy, double uz) {
		this.lx = lx;
		this.ly = ly;
		this.lz = lz;
		this.ux = ux;
		this.uy = uy;
		this.uz = uz;
		
		bodies = new ArrayList<Body>();
		children = new ArrayList<OctNode>();
		centerOfMass = new Point3d();
	}
	
	public void addBody(Body b) {
		this.bodies.add(b);
	}
	
	public ArrayList<Body> getBodies() {
		return bodies;
	}
	
	public void addChild(OctNode n) {
		this.children.add(n);
	}
	
	public ArrayList<OctNode> getChildren() {
		return children;
	}
	
	public void fillProps() {
		double cx = 0;
		double cy = 0;
		double cz = 0;
		double ttl = 0;
		for(int i=0; i<bodies.size(); i++) {
			Body b = bodies.get(i);
			cx += b.m*b.p.x;
			cy += b.m*b.p.y;
			cz += b.m*b.p.z;
			ttl += b.m;
		}
		if(ttl>0) {
			cx /= ttl;
			cy /= ttl;
			cz /= ttl;
		}
		
		centerOfMass.x = cx;
		centerOfMass.y = cy;
		centerOfMass.z = cz;
		totalMass = ttl;
	}
	
	public double getLx() {
		return lx;
	}

	public double getLy() {
		return ly;
	}

	public double getLz() {
		return lz;
	}

	public double getUx() {
		return ux;
	}

	public double getUy() {
		return uy;
	}

	public double getUz() {
		return uz;
	}
	
	public Point3d getCenterOfMass() {
		return centerOfMass;
	}
	
	public double getTotalMass() {
		return totalMass;
	}
}
