import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Interface extends JFrame {

    private BufferedImage backBuffer;
    private ImageIcon fundo = new ImageIcon("./images/fundo.png");
    private int FPS = 30;
    private int janelaW = 600;
    private int janelaH = 600;

    // Coordenadas do alvo
    int xBola = 50;
    int yBola = 30;

    public void atualizar() {
        moveAlvo();
    }

    public void desenharGraficos() {
        Graphics g = getGraphics();
        Graphics bbg = backBuffer.getGraphics();

        // Desenhando a imagem de fundo
        bbg.drawImage(fundo.getImage(), 0, 0, 600, 600, this);

        // Desenhando o alvo
        bbg.setColor(Color.RED);
        bbg.fillOval(xBola, yBola, 25, 25);

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

    public void moveAlvo() {
        if (this.yBola > 600) {
            this.yBola = 0;
        } else {
            this.yBola += 2;
        }
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
