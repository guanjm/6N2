package indi.gjm.rpc.manual;

import java.io.Serializable;

/**
 *
 * @author : guanjm
 * @date: 2020/12/21
 *
 */
public class MyRequest implements Serializable {

    private String interfaceName;

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] args;

    public MyRequest(String interfaceName, String methodName, Class<?>[] parameterTypes, Object[] args) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.args = args;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
