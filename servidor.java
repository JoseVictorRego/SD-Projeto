import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class servidor {

    public static void main(String[] args) {
        int serverPort = 4000; // Porta do servidor

        try {
            ServerSocket serverSocket = new ServerSocket(serverPort);
            System.out.println("Servidor aguardando conexões...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());

                // Tratar a conexão em uma thread separada para permitir o atendimento a múltiplos clientes
                Thread thread = new Thread(() -> {
                    try {
                        receiveFile(clientSocket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void receiveFile(Socket clientSocket) throws IOException {
        InputStream is = clientSocket.getInputStream();

        // Diretório onde os arquivos serão armazenados no servidor
        String saveDir = "C:\\Users\\jdeli\\Videos\\";
        File directory = new File(saveDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Receber o nome do arquivo
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String fileName = br.readLine();

        // Criar um novo arquivo com o nome recebido
        File file = new File(saveDir + fileName);
        FileOutputStream fos = new FileOutputStream(file);

        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1) {
            fos.write(buffer, 0, bytesRead);
        }

        fos.flush();
        fos.close();
        System.out.println("Arquivo recebido e salvo com sucesso: " + fileName);

        // Fechando a conexão com o cliente
        clientSocket.close();
    }
}
