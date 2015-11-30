package com.ibm.bluelist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddConnectionActivity extends Activity {

    EditText editTextRoute;
    EditText editTextKey;
    EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_connection);

        editTextRoute = (EditText) findViewById(R.id.conn_route);
        editTextKey = (EditText) findViewById(R.id.conn_key);
        editTextPassword = (EditText) findViewById(R.id.conn_password);
    }

    public void onClickAdd (View btnAdd) {

        String connRoute = editTextRoute.getText().toString();
        String connKey = editTextKey.getText().toString();
        String connPassword = editTextPassword.getText().toString();

        if ( connRoute.length() != 0 && connKey.length() != 0 ) {

            Intent newIntent = getIntent();
            newIntent.putExtra("tag_conn_route", connRoute);
            newIntent.putExtra("tag_conn_key", connKey);
            newIntent.putExtra("tag_conn_password", connPassword);

            this.setResult(RESULT_OK, newIntent);

            finish();
        }
    }

}
