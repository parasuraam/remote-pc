package com.example.abcdefgh;

import android.annotation.SuppressLint;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@SuppressLint("Registered")
public class c   {

    public static Socket clientSocket = null;
    public static ObjectInputStream objectInputStream = null;
    public static ObjectOutputStream objectOutputStream = null;







    public static void socketException() {

        if (c.clientSocket != null) {
            try {
                c.clientSocket.close();
                c.objectOutputStream.close();
                c.clientSocket = null;
            } catch(Exception e2) {
                e2.printStackTrace();
            }
        }
    }

}