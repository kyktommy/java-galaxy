public class Particle{
	Vector location = new Vector();
	Vector velocity = new Vector();
	Vector acceleration = new Vector();
	private int mass = 1;
	
	public Particle(double x, double y) {
		location.x = x;
		location.y = y;
		//System.out.println(location.x);
	}
	
	public static Vector getRValue(Particle i, Particle j) {
		return Vector.sub(i.location, j.location);
	}
	
	public void giveForce(Vector force, double dT, int MASS) {
		force.div(MASS);
		acceleration.x = force.x;
		acceleration.y = force.y;
		acceleration.mult(dT);
		//System.out.println(temp);
		velocity.add(acceleration);
		
		Vector tempV = new Vector();
		tempV.x = velocity.x;
		tempV.y = velocity.y;
		tempV.mult(dT);
		location.add(tempV);
	}
	public double distance(Particle other) {
		return this.location.dist(other.location);
	}
	
	public double distance(Vector other) {
		return this.location.dist(other);
	}

	//set/get methods for location
	public void setX(double x) {
		location.x = x;
	}
	public void setY(double y) {
		location.y = y;
	}
	public void setMass(int mass) {
		this.mass = mass;
	}
	
	public int getMass() {
		return mass;
	}
	
	//set/get methods for velocity
	public void setVelocity(double vx, double vy) {
		velocity.x = vx;
		velocity.y = vy;
		//velocity.normalize();
	}
	
	public String toString() {
		return "Mass: " + mass;
	}

}
