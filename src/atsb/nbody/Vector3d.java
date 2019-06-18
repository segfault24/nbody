package atsb.nbody;

public class Vector3d {

	public double x;
	public double y;
	public double z;

	public Vector3d() {
	}

	public Vector3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3d(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3d(Vector3d v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}

	public double dot(Vector3d v) {
		return v.x * x + v.y * y + v.z * z;
	}

	public void cross(Vector3d v1, Vector3d v2) {
		this.x = v1.y * v2.z - v1.z * v2.y;
		this.y = v1.z * v2.x - v1.x * v2.z;
		this.z = v1.x * v2.y - v1.y * v2.x;
	}

	public double length() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	public double lengthSquared() {
		return x * x + y * y + z * z;
	}

	public void add(Vector3d v) {
		this.x += v.x;
		this.y += v.y;
		this.z += v.z;
	}

	public void sub(Vector3d v) {
		this.x -= v.x;
		this.y -= v.y;
		this.z -= v.z;
	}

	public void scale(double s) {
		this.x *= s;
		this.y *= s;
		this.z *= s;
	}

	public void normalize() {
		double len = this.length();
		this.x = x / len;
		this.y = y / len;
		this.z = z / len;
	}

	@Override
	public Vector3d clone() {
		return new Vector3d(this);
	}

}
