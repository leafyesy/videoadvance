package com.leafye.annotationtest;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.leafye.annotation.AViewBinder;
import com.leafye.apilib.BindView;
import com.leafye.apilib.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.hello)
    TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AViewBinder.bind(this);
//        tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }

    @OnClick(R.id.hello)
    public void helloClick(View view) {
        Toast.makeText(this, "hhh", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AViewBinder.unBind(this);
    }
}
