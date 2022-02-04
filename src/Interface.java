import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Interface extends JFrame {

    private BufferedImage backBuffer;
    private ImageIcon fundo = new ImageIcon("./images/fundo.png");
    private ImageIcon nave = new ImageIcon("./images/space.png");
    private int FPS = 30;
    private int janelaW = 600, janelaH = 600;
    private boolean esq = false, dir = false;

    private Alvos alvo1;
    private Alvos alvo2;
    private Lancador lancador = new Lancador(new Pontos(284, 575));

    public void atualizar() {

    }

    public void desenharGraficos() {
        Graphics g = getGraphics();
        Graphics bbg = backBuffer.getGraphics();

        // Desenhando a imagem de fundo
        bbg.drawImage(fundo.getImage(), 0, 0, 600, 600, this);

        // Desenhando o alvo
        bbg.setColor(Color.RED);

        if (esq == false) {
            alvo1 = new Alvos(new Pontos(70, 0), new Pontos(70, 600));
            esq = true;
        } else if (alvo1.getChegouDestino()) {
            esq = false;
        }

        if (dir == false) {
            alvo2 = new Alvos(new Pontos(505, 0), new Pontos(505, 600));
            dir = true;
        } else if (alvo2.getChegouDestino()) {
            dir = false;
        }

        bbg.fillOval(alvo1.getLocalizacao().getX(), alvo1.getLocalizacao().getY(), 25, 25);
        bbg.fillOval(alvo2.getLocalizacao().getX(), alvo2.getLocalizacao().getY(), 25, 25);

        // Desenhando o tiro
        bbg.setColor(Color.YELLOW);
        // bbg.fillOval(lancador.getTiro().getLocalizacao().getX(),
        // lancador.getTiro().getLocalizacao().getY(), 15, 15);

        // Desenhando o lançador
        bbg.drawImage(nave.getImage(), 287, 550, 50, 50, this);

        g.drawImage(backBuffer, 0, 0, this);
    }

    public void inicializar() {
        setTitle("Alvos móveis");
        setSize(janelaW, janelaH);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setVisible(true);
        backBuffer = new BufferedImage(janelaW, janelaH, BufferedImage.TYPE_INT_RGB); // Janela do Windows
    }

    public void run() {
        inicializar();
        while (true) {
            atualizar();
            desenharGraficos();
            try {
                Thread.sleep(1000 / FPS);
            } catch (Exception e) {
                System.out.println("Thread interrompida!");
            }

        }
    }
}
