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

		DatagramPacket DpReceive = null; 
        
		DatagramSocket ds = new DatagramSocket();

        DpReceive = new DatagramPacket(receive, receive.length); 
        

		byte[] buf;
		try {


            String selfIPString =  InetAddress.getLocalHost().getHostName();
            System.out.println("Hostname "+ selfIPString);
            InetAddress selfIP = InetAddress.getByName(selfIPString);

            
            byte[] messageTypeBytes = "m1".getBytes(); 
            byte[] selfIPbytes = selfIP.getAddress();

            ByteArrayOutputStream message = new ByteArrayOutputStream();
            message.write(messageTypeBytes);
            message.write(selfIPbytes);

            buf = message.toByteArray();
            message.reset();


            DatagramPacket DpSend = new DatagramPacket(buf, buf.length, ipControlador,3306);
                ds.send(DpSend);
                
  
                ds.receive(DpReceive);

                receive = DpReceive.getData();
            System.out.println("Client:-" + data(DpReceive.getData()));   
            String messageType = new String(receive,0,2);
            String messageIPlist = new String(receive,2,receive.length);

                

                System.out.println("aqui:"+messageIPlist);
            switch(messageType){
                case "m2":

                            String[] newList = messageIPlist.toString().replace("]", "").replace("/", "").replace(" ", "").split(",");
                            System.out.println(newList[2]);
                            String lastIP = newList[newList.length-1].replace("]", "");

                            System.out.println("lastIP "+lastIP);
                            for(int i=0;i<newList.length;i++){
                                InetAddress tmpIP = InetAddress.getByName(newList[i]);
                                System.out.println(tmpIP.toString());
                                baseDadosno.add(tmpIP);
                            }
                            //InetAddress lastIPtmp = InetAddress.getByName(lastIP);
                            //baseDadosno.add(lastIPtmp);
                            System.out.println(baseDadosno.toString());
                            
                           /* String tmpm3 = "m3" + tmp2[1].toString();
                            buf = tmpm3.getBytes();
                            DpSend = new DatagramPacket(buf, buf.length, ipControlador,3306);
                            ds.send(DpSend);*/


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
