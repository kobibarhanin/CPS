package ClientServerMain;

import CarPark.ParkDetails;
import CarPark.Struct;
import ocsf.ChatClient;
import ocsf.ChatIF;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Semaphore;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version July 2000
 */
public class Client implements ChatIF
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;


  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;
  public ClientHandler clientHandler;

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the Client UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public Client(String host, int port)
  {
    try
    {
      client= new ChatClient(host, port, this);
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
    clientHandler = new ClientHandler(client);
  }


  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message)
  {
    clientHandler.handle(message);
  }




  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args [0]The host to connect to.
   */
  public static void main(String[] args)
  {
    String host = "127.0.0.1";

    int port = DEFAULT_PORT;  //The port number

    try
    {
      host = args[0];
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      host = "127.0.0.1";
    }

    System.out.println("connecting to Host at IP: " + host);
    Client client= new Client(host, port);

    /* Tests from Client */

//    client.clientHandler.getUserId("k@b", "")
//    client.clientHandler.addCar(1,"k@b");
//    client.clientHandler.getCars("k@b");
//    try {
//      System.out.println("add order = " + client.clientHandler.addOrder(15, "k@b", "00", "11", 3));
//    }
//    catch (Exception e){
//      e.getStackTrace();
//    }

    try {

//    client.clientHandler.addOrder(33333, "k@b", "2018/01/12 15:00:00", "2018/01/12 17:00:00", 1);
//    client.clientHandler.getFullmemberships("k@b");
//    client.clientHandler.getRegmemberships("k@b");
//    client.clientHandler.getOrders("k@b");

//      client.clientHandler.addOrder(9876, "k@b", "2018/02/12 15:00:00", "2018/02/12 17:00:00", 1);

//      client.clientHandler.addOrder(5555, "k@b", "2018/02/04 10:00:00", "2018/02/04 23:30:00", 14,"AHEAD");
//      client.clientHandler.enterParkOrder(5555,14,22);

//      client.clientHandler.enterPark(5555,14,10);
//      client.clientHandler.leavePark(5555,14);

//        client.clientHandler.enterParkMember(1234,14,0,"2018/02/04 23:30:00");
//client.clientHandler.enterParkMember(12,14,6,"2018/02/04 23:30:00");
//      System.out.println(client.clientHandler.getUserType("k@b"));
//      System.out.println(client.clientHandler.getUserType("cpsm"));

//      client.clientHandler.cancelOrder(32);
//      client.clientHandler.cancelOrder(7);
//      System.out.println(client.clientHandler.calcCost(5));

      client.clientHandler.addComplaint("aaaa",1);

    } catch (Exception e){}

//    client.clientHandler.addRegMembership("12","k@b","14","14:00");

//    String [] kobi = {"1", "2" ,"3"};
//    System.out.println(kobi[1]);


//    client.clientHandler.addComplaint("new complaint");

//    System.out.println(client.clientHandler.getComplaints()[0]);

//    client.clientHandler.closeComplaint(2);

//    System.out.println(client.clientHandler.getCarparks());
//    System.out.println(client.clientHandler.getRegmemberships("k@b")[0]);
//client.clientHandler.enterParkMember(2,1,9,"2018/02/05 23:30:00");
//    client.clientHandler.leavePark(1234,14);
//  client.clientHandler.isaMember()

//    System.out.println(client.clientHandler.isaMember(1,2));

//    System.out.println(client.clientHandler.getPriceChangeRequests()[0]);
//    System.out.println(client.clientHandler.getPriceChangeRequests()[1]);
//    System.out.println(client.clientHandler.getPriceChangeRequests()[2]);
//    System.out.println(client.clientHandler.getFullmemberships("k")[0]);

//    System.out.println(client.clientHandler.createAdminReport(ParkDetails.type.PERFORMANCE.toString(),1));
//    System.out.println(client.clientHandler.createParkAdminReport(Struct.report.COMPLAINT.toString(),1));
//    System.out.println(client.clientHandler.retrievePastReport("2018/02/05 23:30:00"));
//    System.out.println(client.clientHandler.createAdminReport(ParkDetails.type.STATISTICS.toString(), 1));

  }


}
