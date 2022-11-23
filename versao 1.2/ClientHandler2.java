import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;



// ClientHandler class
class ClientHandler2 implements Runnable {
    private final DatagramSocket clientSocket;
    private final DatagramPacket DpReceive;
    private List<InetAddress> baseDados;

    // Constructor
    public ClientHandler2(DatagramSocket socket, DatagramPacket DpReceive,List<InetAddress> baseDados )
    {
        this.clientSocket = socket;
        this.DpReceive = DpReceive;
        this.baseDados = baseDados;
    }

    public void run()
    {
        

		byte[] buf = baseDados.toString().getBytes();
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            
            byte[] receive = DpReceive.getData();
            System.out.println("Client:-" + data(DpReceive.getData())); 
        
            InetAddress address = DpReceive.getAddress();
            int port = DpReceive.getPort();	
            
            DatagramPacket DpSend = 
            new DatagramPacket(buf, buf.length, address, port);
            
            String x = data(receive).toString().replace("/", "");
            
            String[] k = x.split("\\[");

        switch(k[0].toString()){
            case "m3": 	
                        String l = k[1].replace("]", "");
                        
  
                        //baseDados.clear();
                        System.out.println("IP: "+ l);
                        baseDados.add(InetAddress.getByName(l));
                        System.out.println("baseDados"+baseDados.toString());
                        //writeFile(baseDados);
                        /*buf = baseDados.toString().getBytes();
                        //System.out.println(buf.length);
                        clientSocket.send(DpSend);*/
                        //notifyALL node to update; --to do--
                                                
        }


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                    clientSocket.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


public static StringBuilder data(byte[] a) 
{ 
    if (a == null) 
        return null; 
    StringBuilder ret = new StringBuilder(); 
    int i = 0; 
    while (a[i] != 0) 
    { 
        ret.append((char) a[i]); 
        i++; 
    } 
    return ret; 
} 

/*public static void readFile(List<InetAddress> baseDados) throws IOException{
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

/**
 * @param baseDados
 * @throws IOException
 
public static void writeFile(List<InetAddress> baseDados) throws IOException{

    File oldfile = new File(
        "baseDadosNo.txt");

        oldfile.delete();

        File newfile = new File(
            "baseDadosNo.txt");
        
        BufferedWriter bw
				= new BufferedWriter(new FileWriter(newfile, true));

            int i = 0;
			String tmp = "\n";
            String[] tmp2;
            int size = baseDados.size(); 
            System.out.println("here"+ baseDados.toString());
            while(i<size){
                tmp2 = baseDados.get(i).toString().split("/");
                bw.write(tmp2[1]);
                
                if(i!=size-1){bw.write(tmp);}
                i++;
            }
			
            
            bw.close();



}

/*public static void rmFile(String arg) throws IOException{


    try {
        File inputFile = new File("baseDados.txt");
        if (!inputFile.isFile()) {
            System.out.println("File does not exist");
            return;
        }
        //Construct the new file that will later be renamed to the original filename.
        File tempFile = new File(inputFile.getAbsolutePath() + ".tmp");
        BufferedReader br = new BufferedReader(new FileReader("baseDados.txt"));
        PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
        String line = null;

        //Read from the original file and write to the new
        //unless content matches data to be removed.
        while ((line = br.readLine()) != null) {
            if (!line.trim().equals(arg)) {
                pw.println(line);
                pw.flush();
            }
        }
        pw.close();
        br.close();
        

        //Delete the original file
        if (!inputFile.delete()) {
            System.out.println("Could not delete file");
            return;
        }

        //Rename the new file to the filename the original file had.
        if (!tempFile.renameTo(inputFile))
            System.out.println("Could not rename file");
        }
    catch (FileNotFoundException ex) {
        ex.printStackTrace();
    }catch (IOException ex) {
        ex.printStackTrace();
    }

}
*/
}

