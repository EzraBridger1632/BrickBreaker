public class Main {

	public static void main(String[] args) {
		Panel gamePanel = new Panel();
		Window gameWindow = new Window(gamePanel);
		gameWindow.add(gamePanel);
		gamePanel.requestFocus();
	}
	
	
}