package kickdrum.example.com.company.audit;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Employee {
    private String name;
    private int age;
    private double salary;
    private String department;

    public Employee(String name, int age, double salary, String department) {
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.department = department;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public double getSalary() { return salary; }
    public String getDepartment() { return department; }

    @Override
    public String toString() {
        return String.format("Employee{Name='%s', Dept='%s', Salary=$%.2f, Age=%d}",
                name, department, salary, age);
    }

    public static List<Employee> getSampleData() {
        return List.of(
                new Employee("Alice Johnson", 30, 85000.0, "ENGINEERING"),
                new Employee("Bob Smith", 45, 62000.0, "HR"),
                new Employee("Charlie Brown", 22, 91000.0, "ENGINEERING"),
                new Employee("David Lee", 58, 75000.0, "HR"),
                new Employee("Eve Wilson", 33, 55000.0, "SALES"),
                new Employee("Frank Miller", 55, 120000.0, "ENGINEERING")
        );
    }


    public static List<Employee> filterHighPaidEngineers(List<Employee> employees) {
        return employees.stream()
                .filter(s -> s.getSalary() > 70000 && "ENGINEERING".equals(s.getDepartment()))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        List<Employee> employees = getSampleData();


        List<Employee> res = filterHighPaidEngineers(employees);


        List<String> names = employees.stream()
                .map(s -> s.getName().toUpperCase())
                .collect(Collectors.toList());


        double totalSalary = employees.stream()
                .map(Employee::getSalary)
                .reduce(0.0, Double::sum);


        System.out.println(res);
        System.out.println();
        System.out.println(names);
        System.out.println();
        System.out.println(totalSalary);
    }
}
