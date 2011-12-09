import java.util.ArrayList;

//here is edit

public class Node {
	
	protected double width;
	protected ArrayList<Node> children;
	protected Particle[] particles;
	protected Vector centerOfMass;
	
	public Node(Vector start, Vector end, Particle[] particles) {
		//System.out.println(particles.length);
		if 	(particles.length == 1) {
			centerOfMass = particles[0].location;

		} else if (particles.length > 1 && particles.length != 0) {
			this.particles = new Particle[particles.length];
			System.arraycopy(particles, 0, this.particles, 0, particles.length);

			centerOfMass = this.calcCenterOfMass();

			this.width = end.x - start.x;
			double midX = ((end.x - start.x) / 2) + start.x;
			double midY = ((end.y - start.y) / 2) + start.y;
			 
			ArrayList<Particle> NE = new ArrayList<Particle>();
			NE.ensureCapacity(particles.length / 4);
			ArrayList<Particle> SE = new ArrayList<Particle>();
			SE.ensureCapacity(particles.length / 4);
			ArrayList<Particle> NW = new ArrayList<Particle>();
			NW.ensureCapacity(particles.length / 4);
			ArrayList<Particle> SW = new ArrayList<Particle>();
			SW.ensureCapacity(particles.length / 4);
			children = new ArrayList<Node>();
			children.ensureCapacity(4);	
			
			for (int i = 0; i < this.particles.length; i++) {
				if (this.particles[i].location.x <= midX) {
					if (this.particles[i].location.y <= midY) {
						if (midX >= 0.001 && midY >= 0.001)
							NW.add(this.particles[i]);
					}
					else {
						if (midX >= 0.001 && midY <= 0.999)
							SW.add(this.particles[i]);
					}
				} else {
					if (this.particles[i].location.y <= midY) {
						if (midX <= 0.999 && midY >= 0.001)
							NE.add(this.particles[i]);
					}
					else {
						if (midX <= 0.999 && midY <= 0.999)
							SE.add(this.particles[i]);
					}
				}

			}
				if (NW.size() > 0)
					children.add(new Node(new Vector(start.x, start.y), 
							new Vector(midX, midY), (Particle[]) NW.toArray(new Particle[NW.size()])));
				if (NE.size() > 0)
					children.add(new Node(new Vector(midX, start.y), 
							new Vector(end.x, midY), (Particle[]) NE.toArray(new Particle[NE.size()])));
				if (SW.size() > 0)
					children.add(new Node(new Vector(start.x, midY), 
							new Vector(midX, end.y), (Particle[]) SW.toArray(new Particle[SW.size()])));
				if (SE.size() > 0)
					children.add(new Node(new Vector(midX, midY), 
							new Vector(end.x, end.y), (Particle[]) SE.toArray(new Particle[SE.size()])));
		}
		
	}
	
	public Vector calcCenterOfMass() {
		double x = 0;
		double y = 0;
		for(int i = 0; i < particles.length; i++) {
			x += particles[i].location.x;
			y += particles[i].location.y;
		}
		x /= particles.length;
		y /= particles.length;
		return new Vector(x, y);
	}
	
	public int getSectorSize() {
		if (this.particles != null)
			return this.particles.length;
		else
			return 0;
	}
	
	public Vector getCenterOfMass() {
		return centerOfMass;
	}
	public String toString() {
		return "Node children numbers: " + this.children.size() + "\tParticle count: " + this.particles.length + "\n";
	}
}
