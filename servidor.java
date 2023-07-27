import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class servidor {

    private static Map<String, String> clientFolders = new HashMap<>(); // Mapear o nome do cliente para a pasta

    public static void main(String[] args) {
        int serverPort = 4000; // Porta do servidor

        try {
            ServerSocket serverSocket = new ServerSocket(serverPort);
            System.out.println("Servidor aguardando conexões...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("\n-----Cliente conectado------\n");

                // Tratar a conexão em uma thread separada para permitir o atendimento a múltiplos clientes
                Thread thread = new Thread(() -> {
                    try {
                        receiveFile(clientSocket);
                    } catch (IOException e) {
                        System.out.println("-> Servidor perdeu Conexão com o cliente!!");
                    }
                });
                thread.start();
            }
        } catch (IOException e) {
            System.out.println("-> Servidor Parou!!");
        }
    }

    private static void receiveFile(Socket clientSocket) throws IOException {
        InputStream is = clientSocket.getInputStream();

        // Receber o nome do cliente
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String clientName = br.readLine();

        // Verificar se a pasta do cliente já foi criada
        String saveDir = clientFolders.get(clientName);
        if (saveDir == null) {
            // Se a pasta ainda não existe, criar uma nova pasta
            saveDir = "C:\\Users\\jdeli\\Videos\\Armazenamento_Servidor\\" + clientName + "\\";
            File directory = new File(saveDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            // Adicionar a pasta ao mapa para reutilização futura
            clientFolders.put(clientName, saveDir);
        }

        // Receber o nome do arquivo
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
        System.out.println("Cliente: "+clientName+".\n ->Enviou um Arquivo e foi salvo com sucesso: " + fileName);

        // Fechando a conexão com o cliente
        clientSocket.close();
        System.out.println("Cliente: "+clientName+" Saiu!!\n");
    }
}
