package com.khouse.immi.chuck24hoursbible;

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    MainActivity self = this;
    String[] topic_names;
    TypedArray profile_pics;
    String[] statues;
    String[] contactType;
    List<RowItem> rowItems;
    ListView mylistview;
    SeekBar sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        rowItems = new ArrayList<RowItem>();
        topic_names = getResources().getStringArray(R.array.topics);
        profile_pics = getResources().obtainTypedArray(R.array.profile_pics);
        statues = getResources().getStringArray(R.array.statues);
        contactType = getResources().getStringArray(R.array.contactType);
        for (int i = 0; i < topic_names.length; i++) {
            String vidpath = "http://immanuel.co/chuck/video/" + i + ".avi";
            String topath = getApplicationContext().getCacheDir().toString();
            File file = new File(topath, i + ".avi");
            boolean viddwnlded = false;
            if (file.exists()){
                if (!CommonPreference.IsFileSaved(i + ".avi", this)){
                    file.delete();
                } else {
                    vidpath = file.getAbsolutePath();
                    viddwnlded = true;
                }
            }

            String audpath = "http://immanuel.co/chuck/audio/" + i + ".mp3";
            String topath1 = getApplicationContext().getCacheDir().toString();
            File file1 = new File(topath1, i + ".mp3");
            boolean auddwnlded = false;
            if (file1.exists()){
                if (!CommonPreference.IsFileSaved(i + ".mp3", this)){
                    file1.delete();
                } else {
                    audpath = file1.getAbsolutePath();
                    auddwnlded = true;
                }
            }
            RowItem item = new RowItem(topic_names[i],
                    profile_pics.getResourceId(i, -1), statues[i],
                    contactType[i],
                    vidpath,
                    audpath,
                    viddwnlded,
                    auddwnlded
            );
            rowItems.add(item);
        }
        mylistview = (ListView) findViewById(R.id.list);
        sb = (SeekBar) findViewById(R.id.sbar);
        CustomAdapter adapter = new CustomAdapter(this, rowItems, sb, self);
        mylistview.setAdapter(adapter);
        mylistview.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        String member_name = rowItems.get(position).getTopic_name();
        int member_id = rowItems.get(position).getTopic_pic_id();
//        Toast.makeText(getApplicationContext(), "" + member_name + " - " + id,
//                Toast.LENGTH_SHORT).show();

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String strErr = data.getStringExtra("vidRest");
        if (strErr == "Error"){
            Toast.makeText(this, "Error playing video", Toast.LENGTH_LONG);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    void updateAudText(final int position){
        final TextView tAud = (TextView)findViewById(R.id.txtAudPlay);
        runOnUiThread(new Runnable() {
            public void run() {
                tAud.setText(rowItems.get(position).getTopic_name());
            }
        });
    }

    void showError(final String err){
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(MainActivity.this, err, Toast.LENGTH_LONG).show();
            }
        });
    }
}
