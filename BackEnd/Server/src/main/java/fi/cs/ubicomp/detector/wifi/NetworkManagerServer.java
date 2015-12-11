/*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* Please send inquiries to huber AT ut DOT ee
*/

package fi.cs.ubicomp.detector.wifi;


import java.io.*;
import java.net.*;


public class NetworkManagerServer implements Runnable{
    int portnum;
    Socket mysocket = null;
    InputStream in = null;
    OutputStream out = null;
    
    ObjectInputStream ois = null;
    ObjectOutputStream oos = null;
    ServerSocket serversoc = null;
    byte[] serveraddress = new byte[4];
    long processTime;
   
    

    public NetworkManagerServer(/*byte []serveraddress, */Socket mysocket) {
        //this.serveraddress = serveraddress;
        this.mysocket = mysocket;
    }


    private void waitforreceivingdata() {
        try {
            new Receiving().waitforreceivingdata();
        } catch (Exception ex) {
        }
    }


    class Receiving implements Runnable {
    	String RTT = null;
        Object state = null;
        Pack myPack = null;
        

        public Receiving() {
        }

        public void waitforreceivingdata() {
            Thread t = new Thread(this);
            System.out.println("Thread Starting ");
            t.start();
        }

        //@Override
        public void run() {
            try { 
            	
                myPack = (Pack) ois.readObject();
                
                RTT = myPack.getRTT();
                
                
                
                if (RTT != null && RTT.length() > 0) {
                    try {

                    	System.out.println("trying to load and execute");
                        
                        
                        
                        try{
                        	
                        	//System.out.println("param1:" + (int[]) paramValues[0]);
                        	//System.out.println("param1:" + paramValues[1]);
                        	//System.out.println("param1:" +  paramValues[2]);
                        	oos.flush();
                        	Object result = 1;
                        	state = 1;
//                        	float[] resultado = (float[]) result;
                        	//System.out.println(resultado[0]);
                        	//System.out.println(resultado[1]);
                        
                        	ResultPack rp = new ResultPack(result, state);
                        	
                        	//System.out.println("Size in bytes: " + sizeInBytes(rp));
                        	oos.flush();
                        	oos.writeObject(rp);
                        	//System.out.println("Object wrote it");
                        	oos.flush(); 
                        	//System.out.println("Object executed and flushed: " + (System.currentTimeMillis() - processTime));
                        		
                        	System.out.println("Finalize");
                          
                        } catch(Exception ex3){
                            ResultPack rp = new ResultPack(null, state);
                            oos.writeObject(rp);
                            oos.flush();
                            System.out.println("Hubo problema 3" + ex3.getCause());
                        }
                        
                    } catch (IllegalArgumentException ex) {
                        returnnull(oos);
                        System.out.println("Hubo problema 5");
                    } catch (SecurityException ex) {
                        returnnull(oos);
                        System.out.println("Hubo problema 7");
                    } finally {

                 
                    	
                        oos.close();
                        ois.close();

                        in.close();
                        out.close();

                        mysocket.close();

                        oos = null;
                        ois = null;

                        in = null;
                        out = null;
                        mysocket = null;

                    }
                } else {
                    returnnull(oos);
                }
            } catch (IOException ex) {
                returnnull(oos);
                System.out.println("Hubo problema 8");
            } catch (ClassNotFoundException ex) {
                returnnull(oos);
                System.out.println("Hubo problema 9");
                ex.printStackTrace();
            } 
        }
    }

    void returnnull(ObjectOutputStream oos){
        if(oos != null)
            try {
                oos.writeObject(null);
                oos.flush();
            } catch (IOException ex1) {

            }
    }
    
    public static int sizeInBytes(Object obj) throws java.io.IOException  
    {  
        ByteArrayOutputStream byteObject = new ByteArrayOutputStream();  
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteObject);  
        objectOutputStream.writeObject(obj);  
        objectOutputStream.flush();  
        objectOutputStream.close();  
        byteObject.close();  
      
        return byteObject.toByteArray().length;  
    }


	public void run() {
		// TODO Auto-generated method stub
		try {
            System.out.println("server waiting");
            
            in =  mysocket.getInputStream();
            out = mysocket.getOutputStream();

            
            //oos = new ObjectOutputStream(out);
            oos = new ObjectOutputStream(new BufferedOutputStream(out));
            oos.flush();
 
            //ois = new ObjectInputStream(in);
            ois =new ObjectInputStream(new BufferedInputStream(in));

            

            System.out.println("connection established");

            waitforreceivingdata();

        } catch (SocketException ex) {

        } catch (IOException ex) {

        } catch (Exception ex) {

        }
		
	}  


}

