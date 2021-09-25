package dev.pagefault.nbody;

public class Matrix3d {

	public double r0c0;
	public double r0c1;
	public double r0c2;
	public double r1c0;
	public double r1c1;
	public double r1c2;
	public double r2c0;
	public double r2c1;
	public double r2c2;

	public void setRow0(double x, double y, double z) {
		this.r0c0 = x;
		this.r0c1 = y;
		this.r0c2 = z;
	}

	public void setRow1(double x, double y, double z) {
		this.r1c0 = x;
		this.r1c1 = y;
		this.r1c2 = z;
	}

	public void setRow2(double x, double y, double z) {
		this.r2c0 = x;
		this.r2c1 = y;
		this.r2c2 = z;
	}

	public void setRow0(Vector3d v) {
		this.r0c0 = v.x;
		this.r0c1 = v.y;
		this.r0c2 = v.z;
	}

	public void setRow1(Vector3d v) {
		this.r1c0 = v.x;
		this.r1c1 = v.y;
		this.r1c2 = v.z;
	}

	public void setRow2(Vector3d v) {
		this.r2c0 = v.x;
		this.r2c1 = v.y;
		this.r2c2 = v.z;
	}

	public void getRow0(Vector3d v) {
		v.x = r0c0;
		v.y = r0c1;
		v.z = r0c2;
	}

	public void getRow1(Vector3d v) {
		v.x = r1c0;
		v.y = r1c1;
		v.z = r1c2;
	}

	public void getRow2(Vector3d v) {
		v.x = r2c0;
		v.y = r2c1;
		v.z = r2c2;
	}

	public void mul(Matrix3d m) {
		double nr0c0 = r0c0 * m.r0c0 + r0c1 * m.r1c0 + r0c2 * m.r2c0;
		double nr0c1 = r0c0 * m.r0c1 + r0c1 * m.r1c1 + r0c2 * m.r2c1;
		double nr0c2 = r0c0 * m.r0c2 + r0c1 * m.r1c2 + r0c2 * m.r2c2;
		double nr1c0 = r1c0 * m.r0c0 + r1c1 * m.r1c0 + r1c2 * m.r2c0;
		double nr1c1 = r1c0 * m.r0c1 + r1c1 * m.r1c1 + r1c2 * m.r2c1;
		double nr1c2 = r1c0 * m.r0c2 + r1c1 * m.r1c2 + r1c2 * m.r2c2;
		double nr2c0 = r2c0 * m.r0c0 + r2c1 * m.r1c0 + r2c2 * m.r2c0;
		double nr2c1 = r2c0 * m.r0c1 + r2c1 * m.r1c1 + r2c2 * m.r2c1;
		double nr2c2 = r2c0 * m.r0c2 + r2c1 * m.r1c2 + r2c2 * m.r2c2;
		this.r0c0 = nr0c0;
		this.r0c1 = nr0c1;
		this.r0c2 = nr0c2;
		this.r1c0 = nr1c0;
		this.r1c1 = nr1c1;
		this.r1c2 = nr1c2;
		this.r2c0 = nr2c0;
		this.r2c1 = nr2c1;
		this.r2c2 = nr2c2;
	}

	public void rotX(double a) {
		r0c0 = 1;
		r0c1 = 0;
		r0c2 = 0;
		r1c0 = 0;
		r1c1 = Math.cos(a);
		r1c2 = -Math.sin(a);
		r2c0 = 0;
		r2c1 = Math.sin(a);
		r2c2 = Math.cos(a);
	}

	public void rotY(double a) {
		r0c0 = Math.cos(a);
		r0c1 = 0;
		r0c2 = Math.sin(a);
		r1c0 = 0;
		r1c1 = 1;
		r1c2 = 0;
		r2c0 = -Math.sin(a);
		r2c1 = 0;
		r2c2 = Math.cos(a);
	}

	public void rotZ(double a) {
		r0c0 = Math.cos(a);
		r0c1 = -Math.sin(a);
		r0c2 = 0;
		r1c0 = Math.sin(a);
		r1c1 = Math.cos(a);
		r1c2 = 0;
		r2c0 = 0;
		r2c1 = 0;
		r2c2 = 1;
	}

}
