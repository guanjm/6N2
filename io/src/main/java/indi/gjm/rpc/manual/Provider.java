package indi.gjm.rpc.manual;

/**
 *
 * @author : guanjm
 * @date: 2020/12/21
 *
 */
public class Provider implements MyInterface {

    @Override
    public String print(String msg) {
        return "Provider print: [" + msg + "]!";
    }

}


