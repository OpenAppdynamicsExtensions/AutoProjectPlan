package com.appdynamics.jrbronet.projectplan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import com.appdynamics.jrbronet.projectplan.*;


public class AppDynamicsControllerRESTClient {
		
		private ArrayList appList = new ArrayList();
		private ArrayList nodeList = new ArrayList();
		
		public ArrayList getAppList(){
			return this.appList;
		}
		
		public ArrayList getNodeList(){
			return nodeList;
		}
	
	    public void buildAppList(String HostURL, String name, String password)  {
	    	// /controller/rest/applications/LIFE-ECOMM-SYS/nodes
	    	// /controller/rest/applications/
	    	try {
				String AppsURL = HostURL+"/controller/rest/applications";
				////System.out.println("URL: " + AppsURL);
				
				String authString = name + ":" + password;
				////System.out.println("auth string: " + authString);
				byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
				String authStringEnc = new String(authEncBytes);
				////System.out.println("Base64 encoded auth string: " + authStringEnc);				
				
				URL url = new URL(AppsURL);
				URLConnection urlConnection = url.openConnection();
				urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
				InputStream is = urlConnection.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);

				int numCharsRead;
				char[] charArray = new char[1024];
				StringBuffer sb = new StringBuffer();
				while ((numCharsRead = isr.read(charArray)) > 0) {
					sb.append(charArray, 0, numCharsRead);
				}
				String result = sb.toString();
							
				int idx1=0,idx2=0;
				for(idx1 = result.indexOf("<name>", idx2);idx1!=-1;idx1 = result.indexOf("<name>", idx2)){	
					if(idx1 != -1){
						idx2 = result.indexOf("</name>", idx1);
						String appName = result.substring(idx1+6, idx2);
						//System.out.println("-----------------------");
						//System.out.println(appName);					
						appList.add(new ADApplication(appName));
						getAppNodes(HostURL, name, password, appName);
					}
				}		
				//System.out.println("*** BEGIN ***");
				//System.out.println(result);
				//System.out.println("*** END ***");								
				
			} catch (Exception e) {
				e.printStackTrace();
			} 
	    }
	    
	    public boolean getOnlineStatus(String HostURL, String name, String password,String appName, String tierName, String nodeName){
	    	// https://aviva1.saas.appdynamics.com/controller/rest/applications/LIFE-ECOMM-SYS/metric-data?metric-path=Application%20Infrastructure%20Performance%7CUKLAP_SYS_WP%7CIndividual%20Nodes%7CUKLAP_SYS_WP_POR_C1_S1%7CAgent%7CApp%7CAvailability&time-range-type=BEFORE_NOW&duration-in-mins=5
	    	try {
	    		//appName=URLEncoder.encode(appName, "UTF-8");
	    		String nodeURL = HostURL+"/controller/rest/applications/"+URLEncoder.encode(appName, "UTF-8")+"/metric-data?metric-path=Application%20Infrastructure%20Performance%7C"+URLEncoder.encode(tierName, "UTF-8")+"%7CIndividual%20Nodes%7C"+URLEncoder.encode(nodeName, "UTF-8")+"%7CAgent%7CApp%7CAvailability&time-range-type=BEFORE_NOW&duration-in-mins=1";
	    		////System.out.println("URL: " + nodeURL);
				
				String authString = name + ":" + password;
				////System.out.println("auth string: " + authString);
				byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
				String authStringEnc = new String(authEncBytes);
				////System.out.println("Base64 encoded auth string: " + authStringEnc);				
				
				URL url = new URL(nodeURL);
				URLConnection urlConnection = url.openConnection();
				urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
				InputStream is = urlConnection.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);

				int numCharsRead;
				char[] charArray = new char[1024];
				StringBuffer sb = new StringBuffer();
				while ((numCharsRead = isr.read(charArray)) > 0) {
					sb.append(charArray, 0, numCharsRead);
				}
				String result = sb.toString();
				
				
				int idx1=0,idx2=0;
				for(idx1 = result.indexOf("<current>", idx2);idx1!=-1;idx1 = result.indexOf("<current>", idx2)){	
					if(idx1 != -1){					
						idx2 = result.indexOf("</current>", idx1);
						String status = result.substring(idx1+9, idx2);
						//System.out.println("\t\t\t\t"+status);	
						if(status.equals("1"))
							return true;
					}
				}
				
				return false;
				
				/*
				//System.out.println("*** BEGIN ***");
				//System.out.println(result);
				//System.out.println("*** END ***");
				*/								
				
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} 
	    }
	    
	    public void getAppNodes(String HostURL, String name, String password,String appName)  {
	    	// /controller/rest/applications/LIFE-ECOMM-SYS/nodes
	    	// /controller/rest/applications/
	    	
	    	
	    	try {
	    		//appName=URLEncoder.encode(appName, "UTF-8");
	    		String nodeURL = HostURL+"/controller/rest/applications/"+URLEncoder.encode(appName, "UTF-8")+"/nodes";
	    		//System.out.println("URL: " + nodeURL);
				
				String authString = name + ":" + password;
				//System.out.println("auth string: " + authString);
				byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
				String authStringEnc = new String(authEncBytes);
				//System.out.println("Base64 encoded auth string: " + authStringEnc);				
				
				URL url = new URL(nodeURL);
				URLConnection urlConnection = url.openConnection();
				urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
				InputStream is = urlConnection.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);

				int numCharsRead;
				char[] charArray = new char[1024];
				StringBuffer sb = new StringBuffer();
				while ((numCharsRead = isr.read(charArray)) > 0) {
					sb.append(charArray, 0, numCharsRead);
				}
				String result = sb.toString();
				
				
				int idx1=0,idx2=0, idx3, idx4, idxAux;
				for(idx1 = result.indexOf("<tierName>", idx2);idx1!=-1;idx1 = result.indexOf("<tierName>", idx2)){	
					if(idx1 != -1){
						idxAux = idx2;
						idx2 = result.indexOf("</tierName>", idx1);
						String tierName = result.substring(idx1+10, idx2);
						//System.out.println("\t"+tierName);
						idx3 = result.indexOf("<name>", idxAux);
						idx4 = result.indexOf("</name>", idx3);
						String nodeName = result.substring(idx3+6, idx4);
						//System.out.println("\t\t"+nodeName);
						idx3 = result.indexOf("<type>", idxAux);
						idx4 = result.indexOf("</type>", idx3);
						String type = result.substring(idx3+6, idx4);
						
						//String type = getNodeType(HostURL, name, password, appName, tierName);
						type = getNodeTypeSimple(type);
						
						idx3 = result.indexOf("<machineName>", idxAux);
						idx4 = result.indexOf("</machineName>", idx3);
						String hostName = result.substring(idx3+13, idx4);
						if (getOnlineStatus(HostURL, name, password, appName,tierName,nodeName))
							nodeList.add(new ADNode(nodeName,type,hostName,appName,tierName));
					}
				}
				
				/*
				//System.out.println("*** BEGIN ***");
				//System.out.println(result);
				//System.out.println("*** END ***");
				*/								
				
			} catch (Exception e) {
				e.printStackTrace();
			} 
	    }
	    
	    public void addLicenseCount(String appName, String type){
	    	for (int i = 0; i < this.appList.size(); i++) {
	    		ADApplication addApp =  (ADApplication)appList.get(i);
	    		if(addApp.getName().equals(appName)){
	    			addApp.increaseLicenseCount(type);
	    			//System.out.println("RestClient - "+type+" "+addApp.getLicenseCount().size());
	    			/*
	    			if(type.contains("IIS"))
	    				addApp.increaseNetCount();
	    			if(type.contains("Other"))
	    				addApp.increaseJavaCount();
	    			if(type.contains("PHP"))
	    				addApp.increasePHPCount();
	    			*/
	    		}
	    	}
	    }
	    /*
	    public void removeNodesWithSameHostName(String hostName){
	    	int size = this.nodeList.size();
	    	for (int i = 0; i < size; i++) {
				ADNode node = (ADNode)nodeList.get(i);
				if(node.getHostName().equals(hostName) && (node.getType().contains("NET"))){
					System.out.println("Removing entry at "+i);
					nodeList.remove(i);
					size = this.nodeList.size();					
				}
					
			}
	    }
	    */
	    
	    public void calculateLicenses(){
	    	int size = this.nodeList.size();
	    	for (int i = 0; i < size; i++) {
				ADNode node = (ADNode)nodeList.get(i);
				addLicenseCount(node.getAppName(),node.getType());
				// Ahora si es tipo IIS quitar todos los nodos que tengan ese mismo hostName
				if(node.getType().contains("NET")){
					System.out.println("Deleting other .Net nodes for the host "+node.getHostName());
					//removeNodesWithSameHostName(node.getHostName());
					//size = this.nodeList.size();	
					for (int j = i+1; j < size; j++) {
						ADNode node2 = (ADNode)nodeList.get(j);
						// Check again .Net, as in theory the same node can have a JVM and a .Net agent it has be properly counted
						if(node2.getHostName().equals(node.getHostName()) && (node2.getType().contains("NET"))){
							System.out.println("Removing entry at "+j);
							nodeList.remove(j);
							size = this.nodeList.size();					
						}							
					}
				}
			}    	
	    }
	    
	    public String getNodeTypeSimple(String type){
	    	if(type.contains("IIS") || type.contains("NET"))
				return ".NET";
			if(type.contains("PHP"))
				return "PHP";
			if(type.contains("Node"))
				return "Node.js";
			if(type.contains("Machine"))
				return "Machine Only";
			return "Java Agent";
	    	
	    }
	    
	    public String getNodeType(String HostURL, String name, String password, String appName, String tierName){
	    	// /controller/rest/applications/ACME Online Book Store/tiers/ECommerce
	    	// /controller/rest/applications/GI-ECOMM-SYS4/tiers/CommFW
	    	// appName=URLEncoder.encode(appName, "UTF-8");
    		
    		try{
    			String nodeURL = HostURL+"/controller/rest/applications/"+URLEncoder.encode(appName, "UTF-8")+"/tiers/"+URLEncoder.encode(tierName,"UTF-8");
        		//System.out.println("URL: " + nodeURL);
    			
				String authString = name + ":" + password;
				//System.out.println("auth string: " + authString);
				byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
				String authStringEnc = new String(authEncBytes);
				//System.out.println("Base64 encoded auth string: " + authStringEnc);				
				
				URL url = new URL(nodeURL);
				URLConnection urlConnection = url.openConnection();
				urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
				InputStream is = urlConnection.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
	
				int numCharsRead;
				char[] charArray = new char[1024];
				StringBuffer sb = new StringBuffer();
				while ((numCharsRead = isr.read(charArray)) > 0) {
					sb.append(charArray, 0, numCharsRead);
				}
				String result = sb.toString();
				
				int idx1=0,idx2=0;
				for(idx1 = result.indexOf("<type>", idx2);idx1!=-1;idx1 = result.indexOf("<type>", idx2)){	
					if(idx1 != -1){					
						idx2 = result.indexOf("</type>", idx1);
						String type = result.substring(idx1+6, idx2);
						System.out.println("\t\t\t\t"+type);	
						if(type.contains(".NET"))
							return ".NET";
						if(type.contains("PHP"))
							return "PHP";
						if(type.contains("Node"))
							return "NodeJS";
						if(type.contains("Machine"))
							return "Machine Only";
						return "Java Agent";
					}
				}
				
    		} catch (Exception e) {
				e.printStackTrace();
			} 
			
			return null;
	    	
	    }
	    
	    public void printLicenses(){	    	
	    	for (int i = 0; i < this.appList.size(); i++) {
				ADApplication app = (ADApplication)this.appList.get(i);				
				//System.out.println(i+"."+app.getName()+" "+app.getJavaCount()+" "+app.getNetCount()+" "+app.getPHPCount());
				HashMap licenses = app.getLicenseCount();
				Iterator it = licenses.entrySet().iterator();
			    while (it.hasNext()) {
			        Map.Entry pairs = (Map.Entry)it.next();
			        System.out.println(app.getName()+": "+pairs.getKey() + "=" + pairs.getValue() + " ");
			    }
			}
	    }
	}
	  

