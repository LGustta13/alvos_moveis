import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Interface extends JFrame {

    private BufferedImage backBuffer;
    private ImageIcon fundo = new ImageIcon("./images/fundo.png");
    private int FPS = 30;
    private int janelaW = 500;
    private int janelaH = 500;

    public void atualizar() {

    }

    public void desenharGraficos() {
        Graphics g = getGraphics();
        Graphics bbg = backBuffer.getGraphics();
        bbg.drawImage(fundo.getImage(), 100, 100, 500, 500, this);

        g.drawImage(backBuffer, 0, 0, this);
    }

    public void inicializar() {
        setTitle("Alvos móveis");
        setSize(janelaW, janelaH);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setVisible(true);
        backBuffer = new BufferedImage(janelaW, janelaH, BufferedImage.TYPE_INT_RGB);
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
