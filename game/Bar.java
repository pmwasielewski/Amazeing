import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.KeyStroke;


public class Bar extends JMenuBar {
    GamePanel graPanel;

    JMenu gra;

    JMenuItem nowaGra;
    JMenuItem poddanie;
    JMenuItem wyjscie;

    JMenu ustawienia;

    JMenu wyglad;
    JMenuItem tlo;
    JMenuItem sciany;
    JMenu rozmiar;
    JTextField szerokosc;
    JTextField wysokosc;
    JMenu szerokoscMenu;
    JMenu wysokoscMenu;

    JMenu pomoc;

    JMenuItem zasady;
    JMenuItem autor;

    JMenu ruchy;

    JMenuItem lewo;
    JMenuItem prawo;
    JMenuItem gora;
    JMenuItem dol;

    public Bar(GamePanel graPanel) {
        super();

        this.graPanel = graPanel;

        gra = new JMenu("Gra");
        ustawienia = new JMenu("Ustawienia");
        pomoc = new JMenu("Pomoc");
        ruchy = new JMenu("Ruchy");
        add(gra);
        add(ustawienia);
        add(ruchy);
        add(Box.createHorizontalGlue());
        add(pomoc);

        nowaGra = new JMenuItem("Nowa gra");
        poddanie = new JMenuItem("Poddaj się");
        wyjscie = new JMenuItem("Zakończ");
        gra.add(nowaGra);
        gra.add(poddanie);
        gra.add(new JSeparator());
        gra.add(wyjscie);

        wyjscie.setMnemonic(KeyEvent.VK_K);
        wyjscie.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, ActionEvent.CTRL_MASK));
        wyjscie.addActionListener(e -> System.exit(0));

        nowaGra.setMnemonic(KeyEvent.VK_N);
        nowaGra.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        nowaGra.addActionListener(e -> {
            graPanel.restart();
        });

        poddanie.setMnemonic(KeyEvent.VK_P);
        poddanie.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        poddanie.addActionListener(e -> {
            graPanel.lose();
        });

        prawo = new JMenuItem("Prawo");
        lewo = new JMenuItem("Lewo");
        gora = new JMenuItem("Góra");
        dol = new JMenuItem("Dół");

        ruchy.add(prawo);
        ruchy.add(lewo);
        ruchy.add(gora);
        ruchy.add(dol);

        prawo.addActionListener(e -> {
            graPanel.moveRight();
        });
        lewo.addActionListener(e -> {
            graPanel.moveLeft();
        });
        gora.addActionListener(e -> {
            graPanel.moveUp();
        });
        dol.addActionListener(e -> {
            graPanel.moveDown();
        }
        );

        zasady = new JMenuItem("O aplikacji");
        autor = new JMenuItem("O autorze");

        pomoc.add(zasady);
        pomoc.add(autor);

        zasady.addActionListener(e -> {
            graPanel.showRules();
        });

        autor.addActionListener(e -> {
            graPanel.showAuthor();
        });

        wyglad = new JMenu("Wygląd");
        tlo = new JCheckBoxMenuItem("Ciemne tło");
        sciany = new JCheckBoxMenuItem("Ciemne ściany");

        ustawienia.add(wyglad);
        wyglad.add(tlo);
        wyglad.add(sciany);

        tlo.addActionListener(e -> {
            graPanel.changeBackground();
        });

        sciany.addActionListener(e -> {
            graPanel.changeWalls();
        });

        rozmiar = new JMenu("Rozmiar");
        ustawienia.add(rozmiar);
        szerokosc = new JTextField(10);
        wysokosc = new JTextField(10);
        szerokoscMenu = new JMenu("Szerokość");
        wysokoscMenu = new JMenu("Wysokość");
        rozmiar.add(szerokoscMenu);
        rozmiar.add(wysokoscMenu);
        szerokoscMenu.add(szerokosc);
        wysokoscMenu.add(wysokosc);

        szerokosc.addActionListener(e -> {
            graPanel.changeWidth(Integer.parseInt(szerokosc.getText()));
        });

        wysokosc.addActionListener(e -> {
            graPanel.changeHeight(Integer.parseInt(wysokosc.getText()));
        });

    }
    
}
