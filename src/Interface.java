
// Bibliotecas para gerar os gráficos do jogo
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Interface extends JFrame {

    private BufferedImage backBuffer;

    // Icone das imagens no jogo
    private ImageIcon fundo = new ImageIcon("./images/fundo.png");
    private ImageIcon nave = new ImageIcon("./images/space.png");
    private ImageIcon nuvem = new ImageIcon("./images/nuvens.png");
    private ImageIcon meteoro = new ImageIcon("./images/meteoro.png");

    // Definição de algumas características de renderização e tamanho da janela do
    // jogo
    private int FPS = 30;
    private int janelaW = 600, janelaH = 600;

    // Objetos que serão inseridos inicialmente no jogo
    private ArrayList<Alvos> alvosDir = new ArrayList<Alvos>();
    private ArrayList<Alvos> alvosEsq = new ArrayList<Alvos>();
    private boolean colidiu;
    private Lancador lancador;
    private ExecutorService contagem = Executors.newCachedThreadPool();

    // Thread que roda em paralelo com a interface para gerar os alvos
    Runnable r1 = () -> {
        while (true) {
            try {
                Thread.sleep(new Random().nextInt(2000) + 1000);
                alvosEsq.add(new Alvos(new Pontos(60, 0), new Pontos(60, 600)));
                Thread.sleep(new Random().nextInt(2000) + 500);
                alvosDir.add(new Alvos(new Pontos(490, 0), new Pontos(490, 600)));

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    };

    public void atualizar() {

    }

    public void desenharGraficos() {
        Graphics g = getGraphics();
        Graphics bbg = backBuffer.getGraphics();

        // Desenhando a imagem de fundo
        bbg.drawImage(fundo.getImage(), 0, 0, 600, 600, this);

        // Desenhando a nuvem
        bbg.drawImage(nuvem.getImage(), 50, 100, 500, 271, this);

        // Lógica para pegar a localização dos alvos e gerar na tela
        for (int i = 0; i < alvosDir.size(); i++) {
            bbg.drawImage(meteoro.getImage(), alvosDir.get(i).getLocalizacao().getX(),
                    alvosDir.get(i).getLocalizacao().getY(), 50, 50, this);

            // Setar um alvo para o lançador
            if (lancador.getAlvo() == null) {
                lancador.setAlvo(alvosDir.get(i));
            }

            // Verificando colisão
            colidiu = colisao(alvosDir.get(i).getLocalizacao().getX(), alvosDir.get(i).getLocalizacao().getY(), 50, 50,
                    lancador.getTiro().getLocalizacao().getX(), lancador.getTiro().getLocalizacao().getY(), 50, 50);

            // Verificando se o alvo chegou até o final da tela
            if (alvosDir.get(i).getChegouDestino() || colidiu) {
                alvosDir.get(i).setAtingido(colidiu);

                if (colidiu) {
                    lancador.getTiro().setContatoAlvo(colidiu);
                }
                alvosDir.remove(i);
                i--;
            }
        }

        for (int i = 0; i < alvosEsq.size(); i++) {
            bbg.drawImage(meteoro.getImage(), alvosEsq.get(i).getLocalizacao().getX(),
                    alvosEsq.get(i).getLocalizacao().getY(), 50, 50, this);

            if (lancador.getAlvo() == null) {
                lancador.setAlvo(alvosEsq.get(i));
            }

            colidiu = colisao(alvosEsq.get(i).getLocalizacao().getX(), alvosEsq.get(i).getLocalizacao().getY(), 50, 50,
                    lancador.getTiro().getLocalizacao().getX(), lancador.getTiro().getLocalizacao().getY(), 20, 20);

            if (alvosEsq.get(i).getChegouDestino() || colidiu) {
                alvosEsq.get(i).setAtingido(colidiu);

                if (colidiu) {
                    lancador.getTiro().setContatoAlvo(colidiu);
                }
                alvosEsq.remove(i);
                i--;
            }
        }

        // Desenhando o tiro
        bbg.setColor(Color.YELLOW);

        bbg.fillOval(lancador.getTiro().getLocalizacao().getX(), lancador.getTiro().getLocalizacao().getY(), 15,
                15);

        // Desenhando o lançador
        bbg.drawImage(nave.getImage(), 287, 540, 50, 50, this);

        g.drawImage(backBuffer, 0, 0, this);
    }

    // Lógica para colisão
    public boolean colisao(int obj1X, int obj1Y, int obj1W, int obj1H,
            int obj2X, int obj2Y, int obj2W, int obj2H) {
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
        alvosEsq.add(new Alvos(new Pontos(60, 0), new Pontos(60, 600)));
        alvosDir.add(new Alvos(new Pontos(490, 0), new Pontos(490, 600)));
        contagem.execute(r1);
        lancador = new Lancador(new Pontos(284, 575), alvosEsq.get(0));

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
