import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

public class EmployeePayrollService {
    private List<EmployeePayrollData> employeePayrollList;
    public EmployeePayrollService(){   }
    public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList){
        this.employeePayrollList = employeePayrollList;
    }
    public static void main(String[] args) {
        ArrayList<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        Scanner consoleInputReader = new Scanner(System.in);
        EmployeePayrollService employeePayrollService = new EmployeePayrollService(employeePayrollList);
        employeePayrollService.readEmployeePayrollData(consoleInputReader);
        employeePayrollService.writeEmployeePayrollData();
    }
    /*
    Use Case 1: Employee Payroll service to read and write employee payroll to a console
     */
    public void readEmployeePayrollData(Scanner consoleInputReader){
        System.out.println("Enter Employee Id: ");
        int id = consoleInputReader.nextInt();
        System.out.println("Enter Employee name: ");
        String name = consoleInputReader.next();
        System.out.println("Enter Employee Salary");
        double salary = consoleInputReader.nextDouble();
        employeePayrollList.add(new EmployeePayrollData(id,name,salary));
    }
    public void writeEmployeePayrollData(){
        System.out.println("Writing Employee payroll data to console: \n"+ employeePayrollList);
    }
}