package dev.pagefault.nbody;

/**
 * Represents a gravitational body.
 * 
 * @author austin
 */
public class Body {
	
	public double m;
	public Vector3d p;
	public Vector3d v;

	public Body() {
		this.p = new Vector3d();
		this.v = new Vector3d();
	}

	public Body(double mass, Vector3d position, Vector3d velocity) {
		this.m = mass;
		this.p = position.clone();
		this.v = velocity.clone();
	}

	public Body(Body b) {
		this.m = b.m;
		this.p = b.p.clone();
		this.v = b.v.clone();
	}

	public Body clone() {
		return new Body(this);
	}
	
	public String toString() {
		return "[m=" + m + ", p=" + p.toString() + ", v=" + v.toString() + "]";
	}
}
