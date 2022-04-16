
// Bibliotecas para gerar os gráficos do jogo
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Interface extends JFrame {

    // Icone das imagens no jogo
    private BufferedImage backBuffer;
    private ImageIcon fundo = new ImageIcon("./images/fundo.png");
    private ImageIcon nave = new ImageIcon("./images/space.png");
    private ImageIcon nuvem = new ImageIcon("./images/nuvens.png");
    private ImageIcon meteoro = new ImageIcon("./images/meteoro.png");
    private int FPS = 30;
    private int janelaW = 600, janelaH = 600;
    private ExecutorService contagem = Executors.newCachedThreadPool();

    // Objetos que serão inseridos inicialmente no jogo
    private ArrayList<Alvos> alvos = new ArrayList<Alvos>();
    private boolean colidiu;
    private Lancador lancador;

    // Thread que roda em paralelo com a interface para gerar os alvos
    Runnable r1 = () -> {
        while (true) {
            try {
                alvos.add(new Alvos(new Pontos(60, 0), new Pontos(60, 600)));
                Thread.sleep(new Random().nextInt(2000) + 300);// 300/500
                alvos.add(new Alvos(new Pontos(490, 0), new Pontos(490, 600)));
                Thread.sleep(new Random().nextInt(2000) + 700);// 700/1000

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    };

    public void atualizar() {
        if (!this.lancador.isAlive()) {
            Thread.currentThread().interrupt();
        }
    }

    public void desenharGraficos() {
        Graphics g = getGraphics();
        Graphics bbg = backBuffer.getGraphics();
        bbg.drawImage(fundo.getImage(), 0, 0, 600, 600, this); // Desenhando a imagem de fundo
        bbg.drawImage(nuvem.getImage(), 50, 100, 500, 271, this); // Desenhando a nuvem

        bbg.setColor(Color.WHITE);
        bbg.setFont(new Font("helvica", Font.BOLD, 20));
        bbg.drawString("Munição: " + this.lancador.statusCarregador(), 250, 70);

        // Lógica para pegar a localização dos alvos e gerar na tela
        for (int i = 0; i < alvos.size(); i++) {
            bbg.drawImage(meteoro.getImage(), (int) alvos.get(i).getLocalizacao().getX(),
                    (int) alvos.get(i).getLocalizacao().getY(), 50, 50, this);

            // Setar um alvo para o lançador
            if (!alvos.isEmpty() && lancador.getAlvo() == null) {
                if (!alvos.get(i).getErrou()) {
                    lancador.setAlvo(alvos.get(i));
                }
            }

            // Verificando colisão
            if (lancador.getTiro() != null) {
                colidiu = colisao(lancador.getTiro().getLocalizacao().getX(),
                        lancador.getTiro().getLocalizacao().getY(), 15, 15, alvos.get(i).getLocalizacao().getX(),
                        alvos.get(i).getLocalizacao().getY(), 50, 50);

            }

            // Verificando se o alvo chegou até o final da tela
            if (alvos.get(i).getChegouDestino() || colidiu) {
                alvos.get(i).setAtingido(colidiu);

                if (alvos.get(i).getChegouDestino() && lancador.statusCarregador() != 0) {
                    lancador.retirarMunicao();
                }

                if (colidiu) {
                    lancador.getTiro().setContatoAlvo(colidiu);
                }
                alvos.remove(i);
                i--;
            }
        }

        // Desenhando o tiro
        if (lancador.getTiro() != null) {
            bbg.setColor(Color.YELLOW);
            bbg.fillOval((int) lancador.getTiro().getLocalizacao().getX(),
                    (int) lancador.getTiro().getLocalizacao().getY(), 15,
                    15);
        }

        // Desenhando o lançador
        bbg.drawImage(nave.getImage(), 287, 540, 50, 50, this);

        g.drawImage(backBuffer, 0, 0, this);
    }

    // Lógica para colisão
    public boolean colisao(double obj1X, double obj1Y, double obj1W, double obj1H,
            double obj2X, double obj2Y, double obj2W, double obj2H) {
        if ((obj1X >= obj2X && obj1X <= obj2X + obj2W)
                && (obj1Y >= obj2Y && obj1Y <= obj2Y + obj2H)) {
            return true;
        } else if ((obj1X + obj1W >= obj2X && obj1X + obj1W <= obj2X + obj2W)
                && (obj1Y >= obj2Y && obj1Y <= obj2Y + obj2H)) {
            return true;
        } else if ((obj1X >= obj2X && obj1X <= obj2X + obj2W)
                && (obj1Y + obj1H >= obj2Y && obj1Y + obj1H <= obj2Y + obj2H)) {
            return true;
        } else if ((obj1X + obj1W >= obj2X && obj1X + obj1W <= obj2X + obj2W)
                && (obj1Y + obj1H >= obj2Y && obj1Y + obj1H <= obj2Y + obj2H)) {
            return true;
        } else {
            return false;
        }
    }

    public void inicializar() {

        // Alvos iniciais e tarefa r1 inicializada
        contagem.execute(r1);
        lancador = new Lancador(new Pontos(284, 575));

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
                break;
            }
        }
        JOptionPane.showMessageDialog(null, "Jogo finalizado: sem munições!");
    }
}
