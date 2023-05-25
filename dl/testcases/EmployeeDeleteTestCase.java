import com.thinking.machines.hr.dl.exception.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.enums.*;
import java.text.*;
import java.math.*;
import java.util.*;
public class EmployeeDeleteTestCase
{
public static void main(String gg[])
{
String employeeId=(gg[0]);
try
{
EmployeeDAOInterface employeeDAO;
employeeDAO=new EmployeeDAO();
employeeDAO.delete(employeeId);
System.out.println("Employee delete");
}catch(DAOException daoException)
{
System.out.println(daoException.getMessage());
}
}
}