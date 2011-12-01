import java.util.ArrayList;

public class BHTree {
		public BHTree NW;
		public BHTree NE;
		public BHTree SW;
		public BHTree SE;
		double length; //h
		ArrayList<Integer> particleIndices; //included particles
		Vector midPoint;
		
		public BHTree() {
			this.NW = null;
			this.NE = null;
			this.SE = null;
			this.SW = null;
			this.length = 0;
			this.particleIndices = new ArrayList<Integer>(0);
			this.midPoint = new Vector();
		}
		
		public BHTree(double length, Vector midPoint) {
			this.NW = null;
			this.NE = null;
			this.SE = null;
			this.SW = null;
			this.length = length;
			this.particleIndices = new ArrayList<Integer>(0);
			this.midPoint = midPoint;
		}
		
		public void addParticleIndex(int i) {
			this.particleIndices.add(new Integer(i));
		}
		
		public void setLength(double length) {
			this.length = length;
		}
}
