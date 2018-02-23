package com.example.android.booklistinggoogle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        // On button click goes back to Main Activity
        Button startAgain = (Button) findViewById(R.id.search_button);
        startAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Goes to Main Activity
                Intent intent = new Intent(EmptyActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
