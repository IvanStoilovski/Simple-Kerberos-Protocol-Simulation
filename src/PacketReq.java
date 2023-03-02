// This class is used to simulate a packet which is sent from the client to the server when the client requests a session key from the server
public class PacketReq {
    User userRequests;
    User userCommunicatedWith;
    String Nonce;

    public PacketReq(User u1, User u2, String nonce) {
        this.userRequests = u1;
        this.userCommunicatedWith = u2;
        this.Nonce = nonce;
    }
}
