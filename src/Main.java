import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class Main {
    static int[][] busSeats;

    public static void main(String[] args) {
        Scanner inputScanner = new Scanner(System.in);

        System.out.println("---------- Setting up Bus ----------");
        setupBus(inputScanner);


        int userChoice;
        do {
            System.out.println("---------- Bus Management System ----------");
            displayMenu();
            System.out.println("---------------------------------------------");


            System.out.print("-> Choose option (1-6): ");
            userChoice = inputScanner.nextInt();


            switch (userChoice) {
                case 1:
                    showAllBusInformation(inputScanner);
                    break;
                case 2:
                    bookBusSeat(inputScanner);
                    break;
                case 3:
                    cancelBooking(inputScanner);
                    break;
                case 4:
                    resetBus(inputScanner);
                    break;
                case 5:
                    displayBusWithCustomPageSize();
                    break;
                case 6:
                    System.out.println("-> Good bye!");
                    break;

                default:
                    System.out.println("Invalid option. Please choose again.");
            }
        } while (userChoice != 5);
        inputScanner.close();
    }

    public static void setPageSize() {
        Scanner scanner = new Scanner(System.in);
        int pageSize = 4;
        System.out.print("Enter the number of rows per page: ");
        int newSize = scanner.nextInt();
        if (newSize > 0) {
            pageSize = newSize;
            System.out.println("Page size set to " + newSize + " rows.");
        } else {
            System.out.println("Invalid page size. Please enter a positive number.");
        }
    }

    public static void displayBusWithCustomPageSize() {
        // Assuming 'busIndex' and 'inputScanner' are declared and initialized somewhere in your code
        int busIndex = 0; // Replace with the appropriate initialization
        Scanner inputScanner = new Scanner(System.in); // Replace with the appropriate initialization

        setPageSize(); // Call the method to set the page size
        displayBusInformation(busIndex, inputScanner); // Display stock data using the set page size
    }

    public static void displayMenu() {
            CellStyle cellStyle = new CellStyle(CellStyle.HorizontalAlign.center);
            Table menuTable = new Table(6, BorderStyle.UNICODE_DOUBLE_BOX_WIDE,
                    ShownBorders.ALL);


            menuTable.setColumnWidth(0, 15, 20);
            menuTable.setColumnWidth(1, 15, 20);
            menuTable.setColumnWidth(2, 15, 20);
            menuTable.setColumnWidth(3, 15, 20);
            menuTable.setColumnWidth(4, 20, 35);
            menuTable.setColumnWidth(5, 10, 20);


            menuTable.addCell("1- Check Bus", cellStyle);
            menuTable.addCell("2- Booking Bus", cellStyle);
            menuTable.addCell("3- Cancel Booking", cellStyle);
            menuTable.addCell("4- Reset Bus", cellStyle);
            menuTable.addCell("5- Set Row to show record");
            menuTable.addCell("6- Exit", cellStyle);


            System.out.println(menuTable.render());
    }
    static void setupBus(Scanner inputScanner) {

        int numberOfBuses;
        int seatsPerBus;

        while (true) {
            System.out.print("-> Enter number of Buses: ");
            String userInput = inputScanner.nextLine();

            if (isValidInteger(userInput)) {
                numberOfBuses = Integer.parseInt(userInput);
                break;
            } else {
                System.out.println("Invalid input. Please enter a valid positive integer for the number of buses.");
            }
        }

        while (true) {
            System.out.print("-> Enter number of Seats per bus: ");
            String userInput = inputScanner.nextLine();

            if (isValidInteger(userInput)) {
                seatsPerBus = Integer.parseInt(userInput);
                break;
            } else {
                System.out.println("Invalid input. Please enter a valid positive integer for the number of buses.");
            }
        }


        busSeats = new int[numberOfBuses][seatsPerBus];

        for (int i = 0; i < numberOfBuses; i++) {
            for (int j = 0; j < seatsPerBus; j++) {
                busSeats[i][j] = 1;
            }
        }
    }

    public static void showAllBusInformation(Scanner inputScanner) {

        CellStyle numberStyle = new CellStyle(CellStyle.HorizontalAlign.center);
        int page = 1;
        while (true) {
            Table table = new Table(4, BorderStyle.UNICODE_BOX, ShownBorders.ALL);
            int pageSize = 4;


            table.setColumnWidth(0, 5, 5);
            table.setColumnWidth(1, 10, 10);
            table.setColumnWidth(2, 15, 15);
            table.setColumnWidth(3, 15, 15);
            System.out.println("====================| Current Page: |" +page);
            table.addCell("Display All Bus Information", numberStyle, 4);
            table.addCell("Id", numberStyle);
            table.addCell("Seat", numberStyle);
            table.addCell("Available", numberStyle);
            table.addCell("Unavailable", numberStyle);

            int startRow = (page - 1) * pageSize;
            int endRow = Math.min(startRow + pageSize, busSeats.length);

            for (int i = startRow; i < endRow; i++) {
                int availableSeats = 0;
                for (int j = 0; j < busSeats[i].length; j++) {
                    if (busSeats[i][j] == 1) {
                        availableSeats++;
                    }
                }

                table.addCell(Integer.toString(i + 1), numberStyle);
                table.addCell(Integer.toString(busSeats[i].length), numberStyle);
                table.addCell(Integer.toString(availableSeats), numberStyle);
                table.addCell(Integer.toString(busSeats[i].length - availableSeats), numberStyle);
            }

            System.out.println(table.render());
            System.out.println("<-First(1)------ Previous(2)-----(3)Next------(4)Last------(5)See " +
                    "detail each Bus" + "--------- GoTo(6)Method-------");

            int opt = 0;
            while (true) {
                System.out.print("-> Press any key(Integer) to exit : ");

                String userInput = inputScanner.nextLine();

                if (isValidInteger(userInput)) {
                    opt = Integer.parseInt(userInput);
                    break;
                } else {
                    System.out.println("Invalid input. Please enter a valid positive integer for the number of buses.");
                }
            }
            switch (opt){
                case 1:
                    page = 1;
                    break;
                case 2:
                    if (page > 1) {
                        page--;
                    }
                    break;

                case 3:
                    int nextPage = page + 1;
                    int maxPage = (busSeats.length + pageSize - 1) / pageSize; // Calculate the maximum page
                    if (nextPage <= maxPage) {
                        page = nextPage;
                    } else {
                        System.out.println("No more pages available.");
                    }
                    break;
                case 4:
                    // Set the page to the last page
                    page = (busSeats.length + pageSize - 1) / pageSize;
                    break;
                case 5:
                    System.out.print("=> Enter 0 to go back or Bus Id to see details: ");
                    int busId = inputScanner.nextInt();
                    inputScanner.nextLine();
                    displayBusInformation(busId-1, inputScanner);
                    break;
                case 6:
                    System.out.print("Enter the page number: ");
                    int pageNumber = inputScanner.nextInt();
                    if (pageNumber >= 1) {
                        page = pageNumber;
                    } else {
                        System.out.println("Invalid page number. Please enter a positive number.");
                    }
                    break;
                default:
                    System.out.println("Back to the main menu...");
            }
            if (opt > 5 || opt < 1){
                break;
            }

        }

    }


    public static void displayBusInformation(int busIndex, Scanner inputScanner) {
        CellStyle textStyle = new CellStyle(CellStyle.HorizontalAlign.center);

        Table seatTable = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        seatTable.setColumnWidth(0, 10, 10);
        seatTable.setColumnWidth(1, 10, 10);
        seatTable.setColumnWidth(2, 10, 10);
        seatTable.setColumnWidth(3, 10, 10);
        seatTable.setColumnWidth(4, 10, 10);


        seatTable.addCell("Display Bus Information", textStyle, 5);


        int availableSeats = 0;
        System.out.println(busIndex);


        for (int i = 0; i < busSeats[busIndex].length; i++) {
            if (busSeats[busIndex][i] == 1) {

                seatTable.addCell("\u001B[32m(+) " + (i + 1) + "\u001B[0m", textStyle);
            } else {

                availableSeats++;
                seatTable.addCell("\u001B[31m(-) " + (i + 1) + "\u001B[0m", textStyle);
            }
        }


        System.out.println(seatTable.render());
        System.out.println("( - ) : Unavailable(" + availableSeats + ") ( + ) : Available(" + busSeats[busIndex].length + ")");

    }

    static void bookBusSeat(Scanner inputScanner) {
        System.out.println("---------- Bus Management System ----------");
        System.out.print("-> Enter bus’s Id: ");
        int busId = inputScanner.nextInt();

        displayBusInformation(busId - 1, inputScanner);

        System.out.print("-> Enter Seat number to book: ");
        int seatNumber = inputScanner.nextInt();

        if (busSeats[busId - 1][seatNumber - 1] == 1) {
            System.out.print("=> Do you want to book seat number " + seatNumber + "? (y/n): ");
            char userChoice = inputScanner.next().charAt(0);

            if (userChoice == 'y' || userChoice == 'Y') {
                busSeats[busId - 1][seatNumber - 1] = 0;
                System.out.println("Seat number " + seatNumber + " was booked successfully!");
            }
        } else {
            System.out.println("Seat number " + seatNumber + " is already booked. Please choose another seat.");
        }
    }

    static void cancelBooking(Scanner inputScanner) {
        System.out.println("---------- Bus Management System ----------");
        System.out.print("-> Enter bus’s Id: ");
        int busId = inputScanner.nextInt();

        displayBusInformation(busId - 1, inputScanner);

        System.out.print("-> Enter Seat number to cancel booking: ");
        int seatNumber = inputScanner.nextInt();

        if (busSeats[busId - 1][seatNumber - 1] == 0) {
            System.out.print("=> Do you want to cancel booking for seat number " + seatNumber + "? (y/n): ");
            char userChoice = inputScanner.next().charAt(0);

            if (userChoice == 'y' || userChoice == 'Y') {
                busSeats[busId - 1][seatNumber - 1] = 1;
                System.out.println("Booking for seat number " + seatNumber + " was canceled successfully!");
            }
        } else {
            System.out.println("Seat number " + seatNumber + " is not booked. Please choose another seat.");
        }
    }

    static void resetBus(Scanner inputScanner) {
        System.out.println("---------- Bus Management System ----------");
        System.out.print("-> Enter bus’s Id: ");
        int busId = inputScanner.nextInt();

        System.out.print("=> Reset all seats for Bus id " + busId + "? (y/n): ");
        char userChoice = inputScanner.next().charAt(0);

        if (userChoice == 'y' || userChoice == 'Y') {
            for (int i = 0; i < busSeats[busId - 1].length; i++) {
                busSeats[busId - 1][i] = 1;
            }

        }
    }

    private static boolean isValidInteger(String input) {
        String integerPattern = "^-?\\d+$";
        Pattern pattern = Pattern.compile(integerPattern);
        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }

}
