package atsb.nbody;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * TODO: implement simulation parameter controls
 * 
 * @author austin
 */
public class MainPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public MainPanel() {
		JButton b = new JButton();
		
		b.setText("General");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Thread() {public void run() {createDisplayUI(testSim(1000));}}.start();
			}
		});
		add(b);

		b = new JButton();
		b.setText("Solar System");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Thread() {public void run() {createDisplayUI(testSolarSim());}}.start();
			}
		});
		add(b);
		
		b = new JButton();
		b.setText("Galaxy");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Thread() {public void run() {createDisplayUI(testGalaxySim(1000));}}.start();
			}
		});
		add(b);
		
		b = new JButton();
		b.setText("Barnes-Hut");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Thread() {public void run() {createDisplayUI(testBarnesHut(10000));}}.start();
			}
		});
		add(b);
	}

	private NBodySimulation testSim(int n) {
		ArrayList<Body> bodies = new ArrayList<Body>();
		
		Random r = new Random();
		r.setSeed(System.currentTimeMillis());
		for (int i = 0; i < n; i++) {
			Body nb = new Body();
			nb.m = 100 * r.nextDouble();
			
			Vector3d v = new Vector3d(r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5);
			v.normalize();
			v.scale(1000 * r.nextDouble());
			
			nb.p.x = v.x;
			nb.p.y = v.y;
			nb.p.z = v.z;
			nb.v.x = (0.0001*(r.nextDouble() - 0.5));
			nb.v.y = (0.0001*(r.nextDouble() - 0.5));
			nb.v.z = (0.0001*(r.nextDouble() - 0.5));
			bodies.add(nb);
		}

		NBodySimulation sim = new NBodySimulation();
		sim.setTimeStep(60 * 60);
		sim.setBodies(bodies);

		return sim;
	}

	private NBodySimulation testSolarSim() {
		//Random rand = new Random();
		//rand.setSeed(System.currentTimeMillis());
		
		//double theta = rand.nextDouble()*2*Math.PI;
		
		//Body b = new Body();
		//b.m = 2e30 * (99 * Math.random() + 1);
		//b.p.x = (5e20 * Math.random()) * Math.cos(theta);
		//b.p.y = (5e20 * Math.random()) * Math.sin(theta);
		
		ArrayList<Body> bodies = new ArrayList<Body>();
		bodies.add(new Body(1.9891e30, new Vector3d(0, 0, 0), new Vector3d(0, 0, 0))); // sun
		bodies.add(new Body(3.302e23, new Vector3d(5.7909e10, 0, 0), new Vector3d(0, 4.79e4, 0))); // mercury
		bodies.add(new Body(4.87e24, new Vector3d(1.08e11, 0, 0), new Vector3d(0, 3.50e4, 0))); // venus
		bodies.add(new Body(5.97e24, new Vector3d(1.50e11, 0, 0), new Vector3d(0, 2.98e4, 0))); // earth
		bodies.add(new Body(6.42e23, new Vector3d(2.28e11, 0, 0), new Vector3d(0, 2.41e4, 0))); // mars
		bodies.add(new Body(1.90e27, new Vector3d(7.78e11, 0, 0), new Vector3d(0, 1.31e4, 0))); // jupiter
		bodies.add(new Body(5.69e26, new Vector3d(1.43e12, 0, 0), new Vector3d(0, 9.67e3, 0))); // saturn
		bodies.add(new Body(8.68e25, new Vector3d(2.87e12, 0, 0), new Vector3d(0, 6.84e3, 0))); // uranus
		bodies.add(new Body(1.02e26, new Vector3d(4.50e12, 0, 0), new Vector3d(0, 5.48e3, 0))); // neptune

		NBodySimulation sim = new NBodySimulation();
		sim.setTimeStep(7 * 24 * 60 * 60);
		sim.setInterpolation(24 * 60);
		sim.setBodies(bodies);
		
		return sim;
	}

	private NBodySimulation testGalaxySim(int n) {
		// this simulation isn't working very well because it does not include
		// anything like dark matter to approximate real world observations
		
		ArrayList<Body> bodies = new ArrayList<Body>();
		//bodies.add(new Body(8.15e36, new Point3d(0, 0, 0), new Vector3d(0, 0, 0))); // SMBH galactic center
		bodies.add(new Body(2.2e41, new Vector3d(0, 0, 0), new Vector3d(0, 0, 0))); // SMBH galactic center*
		
		//bodies.add(new Body(1.9891e30, new Point3d(2.469e20, 0, 0), new Vector3d(0, 2.3e5, 0))); // sol
		
		Random rand = new Random();
		rand.setSeed(System.currentTimeMillis());
		
		for(int i=0; i<n; i++) {
			double theta = rand.nextDouble()*2*Math.PI;
			
			Body b = new Body();
			b.m = 2e30 * (99 * Math.random() + 1);
			b.p.x = (5e20 * Math.random()) * Math.cos(theta);
			b.p.y = (5e20 * Math.random()) * Math.sin(theta);
			b.p.z = 1e19 * (Math.random() - 0.5);
			
			Vector3d r = new Vector3d(b.p.x, b.p.y, b.p.z);
			Vector3d v = new Vector3d();
			v.cross(r, new Vector3d(0, 0, 1));
			v.normalize();
			
			v.scale(Math.sqrt(NBodySimulation.G*(2.5e41)/r.length()));
			b.v = v;
			
			bodies.add(b);
		}
		
		NBodySimulation sim = new NBodySimulation();
		sim.setTimeStep(1000000L * 365L * 24L * 60L * 60L);
		sim.setInterpolation(1);
		sim.setMethod(NBodySimulation.METHOD_BARNES_HUT);
		sim.setBucketSize(10);
		sim.setBodies(bodies);
		
		return sim;
	}
	
	private NBodySimulation testBarnesHut(int n) {
		NBodySimulation sim = testSim(n);
		sim.setMethod(NBodySimulation.METHOD_BARNES_HUT);
		return sim;
	}
	
	private void createDisplayUI(NBodySimulation sim) {

		// create the display window
		final JFrame displayFrame = new JFrame();
		displayFrame.setTitle("N-Body Display");
		displayFrame.setSize(1280, 720);
		displayFrame.setLocationRelativeTo(null);
		displayFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		final DisplayPanel displayPanel = new DisplayPanel(sim);
		displayFrame.add(displayPanel);
		displayFrame.setVisible(true);
	}
}
