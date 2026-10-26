
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Scanner;

public class mainApp {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        HospitalDAO dao = new HospitalDAO();
        dao.prepareDao();

        int choice;

        do {
            System.out.println("\n--- HOSPITAL MANAGEMENT SYSTEM ---");
            System.out.println("1. Register Patient");
            System.out.println("2. Register Doctor");
            System.out.println("3. Schedule Appointment");
            System.out.println("4. Reschedule Appointment");
            System.out.println("5. Cancel Appointment");
            System.out.println("6. View Appointment Details");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Patient ID: ");
                    int patientId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Patient Name: ");
                    String patientName = sc.nextLine();

                    System.out.print("DOB (yyyy-mm-dd): ");
                    Date dob = Date.valueOf(sc.nextLine());

                    System.out.print("Phone: ");
                    String phone = sc.nextLine();

                    System.out.print("Email: ");
                    String email = sc.nextLine();

                    dao.registerPatient(patientId, patientName, dob, phone, email);
                    break;

                case 2:
                    System.out.print("Doctor ID: ");
                    int doctorId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Doctor Name: ");
                    String doctorName = sc.nextLine();

                    System.out.print("Specialization: ");
                    String specialization = sc.nextLine();

                    System.out.print("License Number: ");
                    String license = sc.nextLine();

                    dao.registerDoctor(doctorId, doctorName, specialization, license);
                    break;

                case 3:
                    System.out.print("Appointment ID: ");
                    int appointmentId = sc.nextInt();

                    System.out.print("Patient ID: ");
                    patientId = sc.nextInt();

                    System.out.print("Doctor ID: ");
                    doctorId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Appointment date and time (yyyy-mm-dd hh:mm:ss): ");
                    String dateTime = sc.nextLine();

                    Timestamp timestamp = Timestamp.valueOf(dateTime);
                    dao.scheduleAppointment(appointmentId, patientId, doctorId, timestamp);
                    break;

                case 4:
                    System.out.print("Appointment ID: ");
                    appointmentId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("New date and time (yyyy-mm-dd hh:mm:ss): ");
                    dateTime = sc.nextLine();

                    timestamp = Timestamp.valueOf(dateTime);
                    dao.rescheduleAppointment(appointmentId, timestamp);
                    break;

                case 5:
                    System.out.print("Appointment ID: ");
                    dao.cancelAppointment(sc.nextInt());
                    break;

                case 6:
                    dao.fetchAppointmentDetails();
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
