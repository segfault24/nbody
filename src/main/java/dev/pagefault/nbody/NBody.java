package dev.pagefault.nbody;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class NBody {

	public NBody() {
		initControlUI();
	}

	private void initControlUI() {
		final JFrame control = new JFrame();
		control.setTitle("N-Body Control");
		control.setSize(240, 400);
		control.setResizable(false);
		control.setLocation(20, 20);
		//control.setLocationRelativeTo(null);
		control.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final MainPanel surface = new MainPanel();
		control.add(surface);

		control.setVisible(true);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new NBody();
			}
		});
	}
}
