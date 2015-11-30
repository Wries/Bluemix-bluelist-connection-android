package com.ibm.bluelist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class ConnectActivity extends Activity {


    private CustomCursorAdapter customAdapter;
    private ConnectionDBHelper databaseHelper;
    private static final int ENTER_DATA_REQUEST_CODE = 1;
    private ListView listView;
    private ConnectActivity myInstance;

    private static final String TAG = ConnectActivity.class.getSimpleName();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        myInstance = this;

        databaseHelper = new ConnectionDBHelper(this);

        listView = (ListView) findViewById(R.id.list_data);
        registerForContextMenu(listView);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "clicked on item: " + position);
                Cursor cursor = (Cursor) parent.getAdapter().getItem(position);
                String route = cursor.getString(1);
                Log.d(TAG, "Route is " + route);
                String key = cursor.getString(2);
                Log.d(TAG, "key is " + key);
                String password = cursor.getString(3);
                Log.d(TAG, "password is " + password);

                BlueListApplication.getAppInstance().connectToServer(key,password,route);


                Intent intent = new Intent(myInstance, MainActivity.class);

                startActivity(intent);

            }
        });

        // Database query can be a time consuming task ..
        // so its safe to call database query in another thread
        // Handler, will handle this stuff for you <img src="http://s0.wp.com/wp-includes/images/smilies/icon_smile.gif?m=1129645325g" alt=":)" class="wp-smiley">

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                customAdapter = new CustomCursorAdapter(ConnectActivity.this, databaseHelper.getAllData());
                listView.setAdapter(customAdapter);
            }
        });
    }

    public void onClickEnterData(View btnAdd) {

        startActivityForResult(new Intent(this, AddConnectionActivity.class), ENTER_DATA_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ENTER_DATA_REQUEST_CODE && resultCode == RESULT_OK) {

            databaseHelper.insertData(data.getExtras().getString("tag_conn_route"), data.getExtras().getString("tag_conn_key"), data.getExtras().getString("tag_conn_password"));

            customAdapter.changeCursor(databaseHelper.getAllData());
        }
    }

    /**
     * MENU
     */

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.list_data) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_list, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.edit:
                // edit stuff here
                Log.d(TAG, "EDIT");
                return true;
            case R.id.delete:
                // remove stuff here
                Log.d(TAG, "DELETE");
                databaseHelper.deleteItemById(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}
