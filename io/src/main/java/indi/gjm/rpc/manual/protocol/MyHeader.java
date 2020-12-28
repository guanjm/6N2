package indi.gjm.rpc.manual.protocol;

import java.io.Serializable;

public class MyHeader implements Serializable {

    //传输id
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
