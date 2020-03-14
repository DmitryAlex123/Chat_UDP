import javax.swing.*;
import java.awt.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.regex.Pattern;

public class ChatUDP extends JFrame{

    private JTextArea textArea;
    private JTextField textField;
    private JButton button;

    private final String FRM_TITLE = "Our Tiny Chat";
    private final int FRM_LOC_X = 100;
    private final int FRM_LOC_Y = 100;
    private final int FRM_WIDTH = 400;
    private final int FRM_HEIGHT = 400;
    private final int PORT = 9876;
    private final String IP_BROADCAST = "192.168.0.255";

    //прием
    private class Reciver extends Thread{
        @Override
        public void start(){
            try {
                DatagramSocket takeSocket = new DatagramSocket(PORT);
                while (true){

                    byte[] data = new byte[1024];
                    DatagramPacket recivePacket = new DatagramPacket(data, data.length);
                    takeSocket.receive(recivePacket);
                    textArea.append(recivePacket.getAddress() + ":" + recivePacket.getPort() + ": " + new String(recivePacket.getData()) + "\r\n");
                }
            }catch (Exception ex){
                System.out.println(ex);
            }
        }
    }


    private void frameDraw(JFrame frame){
        textArea = new JTextArea(FRM_HEIGHT/19, 50);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setLocation(0,0);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textField = new JTextField();
        button = new JButton();
        button.setText("Send");

        button.addActionListener(e -> {
            try{
                btnSendHandler();
            }catch (Exception ex){
                System.out.println(ex);
            }
        });

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle(FRM_TITLE);
        frame.setLocation(FRM_LOC_X, FRM_LOC_Y);
        frame.setSize(FRM_WIDTH, FRM_HEIGHT);
        frame.setResizable(false);
        frame.add(BorderLayout.NORTH, scrollPane);
        frame.add(BorderLayout.CENTER, textField);
        frame.add(BorderLayout.EAST, button);
        frame.setVisible(true);
    }

    //отправка
    private void btnSendHandler() throws Exception{
        DatagramSocket sendSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName(IP_BROADCAST);
        byte[] sendData;
        String sentence = textField.getText();
        textField.setText("");
        sendData = sentence.getBytes("UTF-8");
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, PORT);
        sendSocket.send(sendPacket);
    }


    private void antistatic(){
        frameDraw(new ChatUDP());
        new Reciver().start();
    }

    public static void main(String[] args) {
        new ChatUDP().antistatic();

    }
}
