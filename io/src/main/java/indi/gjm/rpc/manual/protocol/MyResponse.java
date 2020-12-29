package indi.gjm.rpc.manual.protocol;

public class MyResponse extends MyHeader {

    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
