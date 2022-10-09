import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Strings;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

public class Main {
  public static void main(String[] args) throws IOException {
    try (ServerSocket serverSocket = new ServerSocket(8080)){
      System.out.println("Server started. \n Listening for messages");
      while (true){
        try(Socket client = serverSocket.accept()) {
          System.out.println("Debug: got new message " + client.toString());
          InputStreamReader inputStreamReader = new InputStreamReader(client.getInputStream());
          BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
          StringBuilder request = new StringBuilder();
          String line = bufferedReader.readLine();
          while(!Strings.nullToEmpty(line.trim()).isEmpty()){
            request.append(line + "\n");
            line = bufferedReader.readLine();
          }
          System.out.println("--Request--");
          OutputStream clientOutput = client.getOutputStream();
          String firstLine =  request.toString().split("\n")[0];
          String resource = firstLine.split(" ")[1];
          if(resource.equals("/jordan")){
            clientOutput.write(("HTTP/1.1 200 OK\r\n"
                + "\r\n").getBytes());
            FileInputStream imageFile = new FileInputStream("jordan.jpeg");
            clientOutput.write(imageFile.readAllBytes());
            clientOutput.flush();
          }
          else if(resource.equals("/varun")){
            clientOutput.write(("HTTP/1.1 200 OK\r\n"
                + "\r\n"
                + "Hello Varun").getBytes());
            clientOutput.flush();
          }
          System.out.println(resource);
          clientOutput.close();
        }
      }
    }
  }
}