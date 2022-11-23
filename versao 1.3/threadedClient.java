import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;



// Server class
class threadedClient {
	//ClientHandler2 ClientHandler2 = new ClientHandler2(null, null,null);
    
	public static void main(String[] args) throws IOException
	{
        List<InetAddress> baseDadosno= new ArrayList<InetAddress>();
        byte[] receive = new byte[65535];
     
		
        InetAddress ipControlador = InetAddress.getByName("127.0.0.1");

		//DatagramPacket DpReceive = null; 
        
		DatagramSocket ds = new DatagramSocket();

        DatagramPacket DpReceive = new DatagramPacket(receive, receive.length); 
        

		byte[] buf;
		try {
            

            String selfIPString =  InetAddress.getLocalHost().getHostName();
            System.out.println("Hostname "+ selfIPString);
            InetAddress selfIP = InetAddress.getByName(selfIPString);
            System.out.println("SELFIP LENGHT"+ selfIP.toString());
            
            byte[] messageTypeBytes = "m1".getBytes(); 
            byte[] selfIPbytes = selfIP.getAddress();
            System.out.println("SELFIPbytes LENGHT"+ selfIPbytes.toString());
            System.out.println("TESTE "+ InetAddress.getByAddress(selfIPbytes));

            ByteArrayOutputStream message = new ByteArrayOutputStream();
            message.write(messageTypeBytes);
            message.write(selfIPbytes);

            buf = message.toByteArray();
            message.reset();


            DatagramPacket DpSend = new DatagramPacket(buf, buf.length, ipControlador,3306);
                ds.send(DpSend);
                
  
                ds.receive(DpReceive);

                receive = DpReceive.getData();
            System.out.println("Server:-" + data(DpReceive.getData())); 
            System.out.println("size "+DpReceive.getLength()); 
            String messageType = new String(receive,0,2);
            String messageThreadID = new String(receive,2,6);
            
            byte[] messageIPListbytes = Arrays.copyOfRange(receive, 7,DpReceive.getLength()); 
            int numIPs = DpReceive.getLength()/4;
  ;

                
                System.out.println("thread n " + messageThreadID);
                //System.out.println("aqui:"+InetAddress.getByAddress(messageIPInetAddress.getAddress()));
            switch(messageType){
                case "m2":
                            //GUARDA A BASE DE DADOS
                            for(int j=0;j<numIPs-1;j++){
                                byte[] IPbytes = Arrays.copyOfRange(messageIPListbytes, j*4, (j*4+4));
                                InetAddress messageIPInetAddress = InetAddress.getByAddress(IPbytes);
                                baseDadosno.add(messageIPInetAddress);
                            }
                            System.out.println(baseDadosno.toString());
                            

                            //CONFIRMA A RECEÇAO DOS DADOS
                            messageTypeBytes = "m3".getBytes();
                            byte[] threadIDBytes = messageThreadID.getBytes();
                            message.write(messageTypeBytes);
                            message.write(threadIDBytes);

                            buf = message.toByteArray();
                            message.reset();
                            DpSend = new DatagramPacket(buf, buf.length, ipControlador,3306);
                            ds.send(DpSend);
                            break;

                          

            }
			
			//server.setReuseAddress(true);
		
			while (true) {

                Scanner sc = new Scanner(System.in);

                //if(sc.)

                // Step 3 : revieve the data in byte buffer. 
                ds.receive(DpReceive); 

				// create a new thread object
				ClientHandler2 clientSock
					= new ClientHandler2(ds, DpReceive,baseDadosno);

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

}
