package com.example.udpwearos.presentation;

import android.util.Log;
import android.util.Pair;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
    private static final int SERVER_PORT = 2390; // Cổng của server
    private static final String SERVER_IP = "192.168.1.102"; // Địa chỉ IP của server (thay đổi nếu cần)
    public DatagramSocket socket;

    public UDPClient() {
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(5000);  // Thêm thời gian timeout 5 giây
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Phương thức gửi dữ liệu UDP
    public void sendData(String data) {
        try {
            byte[] sendData = data.getBytes();
            InetAddress serverAddress1 = InetAddress.getByName(SERVER_IP);
            DatagramPacket packet = new DatagramPacket(sendData, sendData.length, serverAddress1, 2390);
            socket.send(packet);  // Gửi dữ liệu đến server
            System.out.println("Data sent: " + data);

            Log.d("ABC", "Send data: " + packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Pair<String, Long> receiveData() {
        try {
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            // Gửi yêu cầu để nhận dữ liệu (nếu cần thiết)
            String message = "Hello server!";

            InetAddress serverAddress = InetAddress.getByName(SERVER_IP);
            DatagramPacket requestPacket = new DatagramPacket(message.getBytes(), message.length(), serverAddress, SERVER_PORT);
            long startTime = System.currentTimeMillis();
            socket.send(requestPacket);
//            Log.d("Ping", "Send data: " + requestPacket);
            // Nhận dữ liệu từ server
            socket.receive(packet);
            long endTime = System.currentTimeMillis();

            long pinglocal = endTime - startTime;
            String receivedData = new String(packet.getData(), 0, packet.getLength());

            // Kiểm tra dữ liệu đã nhận
            Log.d("UDPClient1", "Received data: " + receivedData);
//            return receivedData;
            return new Pair<>(receivedData, pinglocal);  // Trả về Pair chứa dữ liệu và thời gian ping
            //Thread.sleep(100); // 100ms hoặc điều chỉnh tùy theo yêu cầu.sleep(10);
        } catch (Exception e) {
            Log.e("UDPClient1", "Error receiving data: " + e.getMessage());
        }
//        return null;
        return new Pair<>(null, -1L);  // Trả về Pair với null và -1 nếu có lỗi
    }

    public void close() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }


}
