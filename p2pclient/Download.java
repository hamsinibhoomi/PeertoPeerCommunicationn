//package p2pclient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Download implements Runnable{
	
	Integer port;
	Socket peerSocket;
	String DownloadPath = "";
	DataInputStream in = null;
	DataOutputStream out = null;

	public Download(Integer uploadPort, String DownloadPath){
		
		port = uploadPort;
		this.DownloadPath = DownloadPath;
		
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ServerSocket downloadSocket = new ServerSocket(port);
			
			
			while(true){
			    peerSocket = downloadSocket.accept();
			    String statusCode =" ";
			    DateFormat dateFormat = new SimpleDateFormat("E, d MMM y HH:mm:ss z");
			    Date date = new Date();
			   
			    
			    out = new DataOutputStream(peerSocket.getOutputStream());
	             in = new DataInputStream(peerSocket.getInputStream());
	             
	             String responseMsg = in.readLine();
	             String[] result = responseMsg.split(" ");
	             String filename = result[2];
	             System.out.println(result[0]+" "+result[1]+" "+result[2]+" "+result[3]);
	             System.out.println(result[4]+" "+result[5]);
	             System.out.println(result[6]+" "+result[7]);
	             
			    
			    File f =new File(DownloadPath+filename);
			    
			    if(!f.exists()){
			    	statusCode = "404 Not Found";
			    }
			    
			    else{
			    	statusCode = "200 OK";
			    }
			    
			    OutputStream os = peerSocket.getOutputStream();
			    byte[] buf = new byte[1024];
			    BufferedOutputStream bout = new BufferedOutputStream(os, 1024);
			    FileInputStream fin = new FileInputStream(f);
			    
			    
			    
			    
			    int i = 0;
			    int bytecount = 1024;
			    
			    String sendResponse = "P2P-CI/1.0 ";
			    
			    sendResponse += statusCode+" ";
			    
			    sendResponse += "Date: "+dateFormat.format(date)+ " ";
			    sendResponse += "OS: " + System.getProperty("os.name") + " ";
			    sendResponse += "Last-Modified: "+dateFormat.format(f.lastModified())+ " ";
			    sendResponse += "Content-length: "+f.length()+ " ";
			    sendResponse += "Content-type: "+"text/text";
			    
			    out.writeBytes(sendResponse+"\n");
			    while ((i = fin.read(buf, 0, 1024)) != -1) {
			      bytecount = bytecount + 1024;
			      bout.write(buf, 0, i);
			      bout.flush();
			    }
			    peerSocket.shutdownOutput(); /* important */
			    System.out.println("Bytes Sent :" + bytecount);

			    
			    
			    bout.close();
			    fin.close();
			    
			}	    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
	
	

}
