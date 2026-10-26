
import java.sql.Date;
import java.util.Scanner;

public class mainApp {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        LibraryDAO dao = new LibraryDAO();
        dao.prepareDao();

        int choice;

        do {
            System.out.println("\n--- LIBRARY MANAGEMENT SYSTEM ---");
            System.out.println("1. Add Book");
            System.out.println("2. Modify Book");
            System.out.println("3. Search Book");
            System.out.println("4. Display Books");
            System.out.println("5. Add Member");
            System.out.println("6. Modify Member");
            System.out.println("7. Search Member");
            System.out.println("8. Issue Book");
            System.out.println("9. Return Book");
            System.out.println("10. Display Borrowing Records");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Book ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    System.out.print("ISBN: ");
                    String isbn = sc.nextLine();

                    System.out.print("Title: ");
                    String title = sc.nextLine();

                    System.out.print("Author: ");
                    String author = sc.nextLine();

                    System.out.print("Total copies: ");
                    int total = sc.nextInt();

                    System.out.print("Available copies: ");
                    int available = sc.nextInt();

                    dao.addBook(id, isbn, title, author, total, available);
                    break;

                case 2:
                    System.out.print("Book ID: ");
                    id = sc.nextInt();
                    sc.nextLine();

                    System.out.print("New title: ");
                    title = sc.nextLine();

                    System.out.print("New author: ");
                    author = sc.nextLine();

                    System.out.print("Total copies: ");
                    total = sc.nextInt();

                    System.out.print("Available copies: ");
                    available = sc.nextInt();

                    dao.modifyBook(id, title, author, total, available);
                    break;

                case 3:
                    System.out.print("Enter Book ID: ");
                    dao.searchBook(sc.nextInt());
                    break;

                case 4:
                    dao.displayBooks();
                    break;

                case 5:
                    System.out.print("Member ID: ");
                    int memberId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Member name: ");
                    String name = sc.nextLine();

                    System.out.print("Email: ");
                    String email = sc.nextLine();

                    System.out.print("Phone: ");
                    String phone = sc.nextLine();

                    dao.addMember(memberId, name, email, phone);
                    break;

                case 6:
                    System.out.print("Member ID: ");
                    memberId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("New member name: ");
                    name = sc.nextLine();

                    System.out.print("New email: ");
                    email = sc.nextLine();

                    System.out.print("New phone: ");
                    phone = sc.nextLine();

                    dao.modifyMember(memberId, name, email, phone);
                    break;

                case 7:
                    System.out.print("Enter Member ID: ");
                    dao.searchMember(sc.nextInt());
                    break;

                case 8:
                    System.out.print("Borrow ID: ");
                    int borrowId = sc.nextInt();

                    System.out.print("Book ID: ");
                    int bookId = sc.nextInt();

                    System.out.print("Member ID: ");
                    memberId = sc.nextInt();

                    System.out.print("Issue date (yyyy-mm-dd): ");
                    Date issueDate = Date.valueOf(sc.next());

                    System.out.print("Due date (yyyy-mm-dd): ");
                    Date dueDate = Date.valueOf(sc.next());

                    dao.issueBook(borrowId, bookId, memberId, issueDate, dueDate);
                    break;

                case 9:
                    System.out.print("Enter Borrow ID: ");
                    dao.returnBook(sc.nextInt());
                    break;

                case 10:
                    dao.displayBorrowing();
                    break;

                case 0:
                    dao.closeDao();
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice");
            }

        } while (choice != 0);

        sc.close();
    }
}
