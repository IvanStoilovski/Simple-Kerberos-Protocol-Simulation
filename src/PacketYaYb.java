// This class is used to simulate a packet which is sent in the process of the server-client and client-client communication in the session-key exchange phase
public class PacketYaYb {
    byte[] ya;
    byte[] yb;

    public PacketYaYb(byte[] a, byte[] b) {
        this.ya = a;
        this.yb = b;
    }
}
