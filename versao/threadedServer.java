import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;



// Server class
class threadedServer {
	ClientHandler ClientHandler = new ClientHandler(null, null,null);
	public static void main(String[] args) throws IOException
	{
        List<InetAddress> baseDados= new ArrayList<InetAddress>();
        byte[] receive = new byte[65535];

		        
        // File path is passed as parameter
		File file = new File(
            "baseDados.txt");

        // Creating an object of BufferedReader class
        BufferedReader br = new BufferedReader(new FileReader(file));
 
        // Declaring a string variable
        String st;
     
		readFile(baseDados);
        InetAddress ipControlador = InetAddress.getByName("244.0.0.1");

		DatagramPacket DpReceive = null; 
        // server is listening on port 3306
		DatagramSocket ds = new DatagramSocket(3306);

		
		try {

			
			//server.setReuseAddress(true);
		
			while (true) {

                // Step 2 : create a DatgramPacket to receive the data. 
                DpReceive = new DatagramPacket(receive, receive.length); 

                // Step 3 : revieve the data in byte buffer. 
                ds.receive(DpReceive); 

				// create a new thread object
				ClientHandler clientSock
					= new ClientHandler(ds, DpReceive,baseDados);

				// This thread will handle the client
				// separately
				new Thread(clientSock).start();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (ds != null) {
				ds.close();
			}
		}
	}

	public static void readFile(List<InetAddress> baseDados) throws IOException{
	    // File path is passed as parameter
		File file = new File(
			"baseDados.txt");
	  
		// Creating an object of BufferedReader class
		BufferedReader br
			= new BufferedReader(new FileReader(file));
	  
		// Declaring a string variable
		String st;
		// Condition holds true till
		// there is character in a string

		while ((st = br.readLine()) != null){
			
			
			// Print the string
			//System.out.println(st);
			InetAddress tmp;
			tmp = InetAddress.getByName(st); 
			
			baseDados.add(tmp);
			
			
		}
		System.out.println("Base de dados: " + baseDados.toString());
		
		br.close();
	  }
}
