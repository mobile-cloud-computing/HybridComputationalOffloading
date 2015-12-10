package fi.cs.ubicomp.detector;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;

import fi.cs.ubicomp.detector.database.DatabaseCommons;
import fi.cs.ubicomp.detector.database.DatabaseHandler;

public class SurrogateSensor extends AppCompatActivity {

    private DatabaseHandler dataEvent;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surrogate_sensor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        context = this;


        dataEvent = DatabaseHandler.getInstance();
        dataEvent.setContext(context);


        dataEvent.getInstance().getDatabaseManager().saveData(System.currentTimeMillis(),
                "surrogate id",
                "127.0.0.1",
                0,
                0,
                1,
                600);

        dataEvent.getInstance().getDatabaseManager().saveData(System.currentTimeMillis(),
                "surrogate id",
                "10.0.0.1",
                0,
                0,
                1,
                700);


        dataEvent.getInstance().getDatabaseManager().saveData(System.currentTimeMillis(),
                "surrogate id",
                "192.0.0.1",
                0,
                1,
                0,
                0);


        extractDatabaseFile(new DatabaseCommons());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_surrogate_sensor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Extract database
    public void extractDatabaseFile(DatabaseCommons db){
        try {
            db.copyDatabaseFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
