import java.io.*;
import java.net.Socket;

public class cliente {

    public static void main(String[] args) {
        String serverAddress = "127.0.0.1"; // Endereço IP do servidor
        int serverPort = 4000; // Porta do servidor

        try {
            // Conectando ao servidor
            Socket socket = new Socket(serverAddress, serverPort);
            System.out.println("Conectado ao servidor.");

            // Solicitação para selecionar um arquivo
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Digite o caminho completo do arquivo a ser enviado: ");
            String filePath = br.readLine();

            // Enviando o arquivo com o nome do arquivo
            sendFile(socket, filePath);

            // Fechando a conexão
            socket.close();
            System.out.println("Conexão encerrada.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendFile(Socket socket, String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("Arquivo não encontrado.");
            return;
        }

        // Enviar o nome do arquivo antes dos dados do arquivo
        String fileName = file.getName();
        OutputStream os = socket.getOutputStream();
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
        System.out.println("Arquivo enviado com sucesso.");
    }
}
