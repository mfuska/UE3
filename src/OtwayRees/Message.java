package OtwayRees;

import java.io.Serializable;

/**
 * Created by mike on 11.05.15.
 */
public class Message implements Serializable {
    private String userNameA;
    private String userNameB;

    private int msgID;

    private String kA;
    private String kB;
    private String kC;

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
    public String getKC() {
        return this.kC;
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
    public void setkC(String value) {
        this.kC = value;
    }
}
