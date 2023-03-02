import java.text.ParseException;
public class MainTest {

    public static void main(String[] args) throws ParseException {
        User Alice = new User("Alice", "1234567890123456");
        User Bob = new User("Bob", "1564830198753204");
        User Oscar = new User("Oscar", "1564830568753204");
        User Trudy = new User("Trudy", "6123450198648305");
        KDCServer server = new KDCServer();
        Implementation implementation = new Implementation(server);
        implementation.SetUserInServer(Alice);
        implementation.SetUserInServer(Bob);
        implementation.SetUserInServer(Oscar);
        implementation.SetUserInServer(Trudy);
        implementation.StartCommunication(Alice, Bob);
    }
}
