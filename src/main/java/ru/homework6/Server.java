package ru.homework6;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * просотой HTTP сервер который на люой get запрос возвращает список файлов из текущей дирректории,
 * 404 на все остальные запросы
 *
 */
public class Server {

    public static void main(String[] args) throws IOException {
        int countClients = 0;
        ServerSocket serverSocket = new ServerSocket(8844);
        while (true) {
            final Socket socket = serverSocket.accept();
            countClients++;
            System.out.println("Установлена свять с клиентом #" + countClients);

            final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            final String request = reader.readLine().toLowerCase();
            System.out.println("От клиента " + countClients + " получен запрос: \n" + request + "\n");

            StringBuilder response = new StringBuilder();
            if (request.contains("get")) {
                response.append("HTTP/1.1 200 OK\nContent-type: text/html\n\n");
                response.append("<body>");
                final File file = new File(".");
                String[] files = file.list();
                response.append("<h2>Files in current directory</h2>");
                for (String str : files) {
                    response.append(str).append("<br>");
                }
                response.append("</body>");
            } else {
                response.append("HTTP/1.1 404 NOT FOUND\nContent-type: text/html\n\n");
            }
            writer.write(response.toString());
            writer.flush();
            System.out.println("Клиенту #" + countClients + " отправлен ответ:\n" + response +
                    "\n---------------");


            reader.close();
            writer.close();
            socket.close();

        }
    }
}
