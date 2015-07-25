package ee.ut.cs.d2d.framework;




import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class D2D extends Activity {
	
	private TextView outputScreen;
	private ImageButton bluetooth;
	private ImageButton clean;
	private ImageButton log;
	
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d2d_activity_main);
        
        outputScreen = (TextView)findViewById(R.id.outputTextViewer);
        
        bluetooth = (ImageButton) findViewById(R.id.bluetoothButton);
        bluetooth.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Toast.makeText(getBaseContext(), "Turning bluetooth..", Toast.LENGTH_SHORT).show();
			
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
        
        
        
        clean = (ImageButton) findViewById(R.id.cleanButton);
        clean.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Toast.makeText(getBaseContext(), "Cleaning screen..", Toast.LENGTH_SHORT).show();
					clearOutputScreen();
			
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
        
        log = (ImageButton) findViewById(R.id.logButton);
        log.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Toast.makeText(getBaseContext(), "Showing logs..", Toast.LENGTH_SHORT).show();
					clearOutputScreen();
					showLogFiles();
			
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
        
        
    }
    
    public void printLnInScreen(String line) {
    	outputScreen.append(line + "\n");
	}
    
    public void clearOutputScreen() {
		outputScreen.setText("");
	}
    
    public void showLogFiles(){
    	
    }
    


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.d2_d, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_about) {
        	AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        	dlgAlert.setMessage("This framework is part of the context-aware hybrid computational offloading project.");
        	dlgAlert.setTitle("Device-to-Device Offloading");
        	dlgAlert.setPositiveButton("OK", null);
        	dlgAlert.setCancelable(true);
        	dlgAlert.create().show();
        	
        	
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
