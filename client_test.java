import java.io.*;
import java.net.*;
import java.util.*;

public class client_test {
	
	public static void main(String[] args) {
		BufferedReader in = null;
		BufferedWriter out = null;
		Socket socket = null;
		Scanner scanner = new Scanner(System.in);
		
		String error1 = "Incorrect: Too many arguments";
		String error2 = "Incorrect: Divided by zero";
		String error3 = "Incorrect: Invalid operator";
		String error4 = "Incorrect: Need more arguments";
		
		//default info
		String host = "localhost";
		int port = 9999;
		
		//read info
		try (InputStream input = new FileInputStream("config.txt")) {
		    Properties prop = new Properties();
		    prop.load(input);

		    // read host, port info from .dat file 
		    host = prop.getProperty("host", host);
		    port = Integer.parseInt(prop.getProperty("port", String.valueOf(port)));

		} catch (FileNotFoundException e) {
		    // 파일이 없을 경우
		    System.out.println("Config file not found: " + e.getMessage());
		} catch (IOException e) {
		    // 파일을 읽을 수 없는 경우
		    System.out.println("Error reading config file: " + e.getMessage());
		} catch (NumberFormatException e) {
		    // port를 정수로 변환할 수 없는 경우
		    System.out.println("Invalid port number in config file: " + e.getMessage());
		}
		
		try {
			socket = new Socket(host, port);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			while (true) {
				System.out.print("Calculate? : ");
				String outputMessage = scanner.nextLine();
				if (outputMessage.equalsIgnoreCase("bye")) //bye -> terminate
				{
					out.write(outputMessage + "\n");
					out.flush();
					break;
				}
				out.write(outputMessage + "\n");
				out.flush();
				
				String Answer = in.readLine(); //inputMessage
								
				switch (Answer) {
				case "e1":
					System.out.println(error1);
					break;
				case "e2":
					System.out.println(error2);
					break;
				case "e3":
					System.out.println(error3);
					break;
				case "e4":
					System.out.println(error4);
					break;
				default:
					System.out.println("result: " + Answer);	
				}
				
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				scanner.close();
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				System.out.println("Error - server..");
			}
		}
	}
}
