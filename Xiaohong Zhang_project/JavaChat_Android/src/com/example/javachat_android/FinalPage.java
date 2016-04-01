package com.example.javachat_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class FinalPage extends Activity {
	private TextView textViewfinal;
    private	Intent intent = new Intent();
    private String result;
    
    public void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.finalpage);  
		intent = getIntent();
		result = intent.getStringExtra("information");
		textViewfinal = (TextView)findViewById(R.id.textViewfinal);
		textViewfinal.setText(result);
    }
}