import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class KDCServer {
    static AES aes = new AES();
    StringPadding paddingReq;
    private Map<User, String> users;

    public KDCServer() {
        this.users = new HashMap<>();
    }

    public void setUser(User u) {
        users.putIfAbsent(u, u.getKEK_U());
    }

    // This method generates a random 128-bit session key
    public static String KsesGenerator() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= 15; i++) {
            stringBuilder.append(secureRandom.nextInt(10));
        }
        return stringBuilder.toString();
    }

    // This method is used when the server receives a request from a client, authenticates the client "A" that initiates the communication and the client "B" with which
    // the client "A" wants to communicate, authenticates the nonce number sent by the client "A". If everything is authenticated successfully,
    // the server generates a response by calling the receiveRespS() method.
    public ServerResponseInternal receiveReqC(PacketReq p) {
        ServerResponseInternal s = null;
        if (users.containsKey(p.userRequests)) {
            if (users.containsKey(p.userCommunicatedWith))
                s = new ServerResponseInternal(p.userRequests, p.userCommunicatedWith, p.Nonce);
            else {
                System.out.println("User "+p.userCommunicatedWith.getId()+ " cannot be found!");
                System.exit(1);
            }
        } else {
            System.out.println("User " + p.userRequests.getId()+" cannot be authenticated by the server!");
            System.exit(1);
        }
        return s;
    }

    // This method generates a session key and a lifetime value and stores them in packets which are encrypted and sent to the corresponding clients.
    public PacketYaYb generateResp(ServerResponseInternal sri) {
        String nonce = sri.nonce;
        User a = sri.a;
        User b = sri.b;
        String Kses = KsesGenerator();
        String LifetimeSec = "300";
        paddingReq = new StringPadding(Kses + " " + nonce + " " + LifetimeSec + " " + b.getId());
        byte[] encUserReq = aes.encrypt(paddingReq.padd().getBytes(StandardCharsets.UTF_8), users.get(a));
        paddingReq = new StringPadding(Kses + " " + a.getId() + " " + LifetimeSec);
        byte[] encUserOth = aes.encrypt(paddingReq.padd().getBytes(StandardCharsets.UTF_8), users.get(b));
        System.out.println("User " + a.getId() + " requests a session key from the KDC server...");
        return new PacketYaYb(encUserReq, encUserOth);

    }

}
