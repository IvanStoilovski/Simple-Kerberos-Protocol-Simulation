import java.text.ParseException;

class Implementation {

    static KDCServer server;

    public Implementation(KDCServer server) {
        this.server = server;
    }

    public void SetUserInServer(User u) {
        server.setUser(u);
    }

    public void StartCommunication(User a, User b) throws ParseException {
        ExchangeSessionKey(a,b);
    }
    public void ExchangeSessionKey(User a, User b) throws ParseException {
        b.receiveInitiation(a.receiveRespS(server.generateResp(server.receiveReqC(a.sendReqS(b)))));
        System.out.println(a.getId() + "'s session key: " + a.getSessionKey());
        System.out.println(b.getId() + "'s session key: " + b.getSessionKey());
        Communicate(a, b);

    }
    public void Communicate(User a, User b) {
        System.out.println("============================ Communication ============================");
        b.receiveMessage(a.SendMessage("Hi Bob!"));
        System.out.println(b.getId() + " Replies... ");
        a.receiveMessage(b.SendMessage("Hi Alice!"));
    }
}