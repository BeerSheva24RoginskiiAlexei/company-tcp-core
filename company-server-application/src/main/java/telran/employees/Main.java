package telran.employees;

import telran.io.Persistable;

public class Main {
    public static void main(String[] args) {
        Company company = new CompanyImpl();

        if (company instanceof Persistable persistable) {
            persistable.restoreFromFile("employees.data");

            PeriodicSaver saver = new PeriodicSaver(company);
            saver.start();

            PeriodicSaver.setDelay(60); 
            PeriodicSaver.setFileName("new_employees.data");
        }

    }
}
