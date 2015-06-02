package com.example.connecttest;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketManager {

    private ServerSocket server;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private static SocketManager sm = new SocketManager();
    private Context context;
    private boolean sesionAbierta;
    
    String error;
    
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            int op = msg.what;
            switch (op) {
                case 0:
                    Toast.makeText(context, "CONEXION ABIERTA", Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(context, "ERROR AL ABRIR CONEXION" + error, Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(context, "ESPERANDO CONEXION", Toast.LENGTH_LONG).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void setContext(Context context) {
        this.context = context;
    }

    public void openConnection(final int puerto) {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                	handler.sendEmptyMessage(2);
                    server = new ServerSocket(puerto);
                    socket = server.accept();
                    writer = new PrintWriter(socket.getOutputStream());
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    sesionAbierta = true;
                    handler.sendEmptyMessage(0);
                } catch (IOException e) {
                    error = e.getLocalizedMessage();
                    handler.sendEmptyMessage(1);
                }
            }
        };
        t.start();
    }

    public void closeConnection() {
        try {
            sesionAbierta = false;
            writer.close();
            reader.close();
            socket.close();
            server.close();
        } catch (IOException e) {
            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public boolean getSesionAbierta() {
        return sesionAbierta;
    }

    public static SocketManager getInstance() {
        return sm;
    }

    public void sendLine(String line) {
        writer.println(line);
        writer.flush();
    }

    public String readLine() {
        String line = "";
        try {
            line = reader.readLine();
        } catch (IOException e) {
            line = e.getLocalizedMessage();
        }
        return line;
    }

}

