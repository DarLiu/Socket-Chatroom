package ����������;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Client {
		
	private String clientName;
    private final int port = 10086;
    private final String addr = "localhost";
    private Socket s;

    
    public Client(String name) {
    	this.clientName=name;
	}
    
    public String  getDate() {
    	Calendar  now=Calendar.getInstance();
		return new SimpleDateFormat("hh:mm:ss").format(now.getTime());
	}
    
    public void connect() throws IOException {
        System.out.println("Client is start...");       
        s = new Socket(addr, port);
        new HandleSend(s);
        new HandleRecv(s);

    }

    class HandleSend implements Runnable {

        private Socket s;

        public HandleSend(Socket s) {
            this.s = s;
            new Thread(this).start();
        }

        @Override
        public void run() {
            try {

                while (s != null) {
                    //��������
                    DataOutputStream out = new DataOutputStream(s.getOutputStream());//���������������
                    String msg = new BufferedReader(new InputStreamReader(System.in)).readLine();
                    if(!msg.equals(""))
                    	out.writeUTF("\n"+getDate()+" # "+msg);
                    if (s == null) {
                        out.close();
                        break;
                    }
                }
            } catch(Exception e) {
            	System.out.println("�������ѶϿ�����");           	
            }

        }
    }
        
    

    class HandleRecv implements Runnable {

        private Socket s;

        public HandleRecv(Socket s) {
            this.s = s;
            new Thread(this).start();
        }

        @Override
        public void run() {
            try {
                while (s != null) {
                    DataInputStream in = new DataInputStream(s.getInputStream());//��ȡ����������
                    //��������
                    String recvMsg = in.readUTF().trim();
                    System.out.println(recvMsg);
                    
                }

            } catch (Exception e) {
            	System.out.println("�������ѶϿ�����");
            }

        }

    }

    
    
    public static void main(String[] args) throws IOException {   	
    		new Client("client").connect();
    	}  
    
  
}
