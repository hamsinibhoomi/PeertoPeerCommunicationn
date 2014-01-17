//package server;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//import common.client_structure;
//import common.rfc_structure;

class ClientConn implements Runnable {
	
	Socket client;
	DataInputStream in;
	DataOutputStream out;
	int s;
	List<client_structure> client_list ;
	List<rfc_structure> rfc_list ;
	client_structure client_structure;
	
	
	ClientConn(Socket client,int s, List<client_structure> al, List<rfc_structure> rfc){
		this.client = client;
		this.s = s;
		
		this.client_list = al;
		this.rfc_list = rfc;

		try {
			in = new DataInputStream(client.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
			out = new DataOutputStream(client.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String response = "";
		   
	    try {
	       
	    	
	            
	            	 
	    		 response = "";
	    		 
	    		 //System.out.println("waiting for hostname");
	    		 String host = in.readLine();
	    		 //System.out.println("received hostname"+host);
	             out.writeBytes(host + "\n");
	             String port = in.readLine();
	             out.writeBytes(port + "\n");
	             //System.out.println("port"+port);
	             String num = in.readLine();
	             out.writeBytes(num+"\n");
	             
	             Integer num1 = Integer.parseInt(num);
	             //System.out.println(num1);
	            
	             for (int i=0;i<num1;i++)
	             { 
	            	 String rfcnum = in.readLine();
	            	 
	            	 out.writeBytes(rfcnum + "\n");
	            	 Integer rfc = Integer.parseInt(rfcnum);
	            	 String rfc_title = in.readLine();
	            	 out.writeBytes(rfc_title + "\n");
	            	 String hostname = in.readLine();
	            	 out.writeBytes(hostname + "\n");
	            	 rfc_list.add(new rfc_structure(rfc,rfc_title,hostname));
	            	 
	             }
	             
	             client_structure = new client_structure(host,Integer.parseInt(port));
	             
	             client_list.add(client_structure);
                  
	             while (true) {
		    		 
	 	    	 //System.out.println("waiting for option");
	 	    		 String option = in.readLine();
	 	    		 Integer opt = Integer.parseInt(option);
	 	    		// System.out.println("received option"+option);
	 	             
	 	             
	 	           switch(opt){
	 	    		 
	 	            case 1  : 
	 	            	//  System.out.println("in option 1");
	 	            	  out.writeBytes(opt+"\n");
	 	            	  String listReq = in.readLine();
	 	            	  String[] listReq1 = listReq.split(" ");
	 	            	  System.out.println(listReq1[0]+" "+listReq1[1]+" "+listReq1[2]);
	 	            	  System.out.println(listReq1[3]+" "+listReq1[4]);
	 	            	  System.out.println(listReq1[5]+" "+listReq1[6]);
	 	            	  Integer size = rfc_list.size();
	 	            	  //System.out.println("size is" + size);
	 	            	  String sizeString = size.toString();
	 	            	  //System.out.println("size string is"+sizeString);
	 	            	  out.writeBytes(sizeString + "\n");
	 	            	  in.readLine();
	 	            	 String listResp= "P2P-CI/1.0 200 OK";
	 	            	  
	 	            	  for(int i=0; i<size; i++){
	 	            		  
	 	            		  rfc_structure rfc = rfc_list.get(i);
	 	            		  
	 	            		  String hostname1 = rfc.hostname;
	 	            		  Integer uploadPort = 0;
	 	            		  for(client_structure cl : client_list )
	 	            		  {
	 	            			  
	 	            			 if(cl.hostName.equals(hostname1.trim()))
	 	            			 {
	 	            				uploadPort = cl.uploadPort; 
	 	            				break;
	 	            			 }
	 	            			 
	 	            			 //System.out.println("" + uploadPort);
	 	            			 
	 	            			 
	 	 	            	  }
	 	            		// out.writeBytes(rfc.rfcNum + " " + rfc.title + " " + rfc.hostname +" " +uploadPort.toString()+"\n");
	 	            		 listResp += " RFC "+rfc.rfcNum+" "+rfc.title+" "+rfc.hostname + " "+uploadPort.toString();
	 	            	  }
	 	            	 // System.out.println("list resp"+listResp);
	 	            	  out.writeBytes(listResp+"\n");
	 	            	  break;
	 	            	  
	 	            case 2 :
	 	            	 Integer count=0;
		 	        	  ArrayList<rfc_structure> rfc_lookup = new ArrayList<rfc_structure>();
		 	            	  //System.out.println("in option 2");
		 	            	  out.writeBytes(option+ "\n");
		 	            	  
		 	            	 
		 	            	  Integer lookup1 = Integer.parseInt(in.readLine()) ;
		 	            	 // System.out.println("lookup is "+ lookup1);
		 	            	  
		 	            	  for(rfc_structure rf2 : rfc_list){
		 	            		  
		 	            		  if(rf2.rfcNum.equals(lookup1)){
		 	            			  count++;
		 	            			  rfc_lookup.add(rf2);
		 	            			  
		 	            		  }
		 	            	  }
		 	            	  
		 	            	  out.writeBytes(count.toString()+"\n");
		 	            	  if (Integer.parseInt(in.readLine()) == 0)
{
				//System.out.println ("RFC not found");
}
		 	          else{  	  
		 	            	 for(int i=0; i<count; i++){
		 	            		  
		 	            		  rfc_structure rfc = rfc_lookup.get(i) ;
		 	            		  String hostname1 = rfc.hostname;
		 	            		  Integer uploadPort = null;
		 	            		  for(client_structure cl : client_list )
		 	            		  {
		 	            			  if(cl.hostName.equals(hostname1)){
		 	            				  uploadPort = cl.uploadPort;
		 	            			 break;
		 	            			  }
		 	            			 
		 	            			//System.out.println("" + uploadPort);  
		 	            			 
		 	            		  }
		 	            		 out.writeBytes(rfc.rfcNum +" " + rfc.title +" " + rfc.hostname +" " +uploadPort.toString()+"\n");
		 	            	 } 
//		 	            		 String rfc_num = in.readLine();
//		 	            		 out.writeBytes(rfc_num+"\n");
//		 	            		 
//		 	            		 String hostname3 = in.readLine();
//		 	            		 out.writeBytes(hostname3+"\n");
//		 	            		 
//		 	            		 String rfctitle = in.readLine();
//		 	            		 
		 	            	//	 rfc_list.add(new rfc_structure(Integer.parseInt(rfc_num),rfctitle,hostname3));
		 	            		 
		 	            		 String addmsg = in.readLine();
		 	            		 String[] addmsg1 = addmsg.split(" ");
		 	            		 
		 	            		 System.out.println(addmsg1[0] + " " + addmsg1[1] + " " + addmsg1[2] + " " + addmsg1[3]);
		 	            		 System.out.println(addmsg1[4] + " " + addmsg1[5]);
		 	            		 System.out.println(addmsg1[6] + " " + addmsg1[7]);
		 	            		 System.out.println(addmsg1[8] + " " + addmsg1[9]);
		 	            		 
		 	            		 //System.out.println(rfc_list);
		 	            		rfc_list.add(new rfc_structure(Integer.parseInt(addmsg1[2]),addmsg1[9],addmsg1[5]));
		 	            		
		 	            		String msgAddResp = "P2P-CI/1.0 200 OK RFC "+ addmsg1[2]+" "+addmsg1[9]+" "+addmsg1[5]+" "+addmsg1[7];
		 	            		
		 	            		out.writeBytes(msgAddResp+"\n");
		 	            		  
		 	            	  
		 	            }	 
		 	            	 break;
	 	            	  
	 	           case 3  : 
	 	        	  Integer count2=0;
	 	        	  ArrayList<rfc_structure> rfc_lookup1 = new ArrayList<rfc_structure>();
	 	            	  //System.out.println("in option 3");
	 	            	  out.writeBytes(option+ "\n");
	 	            	  
	 	            	 String lookupReq = in.readLine();
	 	            	 String[] lookupReq1 = lookupReq.split(" ");
	 	            	 
	 	            	System.out.println(lookupReq1[0] + " " + lookupReq1[1] + " " + lookupReq1[2] + " " + lookupReq1[3]);
	            		 System.out.println(lookupReq1[4] + " " + lookupReq1[5]);
	            		 System.out.println(lookupReq1[6] + " " + lookupReq1[7]);
	            		 System.out.println(lookupReq1[8] + " " + lookupReq1[9]);
	 	            	  
	 	            	  Integer lookup11 = Integer.parseInt(lookupReq1[2]) ;
	 	            	  //System.out.println("lookup is "+ lookup11);
	 	            	  
	 	            	  for(rfc_structure rf2 : rfc_list){
	 	            		  
	 	            		  if(rf2.rfcNum.equals(lookup11)){
	 	            			  count2++;
	 	            			  rfc_lookup1.add(rf2);
	 	            			  
	 	            		  }
	 	            	  }
	 	            	  
	 	            	  out.writeBytes(count2.toString()+"\n");
	 	            	  in.readLine();
	 	            	  
	 	            	  String lookupResp= "P2P-CI/1.0 200 OK";
	 	            	  
	 	            	 for(int i=0; i<count2; i++){
	 	            		  
	 	            		  rfc_structure rfc = rfc_lookup1.get(i) ;
	 	            		  String hostname1 = rfc.hostname;
	 	            		  Integer uploadPort = 0;
	 	            		  for (client_structure cl : client_list)
	 	            		  {
	 	            			  if(cl.hostName.equals(hostname1)){
	 	            				  uploadPort = cl.uploadPort;
	 	            			 break;
	 	            			  }
	 	            			 
	 	            			//System.out.println("" + uploadPort);  
	 	            			 
	 	            		  }
	 	            		  
	 	            		  lookupResp += " RFC "+rfc.rfcNum+" "+rfc.title+" "+rfc.hostname + " "+uploadPort.toString();
	 	            		  
	 	            		// out.writeBytes(rfc.rfcNum + " " + rfc.title + " " + rfc.hostname +" " +uploadPort.toString()+"\n");
	 	            		  
	 	            	  }
	 	            	 
	 	            	 out.writeBytes(lookupResp+"\n");
	 	            	 break;
	 	            	 
	 	           case 4:
	 	        	   
	 	        	   ArrayList<Integer> client_delete = new ArrayList<Integer>();
	 	        	   ArrayList<Integer> rfc_delete = new ArrayList<Integer>();
	 	        	   Iterator<client_structure> it1 = client_list.iterator();
	 	        	   Iterator<rfc_structure> it2 = rfc_list.iterator();
	 	        	   
	 	        	  
	 	        	   
	 	        	   out.writeBytes(option+"\n");
	 	        	   String hostname = in.readLine();
	 	        	   
	 	        	  while(it1.hasNext()){
	 	        		   if(it1.next().hostName.equals(hostname)){
	 	        		   it1.remove();
	 	        		   }
	 	        	   }
	 	        	   
	 	        	 while(it2.hasNext()){
	 	        		   if(it2.next().hostname.equals(hostname)){
	 	        		   it2.remove();
	 	        		   }
	 	        	   }
//	 	        	   for(client_structure cs: client_list){
//	 	        		   if(cs.hostName.equals(hostname)){
//	 	        			   client_list.remove(cs);
//	 	        		   }
//	 	        	   }
//	 	        	   
//	 	        	  
//	 	        	   for(rfc_structure rf: rfc_list){
//	 	        		   if(rf.hostname.equals(hostname)){
//	 	        			   rfc_list.remove(rf);
//	 	        		   }
//	 	        	   }
	 	        	   
	 	        	    
	 	        	  
	 	        	   
	 	        	   System.out.println("rfc list length is" + rfc_list.size());
	 	        	   System.out.println("client list length is" + client_list.size());
	 	        	   
	 	        	   
	 	        	   in.close();
	 	        	   out.close();
	 	        	   client.close();
	 	            	 
	 	            	
	 	         
	           }
	             }
	    	
	    } catch (IOException e) {
	        System.err.println(e);
	    }
	    
	   
	    

	}

	}
