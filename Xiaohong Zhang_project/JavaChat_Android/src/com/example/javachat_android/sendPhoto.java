package com.example.javachat_android;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class sendPhoto extends Activity {

	private ImageView imageView = null;
	private Button Send = null;
	private Socket socket = null;
	private DataOutputStream dout;// 输出流
	private OutputStream out;// 输出流
	private DataInputStream dis;//
	private InputStream ins;
	String buffer = "";
	String usernametrue;
	String ChosedList;
	Intent intent2 = new Intent();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendphoto);

		imageView = (ImageView) findViewById(R.id.imageView);
		Send = (Button) findViewById(R.id.Send);

		Intent intent = getIntent();
		usernametrue = intent.getStringExtra("usernametrue");
		ChosedList = intent.getStringExtra("chosedList");
		final Bitmap photo = intent.getParcelableExtra("Photo");
		imageView.setImageBitmap(photo);// 把图片显示在ImageView控件上
		Send.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// geted1 = ed1.getText().toString();
				// txt1.append("client:"+geted1+"\n");
				// 启动线程 向服务器发送和接收信息
				new MyThread().start();
			}
		});
	}

	class MyThread extends Thread {

		@Override
		public void run() {
			try {
				// 连接服务器 并设置连接超时为5秒
				socket = new Socket();
				socket.connect(new InetSocketAddress("149.125.31.85", 9999), 5000);
				// 获取输入输出流
				ins = socket.getInputStream();
				out = socket.getOutputStream();
				dis = new DataInputStream(ins);
				dout = new DataOutputStream(out);

				dout.writeUTF("LOGIN: " + usernametrue + "," + "cybermed");// 放输入的用户名和密码
				while (true) {
					String response = dis.readUTF();
					if (response.equals("ACCEPTED")) {
						dout.writeUTF("Authorization:" + usernametrue+" "+ChosedList);
						break;
					}
				}
				try {
					FileInputStream fis = new FileInputStream(
							Environment.getExternalStorageDirectory()
									+ "/temp.jpg");
					BufferedInputStream bis = new BufferedInputStream(fis);
					int size = fis.available();
					dout.writeInt(size);
					byte[] buffer = new byte[8192];
					int len;
					//len = fis.read(buffer);
					while (true) {
						len = bis.read(buffer);
						dout.write(buffer, 0, len);
						dout.flush();
						//len = fis.read(buffer);
						size=size-len;
						
						System.out.println(len);
						if (size==0) {
							bis.close();
							//out.flush();
							break;
						}
					}
				} catch (Exception e) {
				}
				while (true) {
					String response = dis.readUTF();
					if (response.startsWith("agreement")) {
						String authName = response.split(":")[1];
						dout.writeUTF("Mobile Leaving EXIT");
						dis.close();
						dout.close();
						socket.close();
						intent2.putExtra("information","User " + authName + " authorized you,Login Successfully.");
						intent2.setClass(sendPhoto.this, FinalPage.class);
						//使用这个Intent对象来启动ResultActivity
						sendPhoto.this.startActivity(intent2);
						break;
					}
					else if(response.startsWith("disagreement")){
						dout.writeUTF("Mobile Leaving EXIT");
						dis.close();
						dout.close();
						socket.close();
						intent2.putExtra("information", "Login request denied.");
						intent2.setClass(sendPhoto.this, FinalPage.class);
						//使用这个Intent对象来启动ResultActivity
						sendPhoto.this.startActivity(intent2);
						break;
					}
				}
			} catch (SocketTimeoutException aa) {
				aa.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
