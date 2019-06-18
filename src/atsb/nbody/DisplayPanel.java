package atsb.nbody;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/**
 * Display panel for the simulation results.
 * TODO: implement rotation correctly
 * TODO: panning
 * TODO: cutting plane for projection
 * 
 * @author austin
 */
public class DisplayPanel extends JPanel
		implements ActionListener, MouseListener, MouseMotionListener, MouseWheelListener {

	private static final long serialVersionUID = 1L;

	private static final Color backgroundColor = new Color(100, 100, 100);
	private static final Color axesColor = new Color(255, 255, 255);
	private static final Color radialColor = new Color(255, 255, 255, 16);
	private static final Color octTreeColor = new Color(255, 255, 255, 16);
	private static final Color[] colors = {
			new Color(181, 230, 29), // green
			new Color(255, 167, 108), // orange
			new Color(153, 217, 234), // blue
			new Color(200, 191, 231), // purple
			new Color(239, 228, 176), // tan/yellow
			new Color(255, 174, 201) // pink
	};
	
	// ref to the sim
	private NBodySimulation sim;
	
	// viewing plane parameters
	//private Vector3d center = new Vector3d(0, 0, 0);
	private Vector3d xaxis = new Vector3d(1, 0, 0);
	private Vector3d yaxis = new Vector3d(0, 1, 0);
	private Vector3d zaxis = new Vector3d(0, 0, 1);

	// zoom scale
	private double scale = 1;

	// for tracking mouse dragging
	private int xprev;
	private int yprev;

	// display options
	private boolean displayAxes = true;
	private boolean displayRadials = false;
	private boolean displayOctTree = false;

	public DisplayPanel(final NBodySimulation sim) {
		this.sim = sim;

		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);

		// right click context menu items
		JMenuItem toggleAxes = new JMenuItem("Toggle Axes");
		toggleAxes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				displayAxes = !displayAxes;
			}
		});
		JMenuItem toggleRadials = new JMenuItem("Toggle Radials");
		toggleRadials.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				displayRadials = !displayRadials;
			}
		});
		JMenuItem toggleOctTree = new JMenuItem("Toggle OctTree");
		toggleOctTree.setEnabled(sim.getMethod()==NBodySimulation.METHOD_BARNES_HUT);
		toggleOctTree.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				displayOctTree = !displayOctTree;
			}
		});
		
		// actually build the menu
		JPopupMenu context = new JPopupMenu();
		context.add(toggleAxes);
		context.add(toggleRadials);
		context.add(toggleOctTree);
		setComponentPopupMenu(context);
		
		// simulation timer
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				sim.step();
				repaint();
			}
		}, 0, 30);

		// auto-detect an appropriate default zoom
		double max = 0;
		for (int i = 0; i < sim.getSize(); i++) {
			max = Math.max(max, sim.getBodies().get(i).p.x);
			max = Math.max(max, sim.getBodies().get(i).p.y);
			max = Math.max(max, sim.getBodies().get(i).p.z);
		}
		scale = 300 / max;
	}

	private void doDrawing(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		// fill background
		g2d.setPaint(backgroundColor);
		g2d.fillRect(0, 0, getWidth(), getHeight());

		if(displayAxes) {
			drawAxes(g2d);
		}
		
		drawBodies(g2d);

		if (sim.getMethod() == NBodySimulation.METHOD_BARNES_HUT && displayOctTree) {
			drawOctTree(g2d, sim.getOctTree().getRoot());
		}
	}

	private void drawAxes(Graphics2D g2d) {
		g2d.setPaint(axesColor);
		line(g2d, new Vector3d(0, 0, 0), new Vector3d(40/scale, 0, 0));
		line(g2d, new Vector3d(0, 0, 0), new Vector3d(0, 40/scale, 0));
		line(g2d, new Vector3d(0, 0, 0), new Vector3d(0, 0, 40/scale));
	}

	private void drawBodies(Graphics2D g2d) {
		ArrayList<Body> results = sim.getBodies();
		for (int i = 0; i < sim.getSize(); i++) {
			g2d.setColor(colors[i % colors.length]); // rotate through colors
			point(g2d, results.get(i).p);
			
			if(displayRadials) {
				g2d.setColor(DisplayPanel.radialColor);
				line(g2d, results.get(i).p, new Vector3d(0, 0, 0));
			}
		}
	}

	private void drawOctTree(Graphics2D g2d, OctNode n) {
		g2d.setColor(octTreeColor);
		cube(g2d, new Vector3d(n.getLx(), n.getLy(), n.getLz()), new Vector3d(n.getUx(), n.getUy(), n.getUz()));
		
		ArrayList<OctNode> nodes = n.getChildren();
		for(int i=0; i<nodes.size(); i++) {
			drawOctTree(g2d, nodes.get(i));
		}
	}

	private void point(Graphics2D g2d, Vector3d p) {
		int w = getWidth();
		int h = getHeight();
		Vector3d v = new Vector3d(p);

		// coordinates on-screen
		double x = scale * v.dot(xaxis) + w / 2;
		double y = scale * v.dot(yaxis) + h / 2;

		// don't draw off-screen points
		if (x < 0 || x > w || y < 0 || y > h) {
			return;
		}

		g2d.fillOval((int) (x - 2), (int) (y - 2), 4, 4);
	}

	private void line(Graphics2D g2d, Vector3d p1, Vector3d p2) {
		int w = getWidth();
		int h = getHeight();
		Vector3d v1 = new Vector3d(p1);
		Vector3d v2 = new Vector3d(p2);

		// coordinates on-screen
		double x1 = scale * v1.dot(xaxis) + w / 2;
		double y1 = scale * v1.dot(yaxis) + h / 2;
		double x2 = scale * v2.dot(xaxis) + w / 2;
		double y2 = scale * v2.dot(yaxis) + h / 2;

		g2d.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
	}

	private void cube(Graphics2D g2d, Vector3d p1, Vector3d p2) {
		line(g2d, new Vector3d(p1.x, p1.y, p1.z), new Vector3d(p2.x, p1.y, p1.z));
		line(g2d, new Vector3d(p1.x, p1.y, p1.z), new Vector3d(p1.x, p2.y, p1.z));
		line(g2d, new Vector3d(p1.x, p1.y, p1.z), new Vector3d(p1.x, p1.y, p2.z));
		
		line(g2d, new Vector3d(p2.x, p1.y, p1.z), new Vector3d(p2.x, p2.y, p1.z));
		line(g2d, new Vector3d(p2.x, p1.y, p1.z), new Vector3d(p2.x, p1.y, p2.z));
		
		line(g2d, new Vector3d(p1.x, p2.y, p1.z), new Vector3d(p2.x, p2.y, p1.z));
		line(g2d, new Vector3d(p1.x, p2.y, p1.z), new Vector3d(p1.x, p2.y, p2.z));
		
		line(g2d, new Vector3d(p1.x, p1.y, p2.z), new Vector3d(p2.x, p1.y, p2.z));
		line(g2d, new Vector3d(p1.x, p1.y, p2.z), new Vector3d(p1.x, p2.y, p2.z));
		
		line(g2d, new Vector3d(p2.x, p2.y, p1.z), new Vector3d(p2.x, p2.y, p2.z));
		line(g2d, new Vector3d(p1.x, p2.y, p2.z), new Vector3d(p2.x, p2.y, p2.z));
		line(g2d, new Vector3d(p2.x, p1.y, p2.z), new Vector3d(p2.x, p2.y, p2.z));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}

	public void actionPerformed(ActionEvent e) {
		repaint();
	}

	public void mouseDragged(MouseEvent e) {
		Matrix3d r = new Matrix3d();
		r.setRow0(xaxis);
		r.setRow1(yaxis);
		r.setRow2(zaxis);

		Matrix3d m = new Matrix3d();
		m.rotX((yprev - e.getY()) / 100d);
		r.mul(m);
		m.rotY((xprev - e.getX()) / 200d);
		r.mul(m);
		// m.rotZ(0);
		// r.mul(m);

		r.getRow0(xaxis);
		r.getRow1(yaxis);
		r.getRow2(zaxis);

		xprev = e.getX();
		yprev = e.getY();

		repaint();
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		scale += scale * e.getWheelRotation() / 20d;
		repaint();
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		// need to capture the initial mouse click
		xprev = e.getX();
		yprev = e.getY();
	}

	public void mouseReleased(MouseEvent e) {
	}

}
