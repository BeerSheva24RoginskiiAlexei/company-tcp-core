package telran.employees;

import org.json.JSONArray;

import telran.net.Protocol;
import telran.net.Request;
import telran.net.Response;
import telran.net.ResponseCode;

import java.util.Arrays;
import java.util.NoSuchElementException;

public class CompanyProtocol implements Protocol {
    private final Company company;

    public CompanyProtocol(Company company) {
        this.company = company;
    }

    @Override
    public Response getResponse(Request req) {
        return switch (req.requestType()) {
            case "addEmployee" -> handleRequest(this::addEmployee, req.requestData());
            case "getDepartmentBudget" -> handleRequest(this::getDepartmentBudget, req.requestData());
            case "getDepartments" -> handleRequest(this::getDepartments, "");
            case "getEmployee" -> handleRequest(this::getEmployee, req.requestData());
            case "getManagersWithMostFactor" -> handleRequest(this::getManagersWithMostFactor, "");
            case "removeEmployee" -> handleRequest(this::removeEmployee, req.requestData());
            default -> new Response(ResponseCode.WRONG_TYPE, "Request type does not exist");
        };
    }

    private Response handleRequest(ResponseFunction function, String data) {
        try {
            return function.apply(data);
        } catch (Exception e) {
            return new Response(ResponseCode.WRONG_DATA, e.getMessage());
        }
    }

    private Response addEmployee(String data) {
        Employee employee = Employee.getEmployeeFromJSON(data);
        if (company.getEmployee(employee.getId()) != null) {
            throw new IllegalStateException("Employee with this ID already exists.");
        }
        company.addEmployee(employee);
        return new Response(ResponseCode.OK, "Employee added successfully.");
    }

    private Response getDepartmentBudget(String data) {
        int budget = company.getDepartmentBudget(data);
        return new Response(ResponseCode.OK, String.valueOf(budget));
    }

    private Response getDepartments(String ignored) {
        String[] departments = company.getDepartments();
        JSONArray jsonArray = new JSONArray(departments);
        return new Response(ResponseCode.OK, jsonArray.toString());
    }

    private Response getEmployee(String data) {
        Employee employee = company.getEmployee(Long.parseLong(data));
        if (employee == null) {
            throw new NoSuchElementException("Employee does not exist with this ID.");
        }
        return new Response(ResponseCode.OK, employee.toString());
    }

    private Response getManagersWithMostFactor(String ignored) {
        Manager[] managers = company.getManagersWithMostFactor();
        JSONArray jsonArray = new JSONArray(Arrays.stream(managers).map(Manager::toString).toArray());
        return new Response(ResponseCode.OK, jsonArray.toString());
    }

    private Response removeEmployee(String data) {
        long id = Long.parseLong(data);
        Employee removedEmployee = company.removeEmployee(id);
        if (removedEmployee == null) {
            throw new NoSuchElementException("Employee does not exist with this ID.");
        }
        return new Response(ResponseCode.OK, removedEmployee.toString());
    }

    @FunctionalInterface
    private interface ResponseFunction {
        Response apply(String data) throws Exception;
    }
}
