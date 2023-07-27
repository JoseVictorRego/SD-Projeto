import java.io.*;
import java.net.Socket;

public class cliente {

    public static void main(String[] args) throws InterruptedException{
        dns DNS = new dns();

        while (true) {
            try {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                
                // Conectando ao servidor
                Socket socket = new Socket(DNS.serveIp, DNS.serverPort);
                System.out.println("Conectado ao servidor.");

                // Solicitação para selecionar um arquivo
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Digite o seu nome: "); // Solicitação do nome do cliente
                String clientName = br.readLine();

                System.out.print("Digite o caminho completo do arquivo a ser enviado: ");
                String filePath = br.readLine();

                // Enviando o nome do cliente e o arquivo com o nome do arquivo
                sendFile(socket, clientName, filePath);

                // Fechando a conexão
                socket.close();
                System.out.println("Conexão encerrada.");
                br.readLine();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void sendFile(Socket socket, String clientName, String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("\n ->Arquivo não encontrado.\n");
            return;
        }

        // Enviar o nome do cliente antes do nome do arquivo e dos dados do arquivo
        OutputStream os = socket.getOutputStream();
        os.write(clientName.getBytes());
        os.write("\n".getBytes());

        String fileName = file.getName();
        os.write(fileName.getBytes());
        os.write("\n".getBytes());

        FileInputStream fis = new FileInputStream(file);

        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = fis.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }

        os.flush();
        fis.close();
        System.out.println("\n ->Arquivo enviado com sucesso.\n");
    }
}
