/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.ConnectException;
import java.net.UnknownHostException;

/**
 * Client
 * Will connect to the server using a Socket. Provides an interface to
 * to that socket to read and write to the socket.
 * 
 * @author Cory Gross
 * @version October 22, 2012
 */
public class Client {
	private Socket server;
	private String username;
	private DataOutputStream dout;//输出流  
	private OutputStream out;//输出流  
	private DataInputStream dis;//   
	private InputStream ins;  
	
	
	public boolean login(String user, String pass) {
		boolean accepted = false;
		try{
			dout.writeUTF("LOGIN: " + user + "," + pass);
			//dout.flush();
		}catch(IOException e) {
			System.err.println(e);
			e.printStackTrace();
		}

		String response;
		try {
			response = dis.readUTF();
			System.out.println("Response: " + response);
			if(response.equals("ACCEPTED")) {
				accepted = true;
				//username = user;
				setUsername(user);
			}
		} catch(IOException e) {
			System.err.println(e);
			e.printStackTrace();
		}

		return accepted;
	}

	public void connect(String ip, short port) throws ConnectException, UnknownHostException, IOException {
		server = new Socket(ip, port);
		try {
			ins=server.getInputStream();  
			out=server.getOutputStream();     
			dis = new DataInputStream(ins);           
			dout=new DataOutputStream(out);  

		} catch (IOException e) {
			System.err.println(e);
			e.printStackTrace();
		}    
	}

	/** Attempt to close the connection, including input/output streams. */
	public boolean disconnect() {
		try {
			server.close();
			dis.close();
			dout.close();
		} catch(IOException e) {
			System.err.println(e);
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/** Write to the connection socket */
	public void write(String msg) {
		try{
			dout.writeUTF(msg);
			//dout.flush();
		}catch(IOException e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

	/** Attempt to read from the connection socket. */
	public void readIMG(){
		try{
			int size=dis.readInt();
			FileOutputStream fos = new FileOutputStream("E:/YES.jpg");
			byte[] buffer = new byte[8192];
			int len;
			while (true){
				len = dis.read(buffer);
				fos.write(buffer, 0, len);
				fos.flush();
				size=size-len;
				if(size==0){
					fos.close();
					break;
				}
			}
			System.out.println("客户端接受图片完毕");
		}catch(IOException e){
			e.printStackTrace(); 
		}
	}

	public String read() {
		String line = null;
		try {
			line=dis.readUTF();
		} catch(IOException e) {
			System.err.println(e);
			e.printStackTrace();
		}
		return line;
	}

	public void sendChatMessage(String msg) {
		write(username + ": " + msg);
	}

	public void sendQuitMessage() {
		write("QUIT");
	}
	//添加
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
