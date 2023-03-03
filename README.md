# Simple-Kerberos-Protocol-Simulation
### This is a simple simulation of the Kerberos protocol. This protocol is a computer-network authentication protocol that works on the basis of tickets to allow nodes communicating over a non-secure network to prove their identity to one another in a secure manner. It provides mutual authentication—both the user and the server verify each other's identity. Kerberos builds on symmetric-key cryptography and requires a trusted third party, and optionally may use public-key cryptography during certain phases of authentication.

 ***For more information about this protocol, click [here](https://en.wikipedia.org/wiki/Kerberos_(protocol)).***
 
 ***A diagram of the protocol is shown [here](https://upload.wikimedia.org/wikipedia/commons/thumb/6/68/Kerberos_protocol.svg/645px-Kerberos_protocol.svg.png).***
 
### For simplicity in the documentation below, the user that initiates communication will be referred to as ***User A***, while the user that receives the initiation for communication will be referred to as ***User B***.


This project is a simple simulation of the aforementioned protocol. This simulation is achieved with **9 classes:** 

**1. User, which consists of 13 methods:**
  - **6 Setters/Getters.**
  - **A constructor with parameters.**
  - **String nonceGenerator**
    - A method that generates a nonce number (128-bit random string value).
   - **PacketReq**
      - A method that simulates a call from the client (***User A***) to the server when initiating communication another user (***User B***).
   - **packetYaYb receiveRespS**
      - This method is used when the initiating client (***User A***) receives a response from the server, decrypts the response, authenticates it, receives a session key from the server and attempts to communicate with ***User B***.
   - **void receiveInitiation**
      - This method is used when ***User B*** receives an initiation to communicate from ***User A*** in order to authenticate the server and ***User A*** and to make sure that the session and session key haven't timed-out.
   - **byte [ ] SendMessage**
      - A method used to encrypt a message with a session key.
   - **void receiveMessage**
      - A method that visualises the process of receiving an encrypted message (what the encrypted message looks like), decrypting a message and reading it as plain text (prints the decrypted message). 

**2. KDCServer**
  - This class simulates a simple KDCServer (Key Distribution Center). It acts as an authoritative body in the Kerberos protocol. This server keeps a record of all of the authenticated users in the network (in this simulation the users are kept in a hash map in the server). It is assumed that every added user is a trusted user in the network and the KEK values of the users are given in advance. When ***User A*** wants to communicate with ***User B***, ***User A*** asks the server for a session key. The KDCServer checks if ***User A*** and ***User B*** are authenticated in the network as trusted users and if so, generates a session key and a lifetime for the sesion and sends the session key, the lifetime value and some additional information back to ***User A*** and ***User B***. To achieve this, **5 methods** are used:
      - **A constructor.**
      - **void setUser** 
        - A setter method used to add users in the server's record of trusted (authenticated) users.
      - **String KsesGenerator**
        - This method generates a random 128-bit session key
      - **ServerResponseInternal receiveReqC** 
        - This method is used when the server receives a request from a client (***User A***), authenticates both ***User A*** and ***User B*** and authenticates the nonce number sent by ***User A***. If everything is authenticated successfully, the server generates a response by calling the receiveRespS() method.
      - **PacketYaYb generateResp**
        - This method generates a session key and a lifetime value and stores them in packets (**Ya** and **Yb**) which are encrypted and sent to the corresponding clients.
        
        
**3. Implementation**
  - This class is used to simulate the whole process of: ***User A*** sends a request to the KDCServer for a session key, the server authenticates ***User A*** and ***User B***, the server generates a session key and sends it to ***User A*** and ***User B*** and in the end ***User A*** and ***User B*** communicate with eachother, encrypting the messages with the session key. To achieve this, **5 methods** are used:
      - **A constructor.**
      - **SetUserInServer** 
        - A setter as a way to set trusted users in the server's records of trusted users.
      - **void StartCommunication**
        - A method that starts the process of session key exchange and communication.
      - **ExchangeSessionKey**
        - A method that simulates the process of the session key exchange by calling needed methods.
      - **Communicate**
        - A method that simulates the communication after a successful session key exchange.
        
**4. AES**
  - This class uses the AES encryption and decryption algorithms provided in the javax.crypto.Cipher library
  
**5. StringPadding, which consists of 6 methods:**
  - **List<String> listDivide** 
    - A method that splits a single string into 128-bit sized blocks of strings and puts them into a list.
  - **String padd**
    - A method that takes the last remaining block of the original string, and pads it to exactly 128 bits if needed.
  - **printStats**
    - A method that prints detailed information about the message such as: the length of the message, number of 128-bit blocks in the message
  - **printDivided**
    - A method that prints the message divided in individual 128-bit blocks.
  - **printDetailed**
    - A method that prints the stats before and after padding the message, if necessary.
  - **convertStringToBinary**
    - A method that converts a string message to binary.
  
**6. PacketReq**
  - This class is used to simulate a packet (request) which is sent from the client to the server when the client (***User A***) requests a session key from the server. It contains information about ***User A***, ***User B*** and a nonce value generated by ***User A***.
  
**7. PacketYaYb**
  - This class is used to simulate a packet that is sent in the process of the server-client and client-client communication in the session-key exchange phase. It contains two byte arrays: **Ya** and **Yb**. **Ya** is the information the server sends to ***User A*** containing: The session key that the server has generated, the nonce value that was sent to the server by ***User A*** , a lifetime value, and the identity of ***User B***, encrypted with the **KEK** value of ***User A***. **Yb** is the information the server sends to ***User B*** containing: The same session key sent to ***User A***, a lifetime value, and the identity of ***User A***, encrypted with the **KEK** value of ***User B***.
  
**8. ServerResponseInternal**
  - This class simulates a packet which the server uses for internal communication purposes within the server class's methods. It contains information about ***User A***, ***User B*** and a nonce value generated by ***User A***.
  
**9. MainTest**
  - The main class used to run the program. Here we manually add users (with their kek numbers), create an instance of the protocol simulation class and run it.
  
