import java.io.*;
import java.net.Socket;

public class cliente {

    public static void main(String[] args) throws InterruptedException{
        
        //Dados do DNS
        dns DNS = new dns(); 

        //Funções de Envio de dados
        funcao Funcao = new funcao();

        while (true) { //Loop Basico para o Cliente ficar sempre em conexão com o servidor
            try {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                
                // Conectando ao servidor
                Socket socket = new Socket(DNS.serveIp, DNS.serverPort);
                System.out.println("Conectado ao servidor.");

                // Solicitação para selecionar um arquivo
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

                // Solicitação do nome do cliente
                System.out.print("Digite o seu nome: "); 
                String clientName = br.readLine();

                // Solicitação do caminho do arquivo a ser enviado
                System.out.print("Digite o caminho completo do arquivo a ser enviado: ");
                String filePath = br.readLine();

                // Enviando o nome do cliente e o arquivo com o nome do arquivo
                Funcao.enviarArquivo(socket, clientName, filePath);

                // Fechando a conexão
                socket.close();
                System.out.println("Conexão encerrada.");

                br.readLine();

            } catch (IOException e) { // Caso não haja conexão com o servidor
                System.out.println("Servidor não Encontrado."); break;
            }
        }
    }
}
