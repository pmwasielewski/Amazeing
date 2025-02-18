import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.Font;

public class WinPanel  extends JPanel{
    Okno okno;
    int moves;
    long time;
    JLabel winLabel;
    JLabel movesLabel;
    JLabel timeLabel;

    public WinPanel(Okno okno) {
        super();
        this.okno = okno;
        setBackground(Color.GRAY);

        winLabel = new JLabel("Wygrana!");
        winLabel.setFont(new Font("Arial", Font.BOLD, 30));
        winLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    }

    public void getStats(int moves, long time) {
        this.moves = moves;
        this.time = time;
        time = time / 1000;

        removeAll();
        add(winLabel);

        movesLabel = new JLabel("Liczba ruchów: " + moves);
        movesLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        movesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(movesLabel);

        timeLabel = new JLabel("Czas gry: " + time + " sekund");
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(timeLabel);

    }

}
