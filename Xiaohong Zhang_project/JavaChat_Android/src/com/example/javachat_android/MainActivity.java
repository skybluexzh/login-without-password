package com.example.javachat_android;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Button login = null;
	private TextView username;
	private EditText usernameinput;
	private DataOutputStream dout;//输出流  
	private OutputStream out;//输出流  
	private DataInputStream dis;//   
	private InputStream ins;  

	Socket socket = null; 
	String[] usernames;
	Intent intent = new Intent();

	/*
	public Handler myHandler = new Handler() {  
        @Override  

        public void handleMessage(Message msg) {  
            if (msg.what == 0x11) {  
                Bundle bundle = msg.getData();  
                //textView.setText("friendList:"+bundle.getString("msg"));
                intent.putExtra("userlist", bundle.getString("msg"));
            }  
        }  	

    };
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		username = (TextView)findViewById(R.id.username);
		usernameinput = (EditText)findViewById(R.id.usernameinput);

		login = (Button) findViewById(R.id.login);
		login.setOnClickListener(new LoginListener());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	class firstThread extends Thread{
		public firstThread(){

		}
		@Override
		public void run(){
			//while(true){
			/*	Message msg = new Message();  
			msg.what = 0x11;  
			Bundle bundle = new Bundle();  
			bundle.clear();
			 */ 
			String response="";
			try{
				socket = new Socket("149.125.31.85", 9999);
				
				ins=socket.getInputStream();  
				out=socket.getOutputStream();     
				dis = new DataInputStream(ins);           
				dout=new DataOutputStream(out);  
				
				String usernametrue = usernameinput.getText().toString();
				dout.writeUTF("LOGIN: "+usernametrue+","+"cybermed");//放输入的用户名和密码

				while(true){
					response = dis.readUTF();
					if(response.startsWith("USERLIST:")){
						usernames = response.substring(response.indexOf(' ')).split(" ");	
						//usernames = response.split(" ")[1].split(" ");
						dis.close();
						dout.close();
						socket.close();
						intent.putExtra("usernametrue", usernametrue);
						intent.putExtra("userlist", usernames);
						intent.setClass(MainActivity.this, FriendList.class);
						//使用这个Intent对象来启动ResultActivity
						MainActivity.this.startActivity(intent);
					}
				}

				/*   
				bundle.putString("msg",usernames);  
                msg.setData(bundle);  
                //发送消息 修改UI线程中的组件  
                myHandler.sendMessage(msg);
				 */
			}catch(IOException e){
				e.printStackTrace();	
			}

		}
	}


	class LoginListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			new firstThread().start();

			//使用这个Intent对象来启动ResultActivity


		}
	}

}
