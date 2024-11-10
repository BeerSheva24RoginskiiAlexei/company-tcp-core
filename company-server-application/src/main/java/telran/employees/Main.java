package telran.employees;

import telran.io.*;
import telran.net.*;

public class Main {
    private static final int PORT = 4000;
    private static final String DATA_FILE = "employees.data";

    public static void main(String[] args) {
        Company company = new CompanyImpl();
        if (company instanceof Persistable persistable) {
            persistable.restoreFromFile(DATA_FILE);
        }
        TcpServer server = new TcpServer(new CompanyProtocol(company), PORT);
        server.run();
    }
}
