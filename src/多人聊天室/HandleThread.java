package 多人聊天室;


import java.io.DataInputStream;

import java.io.DataOutputStream;
import java.io.IOException;

import java.net.ServerSocket;

import java.net.Socket;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;


public class HandleThread {
	    
	    private final int port=10086;
	    private Socket client;
	    private Server server;
	    //连接的socket列表
	    private ArrayList<Socket> socketList=new ArrayList<>();
	    
	    //发送的消息
	    private  String msg;
	    //接收的消息
	    private  String recvMsg;
	    
	    private final String serverName="Server";
	    
	    Vector<String> nameList;
	    
	    public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public String getRecvMsg() {
			return recvMsg;
		}

		public void setRecvMsg(String recvMsg) {
			this.recvMsg = recvMsg;
		}
		
		//连接需要发送的消息
		public void linkMsg() {
			this.msg="\n"+serverName+":"+getDate()+":"+msg;
		}
	    
		//获取系统时间
		public String  getDate() {
			Calendar  now=Calendar.getInstance();
			return new SimpleDateFormat("hh:mm:ss").format(now.getTime());
		}
		
		
	    //创建即处理连接
	    public HandleThread(){
	    	server=new Server();
	    	connect();
	    }
	    
	    //获取用户列表
	    public Vector<String> getClientNameList(){
	    	nameList=new Vector<>();
	    	for(Socket s:socketList) {
	    		nameList.add("client"+s.getPort());
	    	}
	    	return nameList;
	    }
	    
	    
	    //更新用户列表
	    public void updateList() {
	    	nameList.removeAllElements();
	    	for(Socket s:socketList) {
	    		nameList.add("client"+s.getPort());
	    	}
	    	server.userList.setListData(nameList);//一定要重新设置属性
	    }
	    
	    //建立连接
	    public void connect(){
	        System.out.println("Server Started...");       	       
	        try{
	            ServerSocket ss=new ServerSocket(port);
	            while(true){
	                //接入就建立连接,并存入列表中
	                client=ss.accept();
	                socketList.add(client);
	                //列表显示
	                server.userList.setListData(getClientNameList());
	                //创建服务器的接收消息线程和发送消息线程
	                new HandleRecv(client);
	                new HandleSend(client);
	            }
	        }catch(Exception e){
	            e.printStackTrace();
	        }
	    }      
	    
	    //发送消息线程   
	    class HandleSend implements Runnable {

	        private final Socket client;

	        public HandleSend(Socket client) {
	            this.client = client;
	            new Thread(this).start();
	        }

	        @Override
	        public void run() {
	        	boolean isExit=false;
	        		while (true) {
	                	try {
	                    //发送数据                	
	                	DataOutputStream out = new DataOutputStream(client.getOutputStream());
		                //点击发送则发送数据
		                if(server.isClicked()) {
		                	msg=server.getInput();
		                    linkMsg();
		                    out.writeUTF(msg);	                    	
		                    server.setClicked(false);
		                    server.showMsg(msg);
		                }
	                		                    	                    	                                       
	                }catch (Exception e) {
	                	isExit=true;
	                	socketList.remove(this.client);	 
	                	updateList();
		            }
	            } 

	        }

	    }

	      
	    //接收消息线程
	    class HandleRecv implements Runnable {
	    	
	        private final Socket client;
	        
	        public HandleRecv(Socket client) {
	            this.client = client;
	            new Thread(this).start();
	        }        
	        @Override
	        public void run() {
	            	boolean isExit=false;
	                while (!isExit) {
	                	try {
	                		//如果客户端还在线	                
	                		DataInputStream in = new DataInputStream(client.getInputStream());
	 	                    recvMsg = in.readUTF().trim();
	 	                    //转发给所有的用户
	 	                    sendMsgToAll(client,recvMsg); 	                    
	 	                    server.showMsg("\n"+recvMsg);	                	                                       
	                }catch (Exception e) {
	                	isExit=true;
	                	socketList.remove(this.client);
		                updateList();
		            }
	            } 
	        }
	    }
	    
	    //转发消息给所有的客户端
	    public void sendMsgToAll(Socket client,String recvMsg) throws IOException {
	    	DataOutputStream out;
	    	for(Socket s:socketList) {
	    		
	    		System.out.println(s.toString());
	    		
	    		if(!s.equals(client)) {//除了发送消息的客户端，都转发一遍
	    			out = new DataOutputStream(s.getOutputStream());
			    	out.writeUTF(recvMsg);
	    		}    	    		    		
	    	}
	    }
	    
	    public static void main(String[] args) {
			new HandleThread();
		}
		
}
