import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class User {
    private String KEK_U;
    private String id;
    private String otherU;
    private String SessionKey;
    StringPadding padding;
    private String nonceServer;
    static AES aes;

    public String getSessionKey() {
        return SessionKey;
    }

    public void setSessionKey(String sessionKey) {
        SessionKey = sessionKey;
    }

    public void setOtherU(String otherU) {
        this.otherU = otherU;
    }

    public String getId() {
        return id;
    }

    public String getKEK_U() {
        return this.KEK_U;
    }

    public void setNonceServer(String nonceServer) {
        this.nonceServer = nonceServer;
    }

    public User(String username, String kek) {
        this.KEK_U = kek;
        this.id = username;
    }

    // This method generates a nonce number
    public static String nonceGenerator() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= 15; i++) {
            stringBuilder.append(secureRandom.nextInt(10));
        }
        return stringBuilder.toString();
    }


    // This method simulates a call from the client to the server when initiating communication another user
    public PacketReq sendReqS(User other) {
        String nonce = nonceGenerator();
        setNonceServer(nonce);
        setOtherU(other.id);
        PacketReq reqP = new PacketReq(this, other, nonce);
        System.out.println("User " + this.getId() + " has generated nonce: " + nonce + " and is attempting to communicate with user " + other.getId() + "...");
        return reqP;
    }
   // This method is used when the initiating client receives a response from the server,
   // decrypts the response, authenticates it, receives a session key from the server
   // and attempts to communicate with the other user
    public PacketYaYb receiveRespS(PacketYaYb p) {
        PacketYaYb packetYaYb;
        byte[] UserOwn = p.ya;
        byte[] UserOth = p.yb;
        String Decrypted = new String(aes.decrypt(UserOwn, KEK_U));
        String[] decArr = Decrypted.split("\\s+");
        String Kses = decArr[0];
        this.setSessionKey(Kses);
        String nonceRecieved = decArr[1];
        String lifetime = decArr[2];
        String nameOth = decArr[3];
        nameOth = nameOth.replace("+", "");
        if (!lifetime.equals("0") && nonceRecieved.equals(nonceServer) && nameOth.equals(otherU)) {
            Date time = new Date();
            String timestamp = (new Timestamp(time.getTime()).toString());
            padding = new StringPadding(this.getId() + "," + timestamp);
            byte[] Yab = aes.encrypt(padding.padd().getBytes(StandardCharsets.UTF_8), Kses);
            packetYaYb = new PacketYaYb(Yab, UserOth);
            System.out.println("User " + this.getId() + " has successfully received a key from the KDC server...");
            System.out.println("User " + this.getId() + " is initiating a session key exchange with user " + nameOth + "...");
            return packetYaYb;
        } else {
            System.out.println("Server response cannot be verified!");
            return null;
        }

    }

    // This method is used when the user receives an initiation to communicate from another user to authenticate the server and the user
    public void receiveInitiation(PacketYaYb packet) throws ParseException {
        byte[] decrypted = aes.decrypt(packet.yb, KEK_U);
        String receivedYb = new String(decrypted);
        String[] parts = receivedYb.split("\\s+");
        String initiatorYb = parts[1];
        String Kses = parts[0];
        String lifetime = parts[2];
        lifetime = lifetime.replace("+", "");
        byte[] fromSender = aes.decrypt(packet.ya, Kses);
        String recivedYab = new String(fromSender);
        String[] parts2 = recivedYab.split(",");
        String initiatorYab = parts2[0];
        String timestampRecieved = parts2[1];
        timestampRecieved = timestampRecieved.replace("+", "");
        if (!lifetime.equals("0") && initiatorYab.equals(initiatorYb)) {
            Date time = new Date();
            Timestamp timestampLater = new Timestamp(time.getTime());
            int time2Micro = timestampLater.getNanos() / 1000;
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date dat2 = dateFormat.parse(timestampRecieved);
            Timestamp timestampEarlier = new Timestamp(dat2.getTime());
            int time1Micro = timestampEarlier.getNanos() / 1000;
            int lifetimeMicro = (Integer.parseInt(lifetime)) * 1000000;
            if ((time2Micro - time1Micro) <= lifetimeMicro) {
                this.setSessionKey(Kses);
                System.out.println("Both clients have exchanged the session key successfully!");
            } else {
                System.out.println("The session has expired!");
            }
        } else {
            System.out.println("Communication initiation could not be verified!");
        }

    }

    public byte[] SendMessage(String message) {
        padding = new StringPadding(message);
        return aes.encrypt(padding.padd().getBytes(StandardCharsets.UTF_8), this.getSessionKey());
    }

    public void receiveMessage(byte[] encrypted) {
        System.out.println("The message " + this.getId() + " sees before decrypting it: ");
        StringBuilder encText = new StringBuilder();
        for (byte b : encrypted)
            encText.append(String.format("%02X ", b));
        System.out.println(encText);
        System.out.println("The message " + this.getId() + " sees after decrypting it and removing padding, if necessary: ");
        byte[] decrypted = aes.decrypt(encrypted, this.getSessionKey());
        String plaintext = new String(decrypted);
        plaintext = plaintext.replace("+", "");
        System.out.println(plaintext);
    }
}
