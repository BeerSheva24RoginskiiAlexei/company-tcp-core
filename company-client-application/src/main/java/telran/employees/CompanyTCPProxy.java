package telran.employees;

import java.util.Iterator;

import org.json.JSONArray;

import telran.net.TcpClient;

public class CompanyTCPProxy implements Company{
    TcpClient tcpClient;
    public CompanyTCPProxy (TcpClient tcpClient) {
        this.tcpClient = tcpClient;
    }
    @Override
    public Iterator<Employee> iterator() {
        return null;
    }

    @Override
    public void addEmployee(Employee empl) {
        tcpClient.sendAndReceive("addEmployee", empl.toString());
    }

    @Override
    public int getDepartmentBudget(String department) {
        String response = tcpClient.sendAndReceive("getDepartmentBudget", department);
        return Integer.parseInt(response);
    }

    @Override
    public String[] getDepartments() {
        String jsonStr = tcpClient.sendAndReceive("getDepartments", "");
        JSONArray jsonArray = new JSONArray(jsonStr);
        return jsonArray.toList().stream()
                .map(Object::toString)
                .toArray(String[]::new);
    }

    @Override
    public Employee getEmployee(long id) {
        String emplJSON = tcpClient.sendAndReceive("getEmployee", String.valueOf(id));
        return Employee.getEmployeeFromJSON(emplJSON);
    }

    @Override
    public Manager[] getManagersWithMostFactor() {
        String managersJSON = tcpClient.sendAndReceive("getManagersWithMostFactor", "");
        JSONArray jsonArray = new JSONArray(managersJSON);
        return jsonArray.toList().stream()
                .map(Object::toString)
                .map(Employee::getEmployeeFromJSON)
                .toArray(Manager[]::new);
    }

    @Override
    public Employee removeEmployee(long id) {
        String emplJSON = tcpClient.sendAndReceive("removeEmployee", String.valueOf(id));
        return Employee.getEmployeeFromJSON(emplJSON);
    }

}