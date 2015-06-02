package com.example.connecttest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class NXTComunicacion {

    public static final int DESCONECTADO = 0;
	public static final int CONECTANDO = 1;
    public static final int CONEXION_OK = 2;
    
    private int estado;
    private BluetoothAdapter ba;
  
    private ConnectThread conectarThread;
    private ConnectedThread conexionThread;
    
    public NXTComunicacion() {
        ba = BluetoothAdapter.getDefaultAdapter();
        setState(DESCONECTADO);
    }

    private synchronized void setState(int state) {
        estado = state;
    }
    
    public synchronized int getState() {
        return estado;
    }

    public synchronized void connect(BluetoothDevice device) {        
        if (estado == CONECTANDO) {
            if (conectarThread != null) {
                conectarThread.cancel();
                conectarThread = null;
            }
        }
        
        if (conexionThread != null) {
            conexionThread.cancel();
            conexionThread = null;
        }
        
        conectarThread = new ConnectThread(device);
        conectarThread.start();
        setState(CONECTANDO);
    }
    
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        if (conectarThread != null) {
            conectarThread.cancel();
            conectarThread = null;
        }
        
        if (conexionThread != null) {
            conexionThread.cancel();
            conexionThread = null;
        }
        
        conexionThread = new ConnectedThread(socket);
        conexionThread.start();
        
        setState(CONEXION_OK);
    }
    
    public synchronized void stop() {
        if (conectarThread != null) {
            conectarThread.cancel();
            conectarThread = null;
        }
        
        if (conexionThread != null) {
            conexionThread.cancel();
            conexionThread = null;
        }
        setState(DESCONECTADO);
    }
    
    private void connectionFailed() {
        setState(DESCONECTADO);
    }
    
    private void connectionLost() {
        setState(DESCONECTADO);
    }
    
    public void motors(byte l, byte r, boolean speedReg, boolean motorSync) {
        byte[] data = { 0x0c, 0x00, (byte) 0x80, 0x04, 0x02, 0x32, 0x07, 0x00, 0x00, 0x20, 0x00, 0x00, 0x00, 0x00,
                        0x0c, 0x00, (byte) 0x80, 0x04, 0x01, 0x32, 0x07, 0x00, 0x00, 0x20, 0x00, 0x00, 0x00, 0x00 };
        
        Log.i("NXT", "motors: " + Byte.toString(l) + ", " + Byte.toString(r));
        
        data[5] = l;
        data[19] = r;
        write(data);
    }
    
    private void write(byte[] out) {
        ConnectedThread r;
        synchronized (this) {
            if (estado != CONEXION_OK) {
                return;
            }
            r = conexionThread;
        }
        r.write(out);
    }
    
    private class ConnectThread extends Thread {
        private BluetoothSocket socket;
        private final BluetoothDevice dispositivo;
        
        public ConnectThread(BluetoothDevice device) {
            dispositivo = device;
        }
        
        public void run() {
            setName("ConnectThread");
            ba.cancelDiscovery();
            
            try {
                socket = dispositivo.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                socket.connect();
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    Method method = dispositivo.getClass().getMethod("createRfcommSocket", new Class[] { int.class });
                    socket = (BluetoothSocket) method.invoke(dispositivo, Integer.valueOf(1));
                    socket.connect();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    connectionFailed();
                    try {
                        socket.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    return;
                }
            }
            
            synchronized (NXTComunicacion.this) {
                conectarThread = null;
            }
            
            connected(socket, dispositivo);
        }
        
        public void cancel() {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private class ConnectedThread extends Thread {
        private final BluetoothSocket socket;
        private final InputStream inStream;
        private final OutputStream outStream;
        
        public ConnectedThread(BluetoothSocket socket) {
            this.socket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            inStream = tmpIn;
            outStream = tmpOut;
        }
        
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            
            while (true) {
                try {
                    bytes = inStream.read(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                    connectionLost();
                    break;
                }
            }
        }
        
        public void write(byte[] buffer) {
            try {
                outStream.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
