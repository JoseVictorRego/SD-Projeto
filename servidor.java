import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class servidor {  
    public static void main(String[] args){

        int serverPort = 4000; // Porta do servidor
        serve_funcao ServerFuncao = new serve_funcao(); //Funções do Servidor, neste caso o receber arquivo

        try {
            //Abrindo Conexão do servidor
            ServerSocket serverSocket = new ServerSocket(serverPort);
            

            System.out.println("Servidor aguardando conexões...");

            //Fazendo com que a conexão com o cliente continue em loop
            while (true) {

                //conectando o cliente
                Socket clientSocket = serverSocket.accept();
                System.out.println("\n-----Cliente conectado------\n");

                // Tratar a conexão em uma thread separada para permitir o atendimento a múltiplos clientes
                Thread thread = new Thread(() -> {
                    try {
                        ServerFuncao.receberArquivo(clientSocket);
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
            String currentDir = System.getProperty("user.dir");
            saveDir = currentDir + "\\arquivos\\" + clientName + "\\";
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
