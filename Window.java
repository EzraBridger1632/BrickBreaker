import javax.swing.*;
import java.awt.*;

public class Window extends JFrame{
	private String name = "Breakout";
	
	public Window(Panel panel){
		//JFrame window = new JFrame(name);
		
		//setSize(width, height);
		setTitle(name);
		add(panel);
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}