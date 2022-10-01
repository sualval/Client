import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Client extends Thread {
    private Scanner systemInput;
    private DataOutputStream out;
    private DataInputStream in;
    private String exit = "/exit";
    private String userName;
    private Socket socket;


    public Client(String ip, int port) throws IOException {
        this.socket = new Socket(ip, port);
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());
        systemInput = new Scanner(System.in);
        this.addLogin();
        new Read().start();
        start();
    }

    private void addLogin() {
        try {
            System.out.print(in.readUTF());
            userName = systemInput.nextLine();
            out.writeUTF(userName);
            System.out.println(in.readUTF());
            writeLog(date() +"connect :" + userName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (true) {
            String userMassage;
            try {
                userMassage = systemInput.nextLine();
                if (userMassage.equals(exit)) {
                    out.writeUTF(userMassage);
                    break;
                } else {
                    String send = date() + " - " + this.userName + ": " + userMassage;
                    out.writeUTF(send);
                    writeLog(send);
                }
            } catch (IOException e) {
                closeSocket();

            }

        }
    }

    private void closeSocket() {
        writeLog(date()+" "+exit);
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
            }
        } catch (Exception e) {
        }
    }

    private void writeLog(String text) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("fileClient.log", true))) {
            bufferedWriter.write(text + "\n");
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private String date(){
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    private class Read extends Thread {
        @Override
        public void run() {
            String userMassage;
            try {
                while (true) {
                    userMassage = in.readUTF();
                    if (userMassage.equals(exit)) {
                        closeSocket();
                        break;
                    } else {
                        System.out.println(userMassage);
                        writeLog(userMassage);
                    }
                }
            } catch (IOException e) {
                closeSocket();
            }
        }
    }

}




