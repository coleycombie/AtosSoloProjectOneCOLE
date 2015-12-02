package CarParkExitBarrierSystem;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class DriveUpParkingTransaction
{
    private String cardNo = "";

    String reg;

    static int expiryMonth, expiryYear;

    public void confirmSelection(Scanner sc, boolean createTicket)
    {
        if (createTicket == true)
        {
            System.out.println("You have selected Drive Up Ticket, is this correct?");
            String opt = sc.next();
            if ("yes".equalsIgnoreCase(opt))
            {
                System.out.println("Successfully Selected Yes");
                DriveUpParkingTransaction TRANS = new DriveUpParkingTransaction();
                TRANS.getTicket();
            }
            else if ("no".equalsIgnoreCase(opt))
            {
                System.out.println("Successfully selected No");
                ParkingTransaction m = new ParkingTransaction();
                m.getOpt();
            }
            else
            {
                System.out.println("Invalid selection.");
                ParkingTransaction m = new ParkingTransaction();
                m.getOpt();

            }
        }
        else
        {
            System.out.println("You have selected Pay For Ticket, is this correct?");
            String opt = sc.next();
            if ("yes".equalsIgnoreCase(opt))
            {
                System.out.println("Successfully Selected Yes");
                DriveUpParkingTransaction TRANS = new DriveUpParkingTransaction();
                TRANS.payForTicket("D");
            }
            else if ("no".equalsIgnoreCase(opt))
            {
                System.out.println("Successfully selected No");
                ParkingTransaction m = new ParkingTransaction();
                m.getOpt();
            }
            else
            {
                System.out.println("Invalid selection.");
                ParkingTransaction m = new ParkingTransaction();
                m.getOpt();

            }
        }
    }

    public void getTicket()
    {
        Scanner sc = new Scanner(System.in);
        Calendar c = new GregorianCalendar();
        int hour = c.get(Calendar.HOUR_OF_DAY), minute = c.get(Calendar.MINUTE);
        System.out.println("Enter Registration Number: ");
        reg = sc.nextLine();
        ParkingTicket DUT = new ParkingTicket(reg, hour, minute);
        // DUT = drive up ticket
        System.out.println(DUT.toString());
        TicketReader read = new TicketReader();
        FileWriter write = new FileWriter();

        read.readFile(reg, false);
        write.writeToFile(reg, hour, minute);
    }

    public void payForTicket(String transType)
    {
        CardDetailChecker checker = new CardDetailChecker();
        CentralAuthWriter caw = new CentralAuthWriter();

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Registration Number: ");
        reg = sc.nextLine();
        TicketReader PFT = new TicketReader();
        PFT.readFile(reg, true);
        boolean cardAccepted = false;
        do
        {

            boolean cardDateAccepted = false;
            System.out.println("Please input Card Number: ");
            cardNo = sc.nextLine();
            if (checker.checkCardDigits(cardNo))
            {
                System.out.println("Card Number accepted! ");
                do
                {
                    if (checker.checkCardExpiry())
                    {
                        cardDateAccepted = true;
                        cardAccepted = true;
                    }
                    else
                    {
                        if (cardDateAccepted == false)
                        {
                            caw.writeToCentralAuth(false, "card expired", cardNo, transType, expiryMonth, expiryYear);
                        }
                        else
                            caw.writeToCentralAuth(false, "invalid card number", cardNo, transType, expiryMonth,
                                    expiryYear);

                        cardDateAccepted = true;
                    }

                }
                while (cardDateAccepted == false);

            }
            else
                System.out.println("Please try again.");
        }
        while (cardAccepted == false);
        System.out.println("Card has been accepted and processed! Payment Complete!\n");
        caw.writeToCentralAuth(true, "n/a", transType, cardNo, expiryMonth, expiryYear);

        ParkingTransaction m = new ParkingTransaction();
        m.getOpt();

    }

}
