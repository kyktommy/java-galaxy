import java.awt.Container;
import java.awt.GridLayout;
import javax.swing.JFrame;

public class GalaxyApp {

	/**
	 * @param args
	 */
	public GalaxyApp() {
		JFrame f = new JFrame("Galaxy");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Galaxy galaxy1 = new Galaxy();
		galaxy1.setOpaque(true);
		//Galaxy galaxy2 = new Galaxy();
		//galaxy2.setOpaque(true);
		
		Container c = f.getContentPane();
		c.setLayout(new GridLayout(1, 0));
		c.add(galaxy1);
		//c.add(galaxy2);
		f.pack();
		f.setVisible(true);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GalaxyApp app = new GalaxyApp();
	}

}
