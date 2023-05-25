import java.sql.*;
class jdbc1psp
{
public static void main(String gg[])
{
try
{
Class.forName("com.mysql.cj.jdbc.Driver");
Connection c;
c=DriverManager.getConnection("jdbc:mysql://localhost:3306/hrdb","hr","hr");
Statement s;
s=c.createStatement();
s.executeUpdate("insert into designation (title) values ('Drawing Teacher')");
s.close();
c.close();
System.out.println("Done");
}catch(Exception e)
{
System.out.println(e);
}
}
}