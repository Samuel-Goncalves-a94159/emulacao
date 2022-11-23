import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;



// ClientHandler class
class ClientHandler implements Runnable {
    private final DatagramSocket ds;
    private final DatagramPacket DpReceive;
    private List<InetAddress> baseDados;
    private String threadID;

    // Constructor
    public ClientHandler(DatagramSocket socket,DatagramPacket DpReceive,List<InetAddress> baseDados,String threadID )
    {
        this.ds = socket;
        this.DpReceive = DpReceive;
        this.baseDados = baseDados;
        this.threadID = threadID;
    }

    public void run()
    {
        
        try {
			File myObj = new File("baseDados.txt");
			if (myObj.createNewFile()) {
			  System.out.println("File created: " + myObj.getName());
			} else {
			  System.out.println("File already exists.");
			}
		  } catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		
        PrintWriter out = null;
        BufferedReader in = null;
        InetAddress address = DpReceive.getAddress();
            int port = DpReceive.getPort();
        
        try {
            
            byte[] receive = DpReceive.getData();
            //System.out.println("Client:-" + data(DpReceive.getData()));   
            String messageType = new String(receive,0,2);
            //String messageIP = new String(receive,2,6);
            
            byte[] messageIPbytes = Arrays.copyOfRange(receive, 2,6); 
            InetAddress messageIPInetAddress = InetAddress.getByAddress(messageIPbytes);
            
            //tmpReceive = tmpReceive2[0];
            
            messageType = messageType.replace("[","");
            System.out.println(messageType);
            System.out.println(messageIPInetAddress.toString());
            
        switch(messageType){
            case "m1": 	
                        //baseDados.clear();

                        baseDados.add(messageIPInetAddress);
                        System.out.println("baseDados"+baseDados.toString());
                        writeFile(baseDados);

                        byte[] messageTypeBytes = "m2".getBytes(); 
                        byte[] threadIDBytes = threadID.getBytes();
                        
                        //CRIAR E ENVIAR PACOTE COM A BASE DE DADOS
                        ByteArrayOutputStream message = new ByteArrayOutputStream();
                        message.write(messageTypeBytes);
                        message.write(threadIDBytes);
                        for(int i=0; i<(baseDados.size());i++){
                            byte[] baseDadosIPindividualBytes = baseDados.get(i).getAddress();
                            
                            message.write(baseDadosIPindividualBytes);
                        }
                        byte[] buf = message.toByteArray();
                        message.reset();
                        DatagramPacket DpSend = 
                            new DatagramPacket(buf, buf.length, address, port);
                        ds.send(DpSend);

                        

                        //ENVIAR AOS NOS DA REDE O IP DO NOVO NO
                        if(baseDados.size()==1){//se for o primeiro no
                            break;
                        }
                        for(int k = 0;k<baseDados.size();k++ ){
                            messageTypeBytes = "m4".getBytes();
                            byte[] newNodeIP = messageIPbytes;
                            
                            message.write(messageIPbytes);
                            message.write(threadIDBytes);
                            message.write(newNodeIP);
                            buf = message.toByteArray();
                            message.reset();
                            InetAddress otherNodeIP = baseDados.get(k);
                            DpSend = new DatagramPacket(buf, buf.length,otherNodeIP,port);
                            ds.send(DpSend);
                        }
                        
                        //ESPERAR PELAS CONFIRMACOES
                        HashMap<InetAddress,Integer> tabela= new HashMap<>();
                        for(int k = 0;k<baseDados.size();k++ ){
                            tabela.put(baseDados.get(k), 0);
                        }
                        
                        Boolean estado = false;
                        while(estado!=true){
                            Thread.sleep(5000);
                            for(int k = 0;k<baseDados.size();k++ ){
                                if(tabela.get(baseDados.get(k))!=-1){
                                    if(tabela.get(baseDados.get(k))!=3){
                                        DpSend = new DatagramPacket(buf, buf.length,baseDados.get(k),port);
                                        ds.send(DpSend);
                                        int counter=tabela.get(baseDados.get(k));
                                        tabela.put(baseDados.get(k),counter+1);
                                    }
                                }
                                else{estado=true;/*TODO remover nos que tem o counter 3 */}
                            }
                        }
                       
                        
            case "n1":			

                        System.out.println("IP: "+address);
                        baseDados.remove(address);
                        writeFile(baseDados);
                        buf = "n4".getBytes();
                        DpSend = 
                            new DatagramPacket(buf, buf.length, address, port);
                        ds.send(DpSend);
                        //notifyALL node to update; --to do--

            case "m3":
                        
                        
        }


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
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
                    ds.close();
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

/**
 * @param baseDados
 * @throws IOException
 */
public static void writeFile(List<InetAddress> baseDados) throws IOException{

    File oldfile = new File(
        "baseDados.txt");

        oldfile.delete();

        File newfile = new File(
            "baseDados.txt");
        
        BufferedWriter bw
				= new BufferedWriter(new FileWriter(newfile, true));

            int i = 0;
			String tmp = "\n";
            String[] tmp2;
            int size = baseDados.size(); 
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

