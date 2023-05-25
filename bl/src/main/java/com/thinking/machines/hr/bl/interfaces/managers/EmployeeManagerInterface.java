package com.thinking.machines.hr.bl.interfaces.managers;
import java.util.*;
import com.thinking.machines.hr.bl.exception.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
public interface EmployeeManagerInterface
{
public void addEmployee(EmployeeInterface employee) throws BLException;
public void updateEmployee(EmployeeInterface employee) throws BLException;
public void deleteEmployee(String employeeId) throws BLException;
public EmployeeInterface getEmployeeByEmployeeId(String employeeId) throws BLException;
public EmployeeInterface getEmployeeByPANNumber(String panNumber) throws BLException;
public EmployeeInterface getEmployeeByAadharCardNumber(String AadharCardNumber) throws BLException;
public boolean isDesignationAlloted(int designationCode) throws BLException;
public boolean employeeIdExists(String employeeId);
public boolean employeePANNumberExists(String panNumber);
public boolean employeeAadharCardNumberExists(String aadharCardNumber);
public Set<EmployeeInterface> getEmployees();
public Set<EmployeeInterface> getEmployeeByDesignationCode(int designationCode) throws BLException;
public int getEmployeeCount() throws BLException;
public int getEmployeeCountByDesignationCode(int designationCode) throws BLException;
}