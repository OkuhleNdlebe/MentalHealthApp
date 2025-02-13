package za.ac.cput.mentalhealthapp.counselling.DAO;

import za.ac.cput.mentalhealthapp.counselling.ModelClasses.Booking;
import za.ac.cput.mentalhealthapp.counselling.ModelClasses.Student;

import javax.swing.*;
import java.nio.ByteBuffer;
import java.sql.*;
import java.time.LocalDate;
import java.util.UUID;
import java.util.Vector;

import static za.ac.cput.mentalhealthapp.counselling.GUIs.CounsellingGUI.vecArr;

public class BookingDAO {
    Connection con;
    PreparedStatement pst;
    Statement statement;
    String first_name, last_name, email, phone_number;
    int student_number;
    public static int count;

    //Connect to MySQL Database
    public void Connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/affirmation", "root", "");
            //System.out.println("Success");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //Retrieve data from database
    public void RetrieveStudentData() {
        Connect();

        try {

//            student_number = Integer.parseInt(txtStudentNum.getText());
//            pst = con.prepareStatement("select first_name,last_name,email,phone_number from students where student_number = ?");
            pst = con.prepareStatement("select student_number,first_name,last_name,email,phone_number from students");
//            pst.setInt(1, student_number);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                student_number = rs.getInt(1);
                first_name = rs.getString(2);
                last_name = rs.getString(3);
                email = rs.getString(4);
                phone_number = rs.getString(5);
            } else {
                JOptionPane.showMessageDialog(null, "Invalid Student");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //Add records to Bookings table in database
    public void AddBooking(Booking b, int student_number) {

        Connect();

        try {

            //student_number = Integer.parseInt(txtStudentNum.getText());
            pst = con.prepareStatement("select student_number,first_name,last_name from students where student_number = ?");
            pst.setInt(1, student_number);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                try {
                    pst = con.prepareStatement("insert into bookings(booking_id,booking_type,date,student_number)values(?,?,?,?)");
                    pst.setString(1, b.getBooking_id());
                    pst.setString(2, b.getBooking_type());
                    pst.setString(3, b.getDate() + "");
                    pst.setInt(4, b.getStudent_number());
                    pst.executeUpdate();
                    count++;
                    JOptionPane.showMessageDialog(null, "Successfully Submitted!!!");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

            } else {
                //JOptionPane.showMessageDialog(null, "Invalid student");

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //Retrieve data from database
    public void RetrieveBookings(int student_number) {

        Connect();

        try {

            pst = con.prepareStatement("select booking_id,booking_type,date from bookings where student_number = ?");
            pst.setInt(1, student_number);
            ResultSet rs = pst.executeQuery();

            ResultSetMetaData rd = rs.getMetaData();
            int a = rd.getColumnCount();
            count = 0;

            while (rs.next()) {

                Vector vect = new Vector();

                for (int i = 1; i <= a; i++) {
                    vect.add(rs.getString("booking_id"));
                    vect.add(rs.getString("booking_type"));
                    vect.add(rs.getDate("date"));
                    count++;
                }
                vecArr.add(vect);
                System.out.println(vecArr);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void deleteBooking(String bid){

        Connect();

        try {
            pst = con.prepareStatement("delete from bookings where booking_id = ?");
            pst.setString(1, bid);

            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Record Deleted!!!!!");

        }

        catch (SQLException e1)
        {

            e1.printStackTrace();
        }
    }

    public int countRows() throws SQLException {
        // Statements allow to issue SQL queries to the database
        Connect();

        statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery("select count(*) from bookings");
        resultSet.next();
        int count = resultSet.getInt(1);
        return count;
    }

    public static void main(String[] args) throws SQLException {
        new BookingDAO().countRows();
    }
}
