import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.InputMap;
import javax.swing.Action;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;
import javax.swing.ActionMap;
import java.awt.*;

public class Okno extends JFrame {
    private boolean fullScreen = true;
    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    protected CardLayout cardLayout;
    protected MenuPanel menuPanel;
    protected GamePanel gamePanel;
    protected LosePanel losePanel;
    protected WinPanel winPanel;
    JPanel statesPanels;
    
    

    {
        if (gd.isFullScreenSupported()) {
            setUndecorated(true);
            gd.setFullScreenWindow(this);
        } else {
            setSize(300, 200);
            setLocationRelativeTo(null);
        }

        setUpKeyBindings();
        
    }

    public void fullScreen() {
        if (gd.isFullScreenSupported()) {
            if (fullScreen) {
                dispose();
                setUndecorated(false);
                setSize(800, 600);
                setLocationRelativeTo(null);
                setVisible(true);
                fullScreen = false;
            } else {
                dispose();
                setUndecorated(true);
                gd.setFullScreenWindow(this);
                setVisible(true);
                fullScreen = true;
            }
            
        }
    }

    private void setUpKeyBindings() {
        
        Action fullScreenAction = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                fullScreen();
            }
        };

        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getRootPane().getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("F11"), "fullScreen");
        actionMap.put("fullScreen", fullScreenAction);
    }





    public Okno() {
        super("Okno");
        setTitle("aMAZEing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(600, 400));
        
        cardLayout = new CardLayout();
        statesPanels = new JPanel(cardLayout);
        menuPanel = new MenuPanel(this);
        gamePanel = new GamePanel(this);
        losePanel = new LosePanel(this);
        winPanel = new WinPanel(this);
        statesPanels.add(menuPanel, "menu");
        statesPanels.add(gamePanel, "game");
        statesPanels.add(losePanel, "lose");
        statesPanels.add(winPanel, "win");

        setContentPane(statesPanels);
        cardLayout.show(statesPanels, "menu");

        //uswienie głównego panelu
        

        setVisible(true);
    }
}