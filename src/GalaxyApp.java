import javax.swing.JFrame;

public class GalaxyApp {

	/**
	 * @param args
	 */
	public GalaxyApp() {
		JFrame f = new JFrame("Galaxy");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Galaxy galaxy = new Galaxy();
		galaxy.setOpaque(true);
		f.getContentPane().add(galaxy);
		f.pack();
		f.setVisible(true);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GalaxyApp app = new GalaxyApp();
	}

}
