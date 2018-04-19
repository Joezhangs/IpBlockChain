package com.wevolution.ipchain;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.tiande.jbcc.clien.JBCCClien;
public class ConnectionManager {
    private static ThreadLocal<JBCCClien> connectionHolder = new ThreadLocal<JBCCClien>();
    private static String user;
	private static String[] nodeMqTcps;
	private static String userName;
	private static String pwd;
	static{
		Properties prop = new Properties(); 
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("util.properties");
        try {
            prop.load(inputStream); 
            user = prop.getProperty("jbcc.user").trim(); 
            nodeMqTcps = prop.getProperty("jbcc.node").trim().split(",");
            userName = prop.getProperty("jbcc.username").trim();
            pwd = prop.getProperty("jbcc.password").trim();
        }catch (IOException e) { 
            e.printStackTrace(); 
        }finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private ConnectionManager(){
		
	}
    
   /* private static String[] nodeMqTcps = new String[] { "tcp://192.168.0.201:61616?", "tcp://192.168.0.202:61616?",
			"tcp://192.168.0.203:61616?", "tcp://192.168.0.204:61616?" };*/
    /** 
     * 连接JBCCClien 
     * @return 
     */  
    public static JBCCClien getConnection(){  
    	JBCCClien conn = connectionHolder.get();  
        if(conn == null){  
            try {
            	JBCCClien jbccClient = new JBCCClien();
        		//jbccClient.startClien("admin_1", nodeMqTcps, "admin", "123");              
            	jbccClient.startClien(user, nodeMqTcps, userName, pwd);              
                conn = jbccClient;  
                connectionHolder.set(conn);  
            } catch (Exception e) {  
                e.printStackTrace();  
            } 
        }  
        return conn;                                      
          
    }  
    /** 
     * 关闭JBCCClien，清除集合中的JBCCClien 
     */  
    public static void closeConnection(){  
    	JBCCClien conn = connectionHolder.get();  
        if(conn != null){  
            try{  
                conn.close();  
                connectionHolder.remove();  
            }catch(Exception e){  
                e.printStackTrace();  
            }  
  
        }  
    }  
}
