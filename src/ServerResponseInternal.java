// This class simulates a packet which the server uses for internal communication purposes within the server class's methods
public class ServerResponseInternal {
    User a;
    User b;
    String nonce;

    public ServerResponseInternal(User a, User b, String nonce) {
        this.a = a;
        this.b = b;
        this.nonce = nonce;
    }
}
