
import java.sql.*;
import java.util.logging.*;

public class LibraryDAO {

    private static final Logger logger = Logger.getLogger(LibraryDAO.class.getName());
    private Connection conn = null;
    private PreparedStatement ps = null;

    public void prepareDao() {
        try {
            conn = DBConnection.getConnection();

            FileHandler fh = new FileHandler("libraryApp.log", true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);

            logger.info("Library DAO prepared successfully");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "DAO preparation failed", e);
        }
    }

    // ADD BOOK
    public void addBook(int id, String isbn, String title, String author, int total, int available) {
        try {
            String query = "INSERT INTO Book(BOOK_ID, ISBN, TITLE, AUTHOR, TOTAL_COPIES, AVAILABLE_COPIES) VALUES (?,?,?,?,?,?)";
            ps = conn.prepareStatement(query);

            ps.setInt(1, id);
            ps.setString(2, isbn);
            ps.setString(3, title);
            ps.setString(4, author);
            ps.setInt(5, total);
            ps.setInt(6, available);

            int rows = ps.executeUpdate();
            logger.info(rows + " book inserted successfully");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Book insertion failed", e);
        }
    }

    // MODIFY BOOK
    public void modifyBook(int id, String title, String author, int total, int available) {
        try {
            String query = "UPDATE Book SET TITLE=?, AUTHOR=?, TOTAL_COPIES=?, AVAILABLE_COPIES=? WHERE BOOK_ID=?";
            ps = conn.prepareStatement(query);

            ps.setString(1, title);
            ps.setString(2, author);
            ps.setInt(3, total);
            ps.setInt(4, available);
            ps.setInt(5, id);

            int rows = ps.executeUpdate();
            logger.info(rows + " book(s) modified");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Book modification failed", e);
        }
    }

    // SEARCH BOOK
    public void searchBook(int id) {
        try {
            String query = "SELECT * FROM Book WHERE BOOK_ID=?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                logger.info("Book ID: " + rs.getInt("BOOK_ID") + ", ISBN: " + rs.getString("ISBN") + ", Title: " + rs.getString("TITLE") + ", Author: " + rs.getString("AUTHOR") + ", Total: " + rs.getInt("TOTAL_COPIES") + ", Available: " + rs.getInt("AVAILABLE_COPIES"));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Book search failed", e);
        }
    }

    // DISPLAY ALL BOOKS
    public void displayBooks() {
        String query = "SELECT * FROM Book";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                logger.info("Book ID: " + rs.getInt("BOOK_ID") + ", Title: " + rs.getString("TITLE") + ", Author: " + rs.getString("AUTHOR") + ", Available: " + rs.getInt("AVAILABLE_COPIES"));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Book display failed", e);
        }
    }

    // ADD MEMBER
    public void addMember(int id, String name, String email, String phone) {
        try {
            String query = "INSERT INTO Member(MEMBER_ID, MEMBER_NAME, EMAIL, PHONE) VALUES (?,?,?,?)";
            ps = conn.prepareStatement(query);

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, email);
            ps.setString(4, phone);

            int rows = ps.executeUpdate();
            logger.info(rows + " member inserted successfully");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Member insertion failed", e);
        }
    }

    // MODIFY MEMBER
    public void modifyMember(int id, String name, String email, String phone) {
        try {
            String query = "UPDATE Member SET MEMBER_NAME=?, EMAIL=?, PHONE=? WHERE MEMBER_ID=?";
            ps = conn.prepareStatement(query);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setInt(4, id);

            int rows = ps.executeUpdate();
            logger.info(rows + " member(s) modified");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Member modification failed", e);
        }
    }

    // SEARCH MEMBER
    public void searchMember(int id) {
        try {
            String query = "SELECT * FROM Member WHERE MEMBER_ID=?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                logger.info("Member ID: " + rs.getInt("MEMBER_ID") + ", Name: " + rs.getString("MEMBER_NAME") + ", Email: " + rs.getString("EMAIL") + ", Phone: " + rs.getString("PHONE"));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Member search failed", e);
        }
    }

    // ISSUE BOOK
    public void issueBook(int borrowId, int bookId, int memberId, Date issueDate, Date dueDate) {
        try {
            conn.setAutoCommit(false);

            String check = "SELECT AVAILABLE_COPIES FROM Book WHERE BOOK_ID=?";
            ps = conn.prepareStatement(check);
            ps.setInt(1, bookId);

            ResultSet rs = ps.executeQuery();

            if (rs.next() && rs.getInt("AVAILABLE_COPIES") > 0) {
                String query = "INSERT INTO Borrowing(BORROW_ID, BOOK_ID, MEMBER_ID, ISSUE_DATE, DUE_DATE, RETURN_DATE) VALUES (?,?,?,?,?,NULL)";
                ps = conn.prepareStatement(query);

                ps.setInt(1, borrowId);
                ps.setInt(2, bookId);
                ps.setInt(3, memberId);
                ps.setDate(4, issueDate);
                ps.setDate(5, dueDate);
                ps.executeUpdate();

                String update = "UPDATE Book SET AVAILABLE_COPIES=AVAILABLE_COPIES-1 WHERE BOOK_ID=?";
                ps = conn.prepareStatement(update);
                ps.setInt(1, bookId);
                ps.executeUpdate();

                conn.commit();
                logger.info("Book issued successfully");
            } else {
                conn.rollback();
                logger.warning("Book not available or book ID not found");
            }

            conn.setAutoCommit(true);
        } catch (Exception e) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Rollback failed", ex);
            }
            logger.log(Level.SEVERE, "Book issue failed", e);
        }
    }

    // RETURN BOOK
    public void returnBook(int borrowId) {
        try {
            conn.setAutoCommit(false);

            String check = "SELECT BOOK_ID FROM Borrowing WHERE BORROW_ID=? AND RETURN_DATE IS NULL";
            ps = conn.prepareStatement(check);
            ps.setInt(1, borrowId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int bookId = rs.getInt("BOOK_ID");

                String query = "UPDATE Borrowing SET RETURN_DATE=? WHERE BORROW_ID=?";
                ps = conn.prepareStatement(query);
                ps.setDate(1, new Date(System.currentTimeMillis()));
                ps.setInt(2, borrowId);
                ps.executeUpdate();

                String update = "UPDATE Book SET AVAILABLE_COPIES=AVAILABLE_COPIES+1 WHERE BOOK_ID=?";
                ps = conn.prepareStatement(update);
                ps.setInt(1, bookId);
                ps.executeUpdate();

                conn.commit();
                logger.info("Book returned successfully");
            } else {
                conn.rollback();
                logger.warning("Borrowing record not found or book already returned");
            }

            conn.setAutoCommit(true);
        } catch (Exception e) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Rollback failed", ex);
            }
            logger.log(Level.SEVERE, "Book return failed", e);
        }
    }

    // DISPLAY ALL BORROWING RECORDS
    public void displayBorrowing() {
        String query = "SELECT * FROM Borrowing";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                logger.info("Borrow ID: " + rs.getInt("BORROW_ID") + ", Book ID: " + rs.getInt("BOOK_ID") + ", Member ID: " + rs.getInt("MEMBER_ID") + ", Issue Date: " + rs.getDate("ISSUE_DATE") + ", Due Date: " + rs.getDate("DUE_DATE") + ", Return Date: " + rs.getDate("RETURN_DATE"));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Borrowing display failed", e);
        }
    }

    public void closeDao() {
        try {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Closing DAO failed", e);
        }
    }
}
