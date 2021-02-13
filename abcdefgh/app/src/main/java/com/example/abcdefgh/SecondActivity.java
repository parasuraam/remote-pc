package com.example.abcdefgh;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import java.io.IOException;
import java.io.ObjectInputStream;

public class SecondActivity extends AppCompatActivity {

    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        info = findViewById(R.id.terv);

        new Thread(new Te()).start();
    }

    private class Te implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    if (c.clientSocket != null) {
                        if (c.objectInputStream == null) {
                            c.objectInputStream = new ObjectInputStream(
                                    c.clientSocket.getInputStream());
                        }
                        final String message = (String) c.objectInputStream.readObject();
                        if (message != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    info.append(message);
                                }
                            });
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
