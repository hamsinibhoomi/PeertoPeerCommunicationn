//package server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

//import common.client_structure;
//import common.rfc_structure;


public class ServerThread {
	
	    public static void main(String args[]) throws InterruptedException {
	        
	        ServerSocket server = null;
	        Socket clientSocket = null;
	        
                Integer port = Integer.parseInt(args[0]); 
	        Scanner s = new Scanner(System.in);
	        String line;
	        
	        ArrayList<client_structure> client_list1 = new ArrayList<client_structure>();
	        ArrayList<rfc_structure> rfc_list1 = new ArrayList<rfc_structure>();
	        List<client_structure> client_list = Collections.synchronizedList(client_list1);
	        List<rfc_structure> rfc_list = Collections.synchronizedList(rfc_list1);
	        
	        int number=0;
	        DataInputStream is;
	        PrintStream os;
	        
	        try {
	           server = new ServerSocket(port);
	        }
	        catch (IOException e) {
	           System.out.println(e);
	        }   
	        
	          
	        List<Thread> threads = new ArrayList<Thread>();
	        while(true) {
	            try {
	                clientSocket = server.accept();
	                
	            /* start a new thread to handle this client */
	                number++;
	            	Thread t = new Thread(new ClientConn(clientSocket,number,client_list,rfc_list));
	            
	                t.start();
	                Thread.sleep(10000);    
	                
	            } //try
	            catch (IOException e) {
	                System.err.println("Accept failed.");
	                System.err.println(e);
	                System.exit(1);
	            } //catch
	            
//	            System.out.println("_____ printing client list______");
//	            for (client_structure cl : client_list)
//	            {
//	            	System.out.println(cl.hostName);
//	            	System.out.println(cl.uploadPort);
//	            }
//	            System.out.println("_____ printing RFC list______");
//	            for (rfc_structure rfc : rfc_list)
//	            {
//	            	System.out.println(rfc.hostname);
//	            	System.out.println(rfc.title);
//	            	System.out.println(""+rfc.rfcNum);
//	            }
		        	
		        	
	        } //while
	        
	        
	    }
	    
	    
}
