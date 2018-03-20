package 多人聊天室;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream.GetField;
import java.nio.channels.ShutdownChannelGroupException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;

public class Server{

	private JPanel contentPane;
	
	private JTextField inputArea;

	protected JList userList;
	
	protected JTextArea recordPane;
	
	private JFrame frame;
	
	private String msg;
	
	private boolean isClicked=false;
	
	
	
	public boolean isClicked() {
		return isClicked;
	}

	public void setClicked(boolean isClicked) {
		this.isClicked = isClicked;
	}

	
	//获取文本框的输入
	public String getInput() {
		return inputArea.getText();
	}

	/**
	 * Create the frame.
	 */
	public Server() {				
		frame=new JFrame("Server");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 899, 618);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		//用户列表
		userList = new JList<String>();
		userList.setBounds(7, 9, 152, 457);
		contentPane.add(userList);
		
		//发送按钮
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				isClicked=true;
				inputArea.setText("");
			}
		});
		btnSend.setBounds(745, 498, 124, 69);
		contentPane.add(btnSend);
		
		//聊天记录面板
		recordPane = new JTextArea();
		recordPane.setBounds(172, 9, 684, 457);
		contentPane.add(recordPane);
		
		
		//输入框
		inputArea = new JTextField();
		inputArea.setBounds(2, 498, 733, 69);
		contentPane.add(inputArea);
		inputArea.setColumns(10);
		frame.setVisible(true);	
				
	}
	
	
	//面板显示聊天
	public void showMsg(String msg) {
		recordPane.append(msg);
	}
    
}
