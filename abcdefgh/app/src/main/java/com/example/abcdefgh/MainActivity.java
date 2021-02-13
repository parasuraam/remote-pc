package com.example.abcdefgh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.abcdefgh.server.Server;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private Button  touch1;
    private Button connectButton;
    private EditText ipAddressEditText, portNumberEditText;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        touch1 =findViewById(R.id.touvh1);
        touch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T();
            }
        });

        ipAddressEditText = (EditText) findViewById(R.id.ipAddress);
        portNumberEditText = (EditText) findViewById(R.id.portNumber);
        connectButton = (Button) findViewById(R.id.connectButton);
        sharedPreferences = MainActivity.this.getSharedPreferences("lastConnectionDetails", Context.MODE_PRIVATE);
        String lastConnectionDetails[] = getLastConnectionDetails();
        ipAddressEditText.setText(lastConnectionDetails[0]);
        portNumberEditText.setText(lastConnectionDetails[1]);
        MainActivity.this.setTitle(getResources().getString(R.string.connect));
        if (c.clientSocket != null) {
            connectButton.setText("connected");
            connectButton.setEnabled(false);
        }
        connectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                makeConnection();
            }
        });

    }

    public void T(){
        Intent intent=new Intent(this,SecondActivity.class);
        startActivity(intent);
    }

    @SuppressLint("StaticFieldLeak")
    public void makeConnection() {
        String ipAddress = ipAddressEditText.getText().toString();
        String port = portNumberEditText.getText().toString();
        if (ValidateIP.validateIP(ipAddress) && ValidateIP.validatePort(port)) {
            setLastConnectionDetails(new String[] {ipAddress, port});
            connectButton.setText("Connecting...");
            connectButton.setEnabled(false);
            new MakeConnection(ipAddress, port, MainActivity.this) {
                @Override
                public void receiveData(Object result) {
                    c.clientSocket = (Socket) result;
                    if (c.clientSocket == null) {
                        Toast.makeText(MainActivity.this, "Server is not listening", Toast.LENGTH_SHORT).show();
                        connectButton.setText("connect");
                        connectButton.setEnabled(true);
                    } else {
                        connectButton.setText("connected");
                        new Thread(new Runnable() {
                            public void run() {
                                new Server(MainActivity.this).startServer(Integer.parseInt(port));
                            }
                        }).start();
                    }
                }
            }.execute();
        } else {
            Toast.makeText(MainActivity.this, "Invalid IP Address or port", Toast.LENGTH_SHORT).show();
        }
    }
    private String[] getLastConnectionDetails() {
        String arr[] = new String[2];
        arr[0] = sharedPreferences.getString("lastConnectedIP", "");
        arr[1] = sharedPreferences.getString("lastConnectedPort", "3000");
        return arr;
    }
    private void setLastConnectionDetails(String[] arr) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastConnectedIP", arr[0]);
        editor.putString("lastConnectedPort", arr[1]);
        editor.apply();
    }

}