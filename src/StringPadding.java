import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

// This class is used to divide the message in 128-bit blocks, and if the last block isn't 128 bits, it is padded to exactly 128 bits.

public class StringPadding {
    private String message;

    public StringPadding(String s) {
        this.message = s;
    }

    // Divides the incoming message to 128-bit strings and puts them in a list
    public List<String> ListDivide() {
        String s = message;
        List<String> list = new ArrayList<>();
        int msgLen = s.getBytes(StandardCharsets.UTF_8).length;
        int counter = msgLen / 16;
        for (int i = 0; i < counter; ++i) {
            if (i == 0)
                list.add(s.substring(i, 16));
            else {
                list.add(s.substring(i * 16, (i + 1) * 16));
            }
        }
        return list;
    }

    // This method pads the remaining last block to exactly 128 bits if needed
    public String padd() {
        String s = message;
        String end;
        int numOfBlocks = s.getBytes(StandardCharsets.UTF_8).length / 16;
        if (s.getBytes(StandardCharsets.UTF_8).length % 16 == 0) {
            end = s;
        } else {
            String temp;
            end = s.substring(0, numOfBlocks * 16);
            temp = s.substring(numOfBlocks * 16);
            int i = temp.length();
            for (int j = i; j < 16; ++j) {
                temp = temp + "+";
            }
            end = end + temp;
        }
        this.message = end;
        return message;
    }

    public void printStats() {
        System.out.println("Length of the message in bytes: " + message.getBytes(StandardCharsets.UTF_8).length);
        System.out.println("Number of 128-bit blocks in the message : " + message.getBytes(StandardCharsets.UTF_8).length / 16);
    }

    public void printDivided() {
        List<String> printing;
        printing = ListDivide();
        StringBuilder sb = new StringBuilder();
        sb.append("The message contains: ").append(message.getBytes(StandardCharsets.UTF_8).length / 16)
                .append(" 128-bit blocks and they are as follows: ").append('\n');
        for (String s : printing)
            sb.append(s).append('\n');
        System.out.println(sb);
    }

    public void printDetailed() {
        System.out.println("Printing stats before padding: ");
        printStats();
        System.out.println("Padding: ");
        padd();
        System.out.println("Printing stats after padding: ");
        printStats();
        System.out.println("Printing the blocks divided: ");
        printDivided();
    }

    public String convertStringToBinary() {
        String input = this.message;
        StringBuilder result = new StringBuilder();
        char[] chars = input.toCharArray();
        for (char aChar : chars) {
            result.append(
                    String.format("%8s", Integer.toBinaryString(aChar))   // char -> int, auto-cast
                            .replaceAll(" ", "0")                         // zero pads
            );
        }
        return result.toString();

    }
}
