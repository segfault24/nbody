package dev.pagefault.nbody;

import java.util.ArrayList;

public class OctTree {

	private OctNode root;
	private int bucketSize = 1;
	
	public OctTree(double lx, double ly, double lz, double ux, double uy, double uz) {
		root = new OctNode(lx, ly, lz, ux, uy, uz);
	}
	
	public void setBodies(ArrayList<Body> bodies) {
		for(int i=0; i<bodies.size(); i++) {
			root.addBody(bodies.get(i));
		}
	}
	
	public void setBucketSize(int bucketSize) {
		this.bucketSize = bucketSize;
	}
	
	public int getBucketSize() {
		return bucketSize;
	}
	
	public void generateTree() {
		if(root != null) {
			genChildren(root);
		}
	}
	
	public OctNode getRoot() {
		return root;
	}
	
	private void genChildren(OctNode n) {
		if(n.getBodies().size() <= bucketSize) {
			return;
		}
		
		// create 8 children
		double mx = (n.getLx() + n.getUx())/2;
		double my = (n.getLy() + n.getUy())/2;
		double mz = (n.getLz() + n.getUz())/2;
		
		OctNode s1 = new OctNode(n.getLx(), n.getLy(), n.getLz(), mx, my, mz);
		OctNode s2 = new OctNode(mx, n.getLy(), n.getLz(), n.getUx(), my, mz);
		OctNode s3 = new OctNode(n.getLx(), n.getLy(), mz, mx, my, n.getUz());
		OctNode s4 = new OctNode(mx, n.getLy(), mz, n.getUx(), my, n.getUz());
		OctNode s5 = new OctNode(n.getLx(), my, n.getLz(), mx, n.getUy(), mz);
		OctNode s6 = new OctNode(mx, my, n.getLz(), n.getUx(), n.getUy(), mz);
		OctNode s7 = new OctNode(n.getLx(), my, mz, mx, n.getUy(), n.getUz());
		OctNode s8 = new OctNode(mx, my, mz, n.getUx(), n.getUy(), n.getUz());

		// sort the bodies into the child nodes
		for(int i=0; i<n.getBodies().size(); i++) {
			Body b = n.getBodies().get(i);
			if (b.p.x <= mx) {
				if (b.p.y <= my) {
					if (b.p.z <= mz) {
						s1.addBody(b);
					} else {
						s3.addBody(b);
					}
				} else {
					if (b.p.z <= mz) {
						s5.addBody(b);
					} else {
						s7.addBody(b);
					}
				}
			} else {
				if (b.p.y <= my) {
					if (b.p.z <= mz) {
						s2.addBody(b);
					} else {
						s4.addBody(b);
					}
				} else {
					if (b.p.z <= mz) {
						s6.addBody(b);
					} else {
						s8.addBody(b);
					}
				}
			}
		}

		// calc child properties
		s1.fillProps();
		s2.fillProps();
		s3.fillProps();
		s4.fillProps();
		s5.fillProps();
		s6.fillProps();
		s7.fillProps();
		s8.fillProps();
		
		// add the children
		n.addChild(s1);
		n.addChild(s2);
		n.addChild(s3);
		n.addChild(s4);
		n.addChild(s5);
		n.addChild(s6);
		n.addChild(s7);
		n.addChild(s8);
		
		// recurse on each child
		genChildren(s1);
		genChildren(s2);
		genChildren(s3);
		genChildren(s4);
		genChildren(s5);
		genChildren(s6);
		genChildren(s7);
		genChildren(s8);
	}
}