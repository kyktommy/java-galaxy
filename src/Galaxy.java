import java.awt.*;
import java.awt.event.*;
import java.lang.Math;
import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;

import javax.swing.*;

public class Galaxy extends JLabel implements ActionListener{
	protected final int N = 3000;
	protected final int MASS = 1;
	protected final int SIZE = 600;
	protected final double GCONSTANT = 0.0005;
	protected final double DT = 0.001;
	protected final double HRRATIO = 1.2;
	protected final int FPS = 24;
	protected final double softenDistance = 0.01;
	
	
	protected Particle[] particle = new Particle[N];
	protected ArrayList<Particle> particleList = new ArrayList<Particle>();
	protected Node root;
	public Timer frames;
	//private int cnt = 0;
	
	public Galaxy() {
		super();
		setPreferredSize(new Dimension(SIZE, SIZE));
		frames = new Timer(1000 / FPS, this);
		createParticles();
		frames.start();
	}
	
	private void createParticles() {
		double v = 5;
		double v2 = 2.5;
		double alpha = .3;
		Random rnd = new Random();
		double r, theta, x, y;
		double vx, vy, r_;
		int temp = 0;
		for (int i=0; i<2000; i++) {
			r = rnd.nextDouble() / 4.0;
			theta = rnd.nextDouble() * Math.PI * 2.0;
			x = (.4) + r * Math.cos(theta);
			y = (.4) + alpha * r * Math.sin(theta);
			r_ = Math.sqrt(((x+.1)-(1/2.0)) * ((x+.1)-(1/2.0)) + 
					((y+.1)-(1/2.0)) * ((y+.1)-(1/2.0)));
			vx = (-v * r_* Math.sin(theta));
			vy = (v * r_ * Math.cos(theta));
			particle[i] = new Particle(x, y);
			particle[i].setVelocity(vx, vy);
			temp++;
		}
		for (int i=2000; i<N; i++) {
			r = rnd.nextDouble() / 4.0;
			theta = rnd.nextDouble() * Math.PI * 2.0;
			x = (.75) + r * Math.cos(theta);
			y = (.75) + alpha * r * Math.sin(theta);
			double tempX = (.5) + r * Math.cos(theta);
			double tempY = (.5) + r * Math.cos(theta);
			r_ = Math.sqrt((tempX-(1/2.0)) * (tempX-(1/2.0)) + 
					(tempY-(1/2.0)) * (tempY-(1/2.0)));
			vx = (-v2 * r_* Math.sin(theta));
			vy = (v2 * r_ * Math.cos(theta));
			particle[i] = new Particle(x, y);
			particle[i].setVelocity(vx, vy);
			temp++;
		}
	}

	
	private void update() {
		root = new Node(new Vector(-1, -1), new Vector(1, 1), particle);
		//System.out.println(root.particles.length);
		
		Vector force = new Vector();
		Vector rValue;
		double distance;
	//	System.out.println(root.particles[0]);
		for (int i=0; i<N; i++) {

			force.set(0.0, 0.0);
			findPath(i, root);
			//System.out.println(particle[i] + "\tindex: " + i + "\tforce: " + force);
			for (Particle particleSector : particleList) {
				//System.out.println(particleSector.getMass());
				
				distance = particle[i].distance(particleSector);
				if (distance != 0) {
					//System.out.println(distance);
					rValue = Particle.getRValue(particle[i], particleSector);
					double denominator = (distance * distance) + (softenDistance * softenDistance);
					rValue.mult((MASS * particleSector.getMass()) / denominator);
				//System.out.println("index: " + i + "\tforce: " + force + "\tSector: " + particleSector + "Size: " + particleSector.getMass());
				//System.out.println(rValue);
					force.add(rValue);
				//System.out.println(particleSector);
				//System.out.println(particleSector.getMass());
				}
			}
			
			force.mult(-GCONSTANT * MASS);
			particle[i].giveForce(force, DT, particle[i].getMass());
			
			//System.out.println(sum);
			//System.out.println(particleList.size());
			particleList.clear();
		}
		root = null;
	}
	
	public void findPath(int i, Node node) {
		if (node.getSectorSize() > 1 && node.getSectorSize() != 0) {
			if ((node.width / particle[i].distance(node.getCenterOfMass())) > HRRATIO) {
				for (Node childrenNode : node.children)
					findPath(i, childrenNode);
			} else {
				particleList.add(mergeParticle(node));
				//System.out.println("Index: " + i + "\tSector created and added to particle list, merged " + node.particles.length + " particles.");
			}
			//System.out.println(node.width / particle[i].distance(node.centerOfMass));
		} else if (node.getSectorSize() == 1) {
			Particle single = new Particle(node.getCenterOfMass().x,
					node.getCenterOfMass().y);
			single.setMass(MASS);
			particleList.add(single);
				//System.out.println(node.particles[0]);
				//System.out.println("Index: " + i + "\tSector with only 1 particle, added to particle list " + node.particles[j]);
			
		}
		//System.out.println();
		//System.out.println(root.particles + " Item: " + i);

	}
	
	private Particle mergeParticle(Node node) {
		Particle sector = new Particle(node.getCenterOfMass().x, 
				node.getCenterOfMass().y);
		sector.setMass(node.getSectorSize());
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
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.BLACK);
		drawParticles(g);
	}
	
	public void actionPerformed(ActionEvent e) {
		update();
		repaint();
		//cnt++;
	}
}
