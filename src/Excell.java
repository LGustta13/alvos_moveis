// Classe criada para ser possível enviar os dados ao Excell e facilitar as análises
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Excell {
    private FileWriter arq;
    private PrintWriter gravarArq;
    public Excell(int nArquivo) throws IOException {
        this.arq= new FileWriter("./dados/AlvoNumero"+nArquivo+".txt");
        this.gravarArq = new PrintWriter(this.arq);
    }

    public void escrever(double[] _v, double[] _A, double[] _y, double[] _yhat, double _vel, double _var){
        gravarArq.printf("+--Medições / Variâncias / Restrições--+%n");
        for (int i = 0; i<_y.length; i++) {
            gravarArq.printf("%s    %s    %s %n", _y[i], _v[i], _A[i]);
        }
        gravarArq.printf("+-------------+%n");
    }

    public void fechar() throws IOException {
        this.arq.close();
    };
}
