package chatserver;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.io.DataOutputStream;

import javax.swing.JTextArea;


public class Session {
	private String username;
	private Socket socket;
	private DataOutputStream dout;//输出流  
	private OutputStream out;//输出流  
	private DataInputStream dis;//   
	private InputStream ins;  

	/** Constructor establishes a Connection for a given connected socket.
	 * 
	 * @param socket A connected socket for communication.
	 */
	Session(Socket socket) {
		this.socket = socket;
		try {
			ins=socket.getInputStream();  
			out=socket.getOutputStream();     
			dis = new DataInputStream(ins);           
			dout=new DataOutputStream(out);  
		} catch(IOException e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

	/** Write to the connection socket */
	public void write(String msg) {
		try{
			dout.writeUTF(msg);
		}catch(Exception e){

		}
	}

	public void writeInt(int msg){
		try{
			dout.writeInt(msg);
		}
		catch(Exception e){

		}
	}
	
	public void writeIMG(){
		try{ 
			FileInputStream fis = new FileInputStream("F:/YES.jpg");
			BufferedInputStream bis = new BufferedInputStream(fis);
			int size =bis.available();
			dout.writeInt(size);
			byte[] buffer = new byte[8192];
			int len;
			while (true){
				if(size==0){
					dout.flush();
					bis.close();
					//dout.close();
					break;
				}
				else{
					len = bis.read(buffer);
					dout.write(buffer, 0, len);
					dout.flush();
					size=size-len;
				}
			}


		}catch(Exception e){

		}
	}

	/** Attempt to read from the connection socket. */
	public String read() {
		String line = null;
		try {
			line=dis.readUTF();
		}
		catch(SocketException e) {
			System.out.println("Log: Client disconnected, session ended");
		}
		catch(IOException e) {
			System.err.println(e);
			e.printStackTrace();
		}
		return line;
	}

	public void readIMG(){
		try{
			int size = dis.readInt();
			FileOutputStream fos = new FileOutputStream("F:/YES.jpg");
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			byte[] buffer = new byte[8192];
			int len;
			//len = ins.read(buffer);
			while(true){
				len = dis.read(buffer);//之前是ins
				bos.write(buffer, 0, len);
				bos.flush();
				size=size-len;
				System.out.println(size);
				if(size==0){
					//fos.flush();
					bos.close();
					break;
				}
			}
			System.out.println("Finish readIMG");

		}catch(IOException e){
			e.printStackTrace(); 
		}
	}

	/** Attempt to close the connection, including input/output streams. */
	public boolean disconnect() {
		try {
			socket.close();
			dis.close();
			dout.close();
		} catch(IOException e) {
			System.err.println(e);
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/** Set the username associated with the given connection */
	public void setUsername(String username) {
		this.username = username;
	}

	public Socket getSocket() { return socket; }

	public String getUsername() { return username; }
}