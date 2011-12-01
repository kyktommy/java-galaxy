import java.awt.*;
import java.awt.event.*;
import java.lang.Math;
import java.util.Random;
import java.util.ArrayList;

import javax.swing.*;

public class Galaxy extends JLabel implements ActionListener{
	protected final int N = 1000;
	protected final int MASS = 1;
	protected final int SIZE = 600;
	protected final double GCONSTANT = 0.05;
	protected final double DT = 0.001;
	protected Particle[] particle = new Particle[N];
	protected ArrayList<Particle> particleList = new ArrayList<Particle>();
	public Timer frames;
	public BHTree root;
	
	protected final int FPS = 24;
	
	public Galaxy() {
		super();
		setPreferredSize(new Dimension(600, 600));
		frames = new Timer(1000 / FPS, this);
		createParticles();
		frames.start();
	}
	
	private void createParticles() {
		double v = 25;
		double alpha = 0.25;
		Random rnd = new Random();
		double r, theta, x, y;
		double vx, vy, r_;
		for (int i=0; i<N; i++) {
			r = rnd.nextDouble() / 2.0;
			theta = rnd.nextDouble() * Math.PI * 2.0;
			x = (1/2.0) + r * Math.cos(theta);
			y = (1/2.0) + alpha * r * Math.sin(theta);
			r_ = Math.sqrt((x-(1/2.0)) * (x-(1/2.0)) + 
					(y-(1/2.0)) * (y-(1/2.0)));
			vx = (-v * r_ * Math.sin(theta));
			vy = (v * r_ * Math.cos(theta));
			particle[i] = new Particle(x, y);
			particle[i].setVelocity(vx, vy);
		}
	}
	
	private void update() {
		calculateForces();
	}
	
	private void calculateForces() {
		Vector sum = new Vector();
		Vector rValue;
		double distance;
		
		for(Particle p : particle){
			System.out.println(p.location.x +" "+p.location.y);
		}
		
		//createApproxParticleList
		root = new BHTree(600, new Vector(300, 300) );
		for(int i=0; i<N; i++) {
			root.addParticleIndex(i);
		}
		quadrify(0.0, 0.0, 1.0, 1.0, root);
		System.out.println("finish quadrify");
		
		for (int i=0; i<N; i++) {
			sum.set(0.0, 0.0);
			
		    //generate particleList
			transverseTree(root, i);
			System.out.println("finish transverse: " + i);
			
			//loop the sector and sum up
			for (Particle particleSector : particleList) {
				distance = particle[i].distance(particleSector);
				rValue = Particle.getRValue(particle[i], particleSector);
				rValue.mult((MASS * particleSector.getMass()) / (distance * distance));
				sum.add(rValue);
			}

			Vector force = new Vector(sum.x, sum.y); 
			force.mult(-GCONSTANT * MASS);

			particle[i].giveForce(force, DT, MASS);
			particleList.clear();
		}
		
		for(Particle p : particle){
			System.out.println(p.location.x +" "+p.location.y);
		}
	}
	
	
	//create & insert nodes into root
	public void quadrify(double startx, double starty, double endx, double endy, BHTree parent){
		if(parent != null && parent.particleIndices.size() > 1) { //have particle ? end : -
			//midPoint
			double midX = ((endx - startx) / 2) + startx;
			double midY = ((endy - starty) / 2) + starty;
			Vector midPoint = new Vector(midX, midY);
			
			//child
			double length = midX-startx;
			parent.NE = new BHTree(length, midPoint);
			parent.NW = new BHTree(length, midPoint);
			parent.SE = new BHTree(length, midPoint);
			parent.SW = new BHTree(length, midPoint);
			
			//System.out.println("parent "+parent.particleIndices.size());
				
			for (Integer integer : parent.particleIndices) {
				int i = integer.intValue();
				//System.out.println("particle "+i);
				//System.out.println(particle[i].location.x);

				if (particle[i].location.x <= midX) {
					if (particle[i].location.y <= midY) 
						parent.NW.addParticleIndex(i);
					else
						parent.SW.addParticleIndex(i);
				} 
				else {
					if (particle[i].location.y <= midY)
						parent.NE.addParticleIndex(i);
					else
						parent.SE.addParticleIndex(i);
				}
			}
			
			/*
			System.out.println("NW: "+parent.NW.particleIndices.size());
			System.out.println("NE: "+parent.NE.particleIndices.size());
			System.out.println("SE: "+parent.SE.particleIndices.size());
			System.out.println("SW: "+parent.SW.particleIndices.size());
			*/
			/*
			if(parent.NW.particleIndices.size() == 0) { parent.NW = null; }
			if(parent.NE.particleIndices.size() == 0) { parent.NE = null; }
			if(parent.SW.particleIndices.size() == 0) { parent.SW = null; }
			if(parent.SE.particleIndices.size() == 0) { parent.SE = null; }
			*/

			//continue find particle
			quadrify(startx, starty, midX, midY, parent.NW);
			quadrify(midX, starty, endx, midY, parent.NE);
			quadrify(startx, midY, midX, endy, parent.SW);
			quadrify(midX, midY, endx, endy, parent.SE);
		}
	}
	
	//transverse the tree and generate the particle sector list
	public void transverseTree(BHTree bhTree, int i) {	
		if(bhTree != null) {
			int size = bhTree.particleIndices.size();
			if(size != 0) {
				if(size > 1) { // transverse
					if(bhTree.length / particle[i].distance(bhTree.midPoint) > N/1000) {
							transverseTree(bhTree.NW, i);
							transverseTree(bhTree.NE, i);
							transverseTree(bhTree.SW, i);
							transverseTree(bhTree.SE, i);
					} else { //become sector
						Particle p = mergeParticle(bhTree.particleIndices);
						particleList.add(p);
						//System.out.println("list size: " + size);
					}
				} //if size = 1
				else {
					//System.out.println(bhTree.particleIndices.get(0).intValue());
					particleList.add(particle[bhTree.particleIndices.get(0).intValue()]);
				}
			} //end no particle
		}// end not null tree
	}
	
	//merge a group of particles
	private Particle mergeParticle(ArrayList<Integer> indices) {
		double sumX = 0;
		double sumY = 0;
		for(Integer index : indices) {
			sumX += particle[index.intValue()].location.x;
			sumY += particle[index.intValue()].location.y;
		}
		double x = sumX / indices.size();
		double y = sumY / indices.size();
		//System.out.println("sector location: "+x +" "+y);
		Particle sector = new Particle(x, y); 
		sector.setMass(indices.size());
		return sector;
	}

	private void drawParticles(Graphics g) {
		Dimension d = getSize();
		g.setColor(Color.WHITE);
		int x, y;
		for (int i=0; i<N; i++) {
			x = (int)(d.width * particle[i].location.x);
			y = (int)(d.height * particle[i].location.y);
			g.drawLine(x, y, x, y);
		}
	}	
	
	private void drawTree(Graphics g, BHTree bhTree) {
		if(bhTree != null) {
			int size = bhTree.particleIndices.size();
			if(size != 0) {
				if(size > 1) {
					drawTree(g, bhTree.NW);
					drawTree(g, bhTree.NE);
					drawTree(g, bhTree.SW);
					drawTree(g, bhTree.SE);
				} 
				else { //if size = 1
					g.setColor(Color.green);
					g.drawRect((int)(bhTree.midPoint.x + bhTree.length), 
							(int)(bhTree.midPoint.y + bhTree.length),
							(int)bhTree.length, (int)bhTree.length);
				}
			} //end no particle
		}// end not null tree
	}
	
	public void paintComponent(Graphics g) {
		setBackground(Color.BLACK);
		super.paintComponent(g);
		drawParticles(g);
		//drawTree(g, root);
		System.out.println("finish draw");
	}
	
	public void actionPerformed(ActionEvent e) {
		update();
		repaint();
		//frames.stop();
	}
}
