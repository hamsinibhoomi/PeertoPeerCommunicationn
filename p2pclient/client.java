//package p2pclient;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class client {
    public static void main(String[] args) throws IOException {
        Socket clientSocket = null;  
        String serverAdd = args[0];
	Integer serverPort = Integer.parseInt(args[1]);
        boolean t = true;
        DataOutputStream os = null;
        DataInputStream is = null;
        Scanner s = new Scanner(System.in);
       
        String hostname=java.net.InetAddress.getLocalHost().getHostAddress();
       // System.out.println("host name :"+hostname);
        Integer uploadPort = null;
        String directoryname = "";
        Integer option;
        String responseLine;

        try {
        	
        	 
        	 clientSocket = new Socket(serverAdd, serverPort);
        	 
        	 os = new DataOutputStream(clientSocket.getOutputStream());
             is = new DataInputStream(clientSocket.getInputStream());
             
         //    System.out.println("Enter hostname : ");
        //     hostname = s.nextLine();
             
             System.out.println("Enter upload port number : ");
             uploadPort = Integer.parseInt(s.nextLine());
             System.out.println("Enter directoryname : ");
             directoryname = s.nextLine();

             Thread t1 = new Thread(new Download(uploadPort,directoryname));
             t1.start();
             
         	
         	//sending hostname
        	os.writeBytes(hostname + "\n");
        //	System.out.println("Waiting for response");
        	responseLine = is.readLine();
        //	System.out.println("Recieved response as " + responseLine);
        //	System.out.println("server: "+responseLine);
        	
        	//sending uploadport
        	os.writeBytes(uploadPort + "\n");
        	responseLine = is.readLine();
        //	System.out.println("server port :"+responseLine);
        	
        	//sending initial list of RFCs
        	File f = new File(directoryname);
        	Integer num = f.listFiles().length;
        	
        	os.writeBytes(num.toString() + "\n");
        	File[] file_list = f.listFiles();
        	
        is.readLine();
        	
        	for (int i=0;i<num;i++)
        	{
        		os.writeBytes(file_list[i].getName().replace(".txt", "") + "\n");
        		is.readLine();
        		os.writeBytes("RFCtitle"+"\n");
        		is.readLine();
        		os.writeBytes(hostname + "\n");
        		is.readLine();	
        	}
        	
        	
        	
        	
         	
           
        
        
       while(t==true){
        	
        	System.out.println("1.Get list of RFCs");
        	System.out.println("2.Download an RFC");
        	System.out.println("3.Lookup peers");
        	System.out.println("4.Exit");
        	
        	System.out.println("Enter option: ");
        	option = s.nextInt();
        	String opt = option.toString();
        	switch(option){
        	
        	case 1 :
        		try {
        			//System.out.println("in case "+opt);
					os.writeBytes(opt + "\n");
					is.readLine();
					
					String listreq = "LIST ALL P2P-CI/1.0 Host: "+hostname+" Port: "+uploadPort;
					os.writeBytes(listreq+"\n");
					
					String numberofRfcs = is.readLine();
					//System.out.println("num of RFCS recieved" + numberofRfcs);
					os.writeBytes(numberofRfcs + "\n");
//					for(int i=0;i<Integer.parseInt(numberofRfcs);i++)
//					{
//						System.out.println("rfc list is :" + is.readLine());
//						
//					}
					String listResp = is.readLine();
					String[] listResp1 = listResp.split(" ");
					System.out.println(listResp1[0]+ " "+listResp1[1]+ " "+listResp1[2]);
					
					 Integer n=3;
					    
					    for(int i=0;i<Integer.parseInt(numberofRfcs);i++){
					 
					    	System.out.println(listResp1[n]+" "+listResp1[n+1]+" "+listResp1[n+2]+" "+listResp1[n+3]+" "+listResp1[n+4]);
					    	n += 5;
					    }
					
					
					
				
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		break;
        		
        	case 2 :
        		
        		ArrayList<String> list = new ArrayList<String>();
        		
        		//System.out.println("in case "+opt);
				os.writeBytes(opt + "\n");
				is.readLine();
				
        		 Integer number=0;
        		 Scanner s2 = new Scanner(System.in);
        		  System.out.println("Enter the rfc number: ");
        		  number = Integer.parseInt(s2.nextLine());
        		  
        		  
        		  os.writeBytes(number.toString()+"\n");
					
				    Integer rfccount = Integer.parseInt(is.readLine());
				    System.out.println(rfccount);
				    os.writeBytes(rfccount.toString()+"\n");
				    if (rfccount.equals(0))
			            {
                                    System.out.println ("RFC number not found in the P2P System");
			            }
				    else
			            {
				    Integer portnum=0;
				    
				    for(int i=0;i<rfccount;i++)
					{
				    	list.add(is.readLine());
				    	
				    	
				  	}
				    
				    Integer counter = 1;
				    for(String st : list){
				    	
				    	System.out.println(counter+". "+st);
				    	counter++;
				    }
				    
				    System.out.println("Select the peer from which you want to download :");
				    String numberP = s2.nextLine();
				    
				    String result = list.get(Integer.parseInt(numberP)-1);
				    
				    String[] resultParsed = result.split(" ");
				    
				    String peerPort = resultParsed[3];
				    
//
//				    for(int i =0;i< resultParsed.length; i++){
//				    	System.out.println(resultParsed[i]);
//				    }
				    
				    System.out.println("connecting to portnum---------and host name"+peerPort + resultParsed[2]);
				    Socket uploadSocket = new Socket(resultParsed[2], Integer.parseInt(peerPort));
				    
				    DataInputStream in = new DataInputStream(uploadSocket.getInputStream());
				    DataOutputStream out = new DataOutputStream(uploadSocket.getOutputStream());
				    
				    //System.out.println("sending rfc number as "+ resultParsed[0]);
				    
				    String msgToSend = "GET RFC ";
				    msgToSend += resultParsed[0];
				    msgToSend += " P2P-CI/1.0 ";
				    msgToSend += "Host: localhost ";
				    msgToSend += "OS: " + System.getProperty("os.name") + "\n";
				     
				    out.writeBytes(msgToSend);
				    String resp=in.readLine();
				    String[] resp1 = resp.split(" ");
				    System.out.println(resp1[0]+" "+resp1[1]+" "+resp1[2]);
				    System.out.println(resp1[3]+" "+resp1[4]+" "+resp1[5]+" "+resp1[6]+" "+resp1[7]+" "+resp1[8]+" "+resp1[9]);
				    System.out.println(resp1[10]+" "+resp1[11]+" "+resp1[12]);
				    System.out.println(resp1[13]+" "+resp1[14]+" "+resp1[15]+" "+resp1[16]+" "+resp1[17]+" "+resp1[18]+" "+resp1[19]);
				    System.out.println(resp1[20]+" "+resp1[21]);
				    System.out.println(resp1[22]);

				    
				    
				    byte[] b = new byte[1024];
				    int len = 0;
				    int bytcount = 1024;
				    FileOutputStream inFile = new FileOutputStream(directoryname+resultParsed[0]);
				    InputStream is1 = uploadSocket.getInputStream();
				    BufferedInputStream in2 = new BufferedInputStream(is1, 1024);
				    while ((len = in2.read(b, 0, 1024)) != -1) {
				      bytcount = bytcount + 1024;
				      inFile.write(b, 0, len);
				    }
				    //System.out.println("Bytes Writen : " + bytcount);

				    
				 //   System.out.println("Download Successful!");
				    is1.close();
				    in2.close();
				    inFile.close();
				    in.close();
				    out.close();
				    uploadSocket.close();
//				    os.writeBytes(resultParsed[0]+"\n");
//				    
//				    System.out.println("received rfc_num"+is.readLine());
//				    
//				    os.writeBytes(hostname+"\n");
//				    
//				    System.out.println("received hostname"+is.readLine());
//				    
//				    os.writeBytes("RFC_title"+"\n");
//				    
				    String sendRFC = "ADD RFC "+resultParsed[0]+" "+"P2P-CI/1.0 Host: "+hostname+" "+"Port: "+uploadPort+" "+"Title: RFCTitle";
				    
				    os.writeBytes(sendRFC+"\n");
				    
				    String rep2 = is.readLine();
				    
				    String[] rep21 = rep2.split(" ");
				    System.out.println(rep21[0]+" "+rep21[1]+" "+rep21[2]);
				    System.out.println(rep21[3]+" "+rep21[4]+" "+rep21[5]+" "+rep21[6]+" "+rep21[7]);
				    
				    
		            //out.close();
		            
		            
        		    }
        		  break;
        	
        	case 3 : 
        		 Scanner s1 = new Scanner(System.in);
        		String lookup;
        		//System.out.println("in case "+opt);
				try {
					os.writeBytes(opt + "\n");
					is.readLine();
					
					System.out.println("Enter rfc number to lookup :");
					lookup = s1.nextLine();
					
					
					String lookupSend = "LOOKUP RFC "+lookup+" "+"P2P-CI/1.0 Host: "+hostname+" "+"Port: "+uploadPort+" "+"Title: RFCTitle";
					
					os.writeBytes(lookupSend+"\n");
					
				    Integer rfccount1 = Integer.parseInt(is.readLine());
				    //System.out.println(rfccount1);
				    
				    os.writeBytes(rfccount1.toString()+"\n");
				    
//				    for(int i=0;i<rfccount1;i++)
//					{
//						System.out.println("rfc lookup is :" + is.readLine());
//						
//					}
				    String lookupresp = is.readLine();
				    String[] lookupresp1 = lookupresp.split(" ");
				    System.out.println(lookupresp1[0]+ " "+lookupresp1[1]+ " "+lookupresp1[2]);
				    Integer n=3;
				    
				    for(int i=0;i<rfccount1;i++){
				 
				    	System.out.println(lookupresp1[n]+" "+lookupresp1[n+1]+" "+lookupresp1[n+2]+" "+lookupresp1[n+3]+" "+lookupresp1[n+4]);
				    	n += 5;
				    }
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		  
				break;
				
        	case 4 :
        		os.writeBytes(opt+"\n");
        		is.readLine();
        		os.writeBytes(hostname+"\n");
        		System.out.println("Connection Terminated");
        		t=false;
        	    
        		break;
        	}
        	
        
        	
        }
        } //try
        catch (UnknownHostException e) {
          e.printStackTrace();
	} catch (IOException e) {
            e.printStackTrace();
        }// catch
       	
            
    }  // main
    
}  //class
