package OtwayRees.Client;

import com.sun.deploy.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * Created on 10.05.15.
 */
public class ClientReadInputThread extends Thread {

    private ResultSetter setter;
    public ClientReadInputThread() {
    }
    public void setResultSetter(ResultSetter setter) {
        this.setter = setter;
    }
    public void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter your name:");
        try {
            String name = br.readLine();
            System.out.println("\tStart Communication Server for User(" + name + ") : 1");
            System.out.println("\tStart Communication Client for User(" + name + ") : 2");
            System.out.print("Usage: [ 1 | 2 ]:");
            String cmd = br.readLine();
            if (cmd.equals("2")) {
                System.out.print("\tEnter the name of your Chat partner:");
                String nameB = br.readLine();
                setter.setResult(name,nameB);
            } else {
                setter.setResult(name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
