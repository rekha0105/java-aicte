import java.sql.*;
import java.util.*;

public class BankSystem {
    static Connection conn;
    static String user;

    public static void main(String[] a) {
        try (Scanner sc = new Scanner(System.in)) {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ba", "root", "2009");
            while (true) {
                System.out.println("\n1.Register 2.Customer Login 3.Employee Login 4.Admin Login 5.Exit");
                int ch = Integer.parseInt(sc.nextLine());
                if (ch == 1) register(sc);
                else if (ch == 2 && login(sc, "customers", "email", "password")) customerMenu(sc);
                else if (ch == 3 && login(sc, "employees", "employee_id", "name")) System.out.println("Employee logged in.");
                else if (ch == 4 && login(sc, "admin", "username", "password")) adminMenu(sc);
                else if (ch == 5) { conn.close(); System.exit(0); }
                else System.out.println("Invalid.");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    static void register(Scanner sc) throws Exception {
        System.out.print("Email: "); String e = sc.nextLine();
        System.out.print("Pass: "); String p = sc.nextLine();
        System.out.print("Name: "); String n = sc.nextLine();
        var s = conn.prepareStatement("INSERT INTO customers (email,password,name) VALUES (?,?,?)");
        s.setString(1, e); s.setString(2, p); s.setString(3, n);
        System.out.println(s.executeUpdate() > 0 ? "Registered!" : "Failed.");
    }

    static boolean login(Scanner sc, String tbl, String ucol, String pcol) throws Exception {
        System.out.print(ucol + ": "); String u = sc.nextLine();
        System.out.print(pcol + ": "); String p = sc.nextLine();
        var s = conn.prepareStatement("SELECT * FROM " + tbl + " WHERE " + ucol + "=? AND " + pcol + "=?");
        s.setString(1, u); s.setString(2, p);
        var r = s.executeQuery();
        if (r.next()) { if (tbl.equals("customers")) user = u; return true; }
        System.out.println("Login failed."); return false;
    }

    static void customerMenu(Scanner sc) throws Exception {
        while (true) {
            System.out.println("1.Deposit 2.Withdraw 3.Balance 4.Logout");
            int ch = Integer.parseInt(sc.nextLine());
            if (ch == 1) transaction(sc, true);
            else if (ch == 2) transaction(sc, false);
            else if (ch == 3) {
                var s = conn.prepareStatement("SELECT balance FROM customers WHERE email=?");
                s.setString(1, user);
                var r = s.executeQuery();
                if (r.next()) System.out.println("Balance: " + r.getDouble(1));
            } else if (ch == 4) break;
            else System.out.println("Invalid.");
        }
    }

    static void transaction(Scanner sc, boolean isDeposit) throws Exception {
        System.out.print("Amount: "); double a = Double.parseDouble(sc.nextLine());
        if (!isDeposit) {
            var s = conn.prepareStatement("SELECT balance FROM customers WHERE email=?");
            s.setString(1, user);
            var r = s.executeQuery();
            if (r.next() && r.getDouble(1) < a) {
                System.out.println("Insufficient balance."); return;
            }
            a = -a;
        }
        var u = conn.prepareStatement("UPDATE customers SET balance = balance + ? WHERE email=?");
        u.setDouble(1, a); u.setString(2, user);
        u.executeUpdate();
        System.out.println(isDeposit ? "Deposited." : "Withdrawn.");
    }

    static void adminMenu(Scanner sc) throws Exception {
        while (true) {
            System.out.println("1.View Customers 2.View Employees 3.Add Interest 4.Logout");
            int ch = Integer.parseInt(sc.nextLine());
            if (ch == 1) show("SELECT id,email,name,balance FROM customers");
            else if (ch == 2) show("SELECT employee_id,name,position FROM employees");
            else if (ch == 3) {
                var s = conn.prepareStatement("UPDATE customers SET balance = balance + (balance * interest_rate)");
                System.out.println("Interest added to " + s.executeUpdate() + " customers.");
            } else if (ch == 4) break;
            else System.out.println("Invalid.");
        }
    }

    static void show(String q) throws Exception {
        var r = conn.createStatement().executeQuery(q);
        while (r.next()) {
            for (int i = 1; i <= r.getMetaData().getColumnCount(); i++)
                System.out.print(r.getString(i) + "\t");
            System.out.println();
        }
    }
}
