import java.lang.Math;

public class Vector {
	public double x;
	public double y;
	
	public Vector() {}
	
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void add(Vector other) {
		this.x += other.x;
		this.y += other.y;
	}
	
	public static Vector add(Vector v1, Vector v2) {
		return new Vector(v1.x + v2.x, v1.y + v2.y);
	}
	
	public void sub(Vector other) {
		this.x -= other.x;
		this.y -= other.y;
	}
	
	public static Vector sub(Vector v1, Vector v2) {
		return new Vector(v1.x - v2.x, v1.y - v2.y);
	}
	
	public void mult(double scale) {
		this.x *= scale;
		this.y *= scale;
	}
	
	public void div(double scale) {
		this.x = this.x * scale;
		this.y = this.y * scale;
	}
	
	public double mag() {
		return Math.sqrt(this.x * this.x + this.y * this.y);
	}
	
	public double dist(Vector other) {
		Vector d = new Vector(this.x - other.x, this.y - other.y);
		return d.mag();
	}
	
	public void normalize() {
		double m = this.mag();
		this.div(m);
	}
	
	public String toString() {
		return "[" + x + ", " + y + "]";
	}
}
