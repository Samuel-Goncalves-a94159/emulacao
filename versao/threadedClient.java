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
class threadedClient {
	ClientHandler2 ClientHandler2 = new ClientHandler2(null, null,null);
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


            InetAddress selfIP =  InetAddress.getLocalHost();
            
            String tmp = selfIP.toString();
            String[] tmp2 = tmp.split("/");
            tmp = "m1/"+tmp2[1];

            buf = tmp.getBytes();

            DatagramPacket DpSend = new DatagramPacket(buf, buf.length, ipControlador,3306);
                ds.send(DpSend);
                
  
                ds.receive(DpReceive);

                tmp2 = data(receive).toString().split("\\[");

                

                System.out.println("auqi:"+tmp2[1]);
            switch(tmp2[0]){
                case "m2":System.out.println("ola");

                            String[] newList = tmp2[1].toString().replace("\\]", "").replace("/", "").split(",");
                            //System.out.println(newList[0]+newList[1]);
                            String lastIP = newList[newList.length-1].replace("]", "");

                            System.out.println("lastIP"+lastIP);
                            for(int i=0;i<newList.length-2;i++){
                                InetAddress tmpIP = InetAddress.getByName(newList[i]);

                                baseDadosno.add(tmpIP);
                            }
                            InetAddress lastIPtmp = InetAddress.getByName(lastIP);
                            baseDadosno.add(lastIPtmp);
                            System.out.println(baseDadosno.toString());

                          

            }
			
			//server.setReuseAddress(true);
		
			while (true) {

                

                // Step 3 : revieve the data in byte buffer. 
                ds.receive(DpReceive); 

				// create a new thread object
				ClientHandler clientSock
					= new ClientHandler(ds, DpReceive,baseDadosno);

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
