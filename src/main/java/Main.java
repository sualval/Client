import java.io.IOException;

public class Main {
        private static String ip = "localhost";
        private static int port = 12345;

        public static void main(String[] args)  {

            try {
                new Client(ip, port);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }



