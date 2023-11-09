import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class server_test {
	
	public static String calc(String exp) {
        StringTokenizer st = new StringTokenizer(exp, " ");
        if (st.countTokens() > 3)
            return "e1";
        else if (st.countTokens() < 3)
            return "e4";
        
        String opcode = st.nextToken();
        int op1 = Integer.parseInt(st.nextToken());
        int op2 = Integer.parseInt(st.nextToken());
        switch (opcode) {
            case "add":
                return Integer.toString(op1 + op2);
            case "sub":
                return Integer.toString(op1 - op2);
            case "mul":
                return Integer.toString(op1 * op2);
            case "div":
                return op2 != 0 ? Integer.toString(op1 / op2) : "e2";
            default:
                return "e3";
        }
    }
	
	 private static class ClientHandler implements Runnable {
	        private final Socket socket;

	        public ClientHandler(Socket socket) {
	            this.socket = socket;
	        }

	        @Override
	        public void run() {
	            System.out.println("Connected: " + socket);
	            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	                 BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

	                String inputMessage;
	                while ((inputMessage = in.readLine()) != null) {
	                    if ("bye".equalsIgnoreCase(inputMessage)) {
	                        System.out.println("Client terminated the connection");
	                        break;
	                    }
	                    System.out.println("Received from client: " + inputMessage);
	                    String res = calc(inputMessage);
	                    out.write(res + "\n");
	                    out.flush();
	                }
	            } catch (IOException e) {
	                System.out.println("Exception in client handler: " + e.getMessage());
	                e.printStackTrace();
	            } finally {
	                try {
	                    socket.close();
	                    System.out.println("Closed: " + socket);
	                } catch (IOException e) {
	                    System.out.println("Could not close client socket: " + e.getMessage());
	                }
	            }
	        }
	    }

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(20);
        try (ServerSocket listener = new ServerSocket(9999)) {
            System.out.println("Server is running...");
            while (true) {
                Socket socket = listener.accept();
                pool.execute(new ClientHandler(socket));
            }
        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

   

    
}
