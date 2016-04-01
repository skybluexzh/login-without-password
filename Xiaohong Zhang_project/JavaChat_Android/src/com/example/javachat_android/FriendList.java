
package com.example.javachat_android;


import java.io.ByteArrayOutputStream;
import java.io.File;

import android.R.id;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class FriendList extends Activity{
	private TextView textView;
	private Button takephoto;
	private CheckBox checkBox;
	String chosedList="";
	int num=0;
	Intent intent2 = new Intent();
	private static final int NONE = 0;
	private static final int PHOTO_GRAPH = 1;//拍照
	private static final int PHOTO_ZOOM = 2; // 缩放
	private static final int PHOTO_RESOULT = 3;// 结果
	private static final String IMAGE_UNSPECIFIED = "image/*";

	public void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.friendlist);  
		//绑定Layout里面的ListView  
		takephoto = (Button)findViewById(R.id.takephoto);
		textView = (TextView)findViewById(R.id.textView);
		Intent intent1 = getIntent();
		String usernametrue = intent1.getStringExtra("usernametrue");
		intent2.putExtra("usernametrue", usernametrue);
		String[] userlist = intent1.getStringArrayExtra("userlist");
		/*
		if(userlist==null){
			textView.setText("null");
		}else{
			for(int i=0;i<userlist.length;i++){
		textView.append(userlist[i]+"\n");
			}
		}
		 */
		LinearLayout listLayout =(LinearLayout) this.findViewById(R.id.listLayout);
		int rowCount = userlist.length-1; // 行总数

		showFriendList(userlist, listLayout, rowCount);

		takephoto.setOnClickListener(new OnClickListener(){
			@Override  
			public void onClick(View v){
				intent2.putExtra("chosedList", chosedList);

				Intent intent3 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent3.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"temp.jpg")));
				startActivityForResult(intent3, PHOTO_GRAPH);

				// intent2.setClass(FriendList.this, sendPhoto.class);
				//使用这个Intent对象来启动ResultActivity
				//	FriendList.this.startActivity(intent2);

			} 
		});

	}
	private void showFriendList(String[] userlist, LinearLayout listLayout,	int rowCount) {
		for(int i=1;i<rowCount;i++){
			checkBox=new CheckBox(this);
			checkBox.setId(i);
			checkBox.setText(userlist[i]);
			listLayout.addView(checkBox);
			// }

			checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					if(isChecked){
						//String chosedList = buttonView.getText()+"";
						chosedList+= buttonView.getText()+" ";
						//num++;
						//textView.setText(chosedList+num);

					}
				}
			});
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == NONE)
			return;//拍照
		if (requestCode == PHOTO_GRAPH) {
			// 设置文件保存路径
			File picture = new File(Environment.getExternalStorageDirectory() + "/temp.jpg");
			startPhotoZoom(Uri.fromFile(picture));
		}
		if (data == null)
			return;
		// 读取相册缩放图片
		if (requestCode == PHOTO_ZOOM) {
			startPhotoZoom(data.getData()); 
		}
		// 处理结果
		if (requestCode == PHOTO_RESOULT) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");
				//FileOutputStream b = new FileOutputStream(picture);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);

				//String username str = usernameinput.getText().toString();
				//Intent intent2 = new Intent();
				intent2.putExtra("Photo",photo);
				intent2.setClass(FriendList.this, sendPhoto.class);
				//使用这个Intent对象来启动ResultActivity
				FriendList.this.startActivity(intent2);
				//imageView.setImageBitmap(photo);//把图片显示在ImageView控件上 
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}


	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 500);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTO_RESOULT);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
