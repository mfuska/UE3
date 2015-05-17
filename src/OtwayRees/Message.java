package OtwayRees;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created on 11.05.15.
 */
public class Message implements Serializable {
    private String userNameA;
    private String userNameB;

    private int msgID;

    private String kA;
    private String kB;

    public Message(int id) {
        this.msgID = id;
    }
    public Message(int id, String nameA, String nameB) {
        this.msgID = id;
        this.userNameA = nameA;
        this.userNameB = nameB;
    }
    public String getUserNameA() {
        return this.userNameA;
    }
    public String getUserNameB() {
        return this.userNameB;
    }
    public int getMsgID() {
        return this.msgID;
    }
    public String getKA() {
        return this.kA;
    }
    public String getKB() {
        return this.kB;
    }

    public void setUserNameA(String name) {
        this.userNameA = name;
    }
    public void setUserNameB(String name) {
        this.userNameB = name;
    }
    public void setMsgID(int id) {
        this.msgID = id;
    }
    public void setkA(String value) {
        this.kA = value;
    }
    public void setkB(String value) {
        this.kB = value;
    }
    public int sizeof()  {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(byteOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteOutputStream.toByteArray().length;
    }
}
