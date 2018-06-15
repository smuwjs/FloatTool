package me.jeeson.android.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;

import me.jeeson.android.floattool.FloatManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView iv = new ImageView(getBaseContext());
        iv.setImageResource(R.mipmap.ic_launcher);

        FloatManager.getInstance().initialize(iv);

        final SwitchCompat control = findViewById(R.id.control);
        control.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!FloatManager.getInstance().showMenu()) {
                        control.setChecked(false);
                    }
                } else {
                    FloatManager.getInstance().dismissMenu();
                }
            }
        });
        control.setChecked(true);

        final Button button = findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });
    }
}
