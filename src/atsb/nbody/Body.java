package atsb.nbody;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * Represents a gravitational body.
 * 
 * @author austin
 */
public class Body {
	
	public double m;
	public Point3d p;
	public Vector3d v;

	public Body() {
		this.p = new Point3d();
		this.v = new Vector3d();
	}

	public Body(double mass, Point3d position, Vector3d velocity) {
		this.m = mass;
		this.p = (Point3d) position.clone();
		this.v = (Vector3d) velocity.clone();
	}

	public Body(Body b) {
		this.m = b.m;
		this.p = (Point3d) b.p.clone();
		this.v = (Vector3d) b.v.clone();
	}

	public Body clone() {
		return new Body(this);
	}
	
	public String toString() {
		return "[m=" + m + ", p=" + p.toString() + ", v=" + v.toString() + "]";
	}
}
