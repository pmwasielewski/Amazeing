import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;


public class MenuPanel extends JPanel {
    BufferedImage background;
    Okno okno;
    BufferedImage title;
    //Tworze wszystko w oparciu o propocje okna do wielkosci obrazu tła
    int[] scale = new int[2];
    JButton playButton;
    BufferedImage playImage;
    BufferedImage FocusedPlayImage;
    boolean focused = false;

    private int[] scaledParams (BufferedImage img) {
        int[] params = new int[2];
        params[0] = img.getWidth() * scale[0];
        params[1] = img.getHeight() * scale[1];
        return params;
    }
    
    public MenuPanel(Okno okno) {
        super();
        setLayout(null);
        setBackground(Color.LIGHT_GRAY);

        try {
            background = ImageIO.read(new File("./data/images/kafalekBezKrawedzi.png"));
            title = ImageIO.read(new File("./data/images/Title.png"));
            playImage = ImageIO.read(new File("./data/images/Play.png"));
            FocusedPlayImage = ImageIO.read(new File("./data/images/FocusedPlay.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.okno = okno;
        scale[0] = okno.getWidth() / background.getWidth();
        scale[1] = okno.getHeight() / background.getHeight();    
        
        playButton = new JButton(new ImageIcon(playImage));
        playButton.setText(null);
        playButton.setBorderPainted(false);
        playButton.setContentAreaFilled(false);
        playButton.setFocusPainted(false);
        add(playButton);

        playButton.addActionListener(e -> {
            //setVisible(false);
            System.out.println("Play button clicked");
            //okno.cardLayout.show(okno.statesPanels, "game");
            okno.gamePanel.createRound(10, 10, false, false);
        });

        playButton.addMouseListener(
            new MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    focused = true;
                    repaint();
                };
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    focused = false;
                    repaint();
                };
            }
        );

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        scale[0] = okno.getWidth() / background.getWidth();
        scale[1] = okno.getHeight() / background.getHeight();

        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2d.drawImage(background, 0, 0, okno.getWidth(), okno.getHeight(), null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        int[] titleParams = scaledParams(title);
        g2d.drawImage(title, okno.getWidth() / 2 - titleParams[0] / 2, okno.getHeight() / 3 - titleParams[1] / 2, titleParams[0], titleParams[1], null);

        int[] playParams = scaledParams(playImage);
        //playButton.setBounds(okno.getWidth() / 2 - playParams[0] / 2, okno.getHeight() / 2 - playParams[1] / 2, playParams[0], playParams[1]);
        ImageIcon resizedPlay;
        if (focused){
            resizedPlay = new ImageIcon(FocusedPlayImage.getScaledInstance(playParams[0], playParams[1], Image.SCALE_SMOOTH));
        }
        else {
            resizedPlay = new ImageIcon(playImage.getScaledInstance(playParams[0], playParams[1], Image.SCALE_SMOOTH));
        }
            
        playButton.setIcon(resizedPlay);
        playButton.setBounds(okno.getWidth() / 2 - playParams[0] / 2, okno.getHeight() / 2 - playParams[1] / 2, playParams[0], playParams[1]);
        playButton.revalidate(); // Odśwież układ
    }

}
