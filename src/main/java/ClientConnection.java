import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class ClientConnection implements Runnable {
    private static Socket socket;
    private DataInputStream in;
    private static DataOutputStream out;
    private static String nick;
    private static String login;
    private static String pass;
    private Boolean register;
    private ChatController chatController;

    public ClientConnection(String login, String pass, ChatController chatController) {
        ClientConnection.login = login;
        ClientConnection.pass = pass;
        this.chatController = chatController;
        this.register = false;
    }

    public void setRegister(Boolean register) {
        this.register = register;
    }

    private void register() {
        sendMessage("/register " + login + " " + pass);
    }

    private void login() {
        sendMessage("/auth " + login + " " + pass);
    }

    public static void logout() {
        nick = null;
        sendMessage("/end");
    }

    public static void delete() {
        nick = null;
        sendMessage("/delete");
    }

    public static void sendMessage(String message) {
        if (!message.isEmpty()) {
            try {
                out.writeUTF(message);
                out.flush();
            } catch (IOException var2) {
                var2.printStackTrace();
            }

        }
    }

    public void run() {
        try {
            socket = new Socket("localhost", 8189);
            this.in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            if (this.register) {
                this.register();
            } else {
                this.login();
            }

            while(socket.isConnected()) {
                String msg = this.in.readUTF();
                String[] users;
                if (nick != null) {
                    if (msg.startsWith("/")) {
                        if (msg.equalsIgnoreCase("/end")) {
                            this.chatController.showLogin();
                        } else if (msg.equalsIgnoreCase("/delete")) {
                            this.chatController.showLogin();
                        } else if (msg.startsWith("/user_list ")) {
                            users = msg.split(" ");
                            this.chatController.updateUserList((String[])Arrays.copyOfRange(users, 1, users.length));
                        }
                    } else if (!msg.isEmpty()) {
                        this.chatController.addMessage(msg);
                    }
                } else if (msg.startsWith("/auth_ok")) {
                    users = msg.split(" ");
                    nick = users[1];
                    this.chatController.setNickLabel(nick);
                    LoginController.getInstance().showScene();
                } else {
                    String response;
                    if (msg.startsWith("/auth_fail ")) {
                        response = msg.substring(11);
                        LoginController.getInstance().showResponse(response);
                        break;
                    }

                    if (msg.startsWith("/register_ok ")) {
                        users = msg.split(" ");
                        nick = users[1];
                        this.chatController.setNickLabel(nick);
                        LoginController.getInstance().showScene();
                    } else if (msg.startsWith("/register_fail ")) {
                        response = msg.substring(15);
                        LoginController.getInstance().showResponse(response);
                        break;
                    }
                }
            }
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        try {
            socket.close();
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }
}
