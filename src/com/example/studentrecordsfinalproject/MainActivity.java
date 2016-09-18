package com.example.studentrecordsfinalproject;

import android.os.Bundle;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.provider.MediaStore;
import android.R.string;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	EditText name,roll,marks,marks1,marks2;
	Button add,delete,result,update,capture,view;
	SQLiteDatabase db;
	ImageView img;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		add= (Button) findViewById(R.id.add);
		delete = (Button) findViewById(R.id.delete);
		result = (Button) findViewById(R.id.result);
		update = (Button) findViewById(R.id.update);
		name = (EditText) findViewById(R.id.name1);
		roll = (EditText) findViewById(R.id.roll_no);
		marks = (EditText) findViewById(R.id.marks1);
		marks1 = (EditText) findViewById(R.id.marks2);
		view= (Button) findViewById(R.id.view);

		marks2 = (EditText) findViewById(R.id.marks3);

		img= (ImageView) findViewById(R.id.imageView1);
		capture = (Button) findViewById(R.id.capture);
		
		db=openOrCreateDatabase("Student_record", Context.MODE_PRIVATE, null);
		db.execSQL("CREATE TABLE IF NOT EXISTS student(rollno INTEGER,name CHAR,marks INTEGER,marks1 INTEGER,marks2 INTEGER);");
		
		capture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(i, 1);
				
				
			}
		});
		
		
		add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if 
				( roll.getText().toString().trim().length()==0|| name.getText().toString().trim().length()==0 ||marks.getText().toString().trim().length()==0||
				marks1.getText().toString().trim().length()==0||marks2.getText().toString().trim().length()==0)
				{
					showMessage("Error", "!!!!please enter the info (cant be left blank)");
					return;
				}
				else
					{db.execSQL("INSERT INTO student VALUES('"+roll.getText()+"','"+name.getText()+
		    				   "','"+marks.getText()+"','"+marks1.getText()+"','"+marks2.getText()+"');");
					showMessage("Success", "Record added successfully");
					clearText();
					}		
				
			}
					
				
			

				
				
			
		});
delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(roll.getText().toString().trim().length()==0)
	    		{
	    			showMessage("Error", "Please enter Rollno");
	    			return;
	    		}
	    		Cursor c=db.rawQuery("SELECT * FROM student WHERE rollno='"+roll.getText()+"'", null);
	    		if(c.moveToFirst())
	    		{
	    			db.execSQL("DELETE FROM student WHERE rollno='"+roll.getText()+"'");
	    			showMessage("Success", "Record Deleted");
	    		}
	    		else
	    		{
	    			showMessage("Error", "Invalid Rollno");
	    		}
	    		clearText();
			}
		});
update.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(roll.getText().toString().trim().length()==0)
		{
			showMessage("Error", "Please enter Rollno");
			return;
		}
		Cursor c=db.rawQuery("SELECT * FROM student WHERE rollno='"+roll.getText()+"'", null);
		if(c.moveToFirst())
		{
			db.execSQL("UPDATE student SET name='"+name.getText()+"',marks='"+marks.getText()+
					"',marks1='"+marks1.getText()+
					"',marks2='"+marks2.getText()+
					"' WHERE rollno='"+roll.getText()+"'");
			showMessage("Success", "Record Modified");
		}
		else
		{
			showMessage("Error", "Invalid Rollno");
		}
		clearText();
	}
});
view.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(roll.getText().toString().trim().length()==0)
		{
			showMessage("Error", "Please enter Rollno");
			return;
		}
		Cursor c=db.rawQuery("SELECT * FROM student WHERE rollno='"+roll.getText()+"'", null);
		if(c.moveToFirst())
		{
			name.setText(c.getString(1));
			marks.setText(c.getString(2));
			marks1.setText(c.getString(3));
			marks2.setText(c.getString(4));

		}
		else
		{
			showMessage("Error", "Invalid Rollno");
			clearText();
		}
	}
});
result.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Cursor c=db.rawQuery("SELECT * FROM student", null);
        
		
		if(c.getCount()==0)
		{
			showMessage("Error", "No records found");
			return;
		}
		StringBuffer buffer=new StringBuffer();
		while(c.moveToNext())
		{
			buffer.append("Rollno: "+c.getString(0)+"\n");
			buffer.append("Name: "+c.getString(1)+"\n");
			buffer.append("Maths: "+c.getString(2)+"\n");
			buffer.append("English: "+c.getString(3)+"\n");
			buffer.append("Science: "+c.getString(4)+"\n");
			buffer.append("---------------------"+"\n");
			
		}
		
		

		showMessage("Student Details", buffer.toString());
		clearText();
	}
});
	}
	
	
	public void showMessage(String title,String message)
    {
    	Builder builder=new Builder(this);
    	builder.setCancelable(true);
    	builder.setTitle(title);
    	builder.setMessage(message);
    	builder.show();
	}
    public void clearText()
    {
    	roll.setText("");
    	name.setText("");
    	marks.setText("");
    	marks1.setText("");
    	marks2.setText("");
    	roll.requestFocus();
    }
 
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK && requestCode==1)
		{Bitmap bm = (Bitmap) data.getExtras().get("data");
		img.setImageBitmap(bm);
		
		
			Toast.makeText(MainActivity.this, "image captured", Toast.LENGTH_LONG).show();
			
		}
		
		else
		{
			Toast.makeText(MainActivity.this, "image not captured", Toast.LENGTH_LONG).show();
		}
			
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
