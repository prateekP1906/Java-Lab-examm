
import java.sql.*;
import java.util.logging.*;

public class HospitalDAO {

    private static final Logger logger = Logger.getLogger(HospitalDAO.class.getName());
    private Connection conn = null;
    private PreparedStatement ps = null;

    public void prepareDao() {
        try {
            conn = DBConnection.getConnection();

            FileHandler fh = new FileHandler("hospitalApp.log", true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);

            logger.info("Hospital DAO prepared successfully");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "DAO preparation failed", e);
        }
    }

    // REGISTER PATIENT
    public void registerPatient(int id, String name, Date dob, String phone, String email) {
        try {
            String query = "INSERT INTO Patient(Patient_ID, Patient_Name, DOB, Phone, Email) VALUES (?,?,?,?,?)";
            ps = conn.prepareStatement(query);

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setDate(3, dob);
            ps.setString(4, phone);
            ps.setString(5, email);

            int rows = ps.executeUpdate();
            logger.info(rows + " patient registered successfully");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Patient registration failed", e);
        }
    }

    // REGISTER DOCTOR
    public void registerDoctor(int id, String name, String specialization, String license) {
        try {
            String query = "INSERT INTO Doctor(Doctor_ID, Doctor_Name, Specialization, License_No) VALUES (?,?,?,?)";
            ps = conn.prepareStatement(query);

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, specialization);
            ps.setString(4, license);

            int rows = ps.executeUpdate();
            logger.info(rows + " doctor registered successfully");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Doctor registration failed", e);
        }
    }

    // SCHEDULE APPOINTMENT
    public void scheduleAppointment(int appointmentId, int patientId, int doctorId, Timestamp dateTime) {
        try {
            String query = "INSERT INTO Appointment(Appointment_ID, Patient_ID, Doctor_ID, Appointment_DateTime, Status) VALUES (?,?,?,?,?)";
            ps = conn.prepareStatement(query);

            ps.setInt(1, appointmentId);
            ps.setInt(2, patientId);
            ps.setInt(3, doctorId);
            ps.setTimestamp(4, dateTime);
            ps.setString(5, "Scheduled");

            int rows = ps.executeUpdate();
            logger.info(rows + " appointment scheduled successfully");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Appointment scheduling failed", e);
        }
    }

    // RESCHEDULE APPOINTMENT
    public void rescheduleAppointment(int appointmentId, Timestamp newDateTime) {
        try {
            String query = "UPDATE Appointment SET Appointment_DateTime=? WHERE Appointment_ID=? AND Status='Scheduled'";
            ps = conn.prepareStatement(query);

            ps.setTimestamp(1, newDateTime);
            ps.setInt(2, appointmentId);

            int rows = ps.executeUpdate();
            logger.info(rows + " appointment(s) rescheduled");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Appointment rescheduling failed", e);
        }
    }

    // CANCEL APPOINTMENT
    public void cancelAppointment(int appointmentId) {
        try {
            String query = "UPDATE Appointment SET Status='Cancelled' WHERE Appointment_ID=? AND Status='Scheduled'";
            ps = conn.prepareStatement(query);

            ps.setInt(1, appointmentId);

            int rows = ps.executeUpdate();
            logger.info(rows + " appointment(s) cancelled");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Appointment cancellation failed", e);
        }
    }

    // RETRIEVE APPOINTMENT DETAILS USING JOIN
    public void fetchAppointmentDetails() {
        String query = "SELECT Appointment.Appointment_ID, Patient.Patient_Name, Doctor.Doctor_Name, Doctor.Specialization, Appointment.Appointment_DateTime, Appointment.Status FROM Appointment JOIN Patient ON Appointment.Patient_ID=Patient.Patient_ID JOIN Doctor ON Appointment.Doctor_ID=Doctor.Doctor_ID";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                logger.info("Appointment ID: " + rs.getInt("Appointment_ID") + ", Patient: " + rs.getString("Patient_Name") + ", Doctor: " + rs.getString("Doctor_Name") + ", Specialization: " + rs.getString("Specialization") + ", Date: " + rs.getTimestamp("Appointment_DateTime") + ", Status: " + rs.getString("Status"));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Appointment retrieval failed", e);
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
