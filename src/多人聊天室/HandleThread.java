package ����������;


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
	    //���ӵ�socket�б�
	    private ArrayList<Socket> socketList=new ArrayList<>();
	    
	    //���͵���Ϣ
	    private  String msg;
	    //���յ���Ϣ
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
		
		//������Ҫ���͵���Ϣ
		public void linkMsg() {
			this.msg="\n"+serverName+":"+getDate()+":"+msg;
		}
	    
		//��ȡϵͳʱ��
		public String  getDate() {
			Calendar  now=Calendar.getInstance();
			return new SimpleDateFormat("hh:mm:ss").format(now.getTime());
		}
		
		
	    //��������������
	    public HandleThread(){
	    	server=new Server();
	    	connect();
	    }
	    
	    //��ȡ�û��б�
	    public Vector<String> getClientNameList(){
	    	nameList=new Vector<>();
	    	for(Socket s:socketList) {
	    		nameList.add("client"+s.getPort());
	    	}
	    	return nameList;
	    }
	    
	    
	    //�����û��б�
	    public void updateList() {
	    	nameList.removeAllElements();
	    	for(Socket s:socketList) {
	    		nameList.add("client"+s.getPort());
	    	}
	    	server.userList.setListData(nameList);//һ��Ҫ������������
	    }
	    
	    //��������
	    public void connect(){
	        System.out.println("Server Started...");       	       
	        try{
	            ServerSocket ss=new ServerSocket(port);
	            while(true){
	                //����ͽ�������,�������б���
	                client=ss.accept();
	                socketList.add(client);
	                //�б���ʾ
	                server.userList.setListData(getClientNameList());
	                //�����������Ľ�����Ϣ�̺߳ͷ�����Ϣ�߳�
	                new HandleRecv(client);
	                new HandleSend(client);
	            }
	        }catch(Exception e){
	            e.printStackTrace();
	        }
	    }      
	    
	    //������Ϣ�߳�   
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
	                    //��������                	
	                	DataOutputStream out = new DataOutputStream(client.getOutputStream());
		                //���������������
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

	      
	    //������Ϣ�߳�
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
	                		//����ͻ��˻�����	                
	                		DataInputStream in = new DataInputStream(client.getInputStream());
	 	                    recvMsg = in.readUTF().trim();
	 	                    //ת�������е��û�
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
	    
	    //ת����Ϣ�����еĿͻ���
	    public void sendMsgToAll(Socket client,String recvMsg) throws IOException {
	    	DataOutputStream out;
	    	for(Socket s:socketList) {
	    		
	    		System.out.println(s.toString());
	    		
	    		if(!s.equals(client)) {//���˷�����Ϣ�Ŀͻ��ˣ���ת��һ��
	    			out = new DataOutputStream(s.getOutputStream());
			    	out.writeUTF(recvMsg);
	    		}    	    		    		
	    	}
	    }
	    
	    public static void main(String[] args) {
			new HandleThread();
		}
		
}
