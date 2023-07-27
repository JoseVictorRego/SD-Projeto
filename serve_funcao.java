import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class serve_funcao {
        private static Map<String, String> clientFolders = new HashMap<>(); // Mapear o nome do cliente para a pasta

    public static void receberArquivo(Socket clientSocket) throws IOException {
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
