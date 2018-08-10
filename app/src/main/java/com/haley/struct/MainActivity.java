package com.haley.struct;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void startFragComm(View view) {
        startActivity(new Intent(this, FragmentActivity.class));
    }

    public void startOrmDb(View view) {
        startActivity(new Intent(this, OrmDbActivity.class));
    }

}
