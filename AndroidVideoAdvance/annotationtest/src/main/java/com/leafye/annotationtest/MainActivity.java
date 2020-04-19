package com.leafye.annotationtest;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.leafye.annotation.AViewBinder;
import com.leafye.apilib.BindView;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.hello)
    TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AViewBinder.bind(this);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AViewBinder.unBind(this);
    }
}
