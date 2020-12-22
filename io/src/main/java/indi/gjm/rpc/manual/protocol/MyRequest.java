package indi.gjm.rpc.manual.protocol;

import java.util.UUID;

public class MyRequest extends MyHeader {

    //类名
    private String className;

    //方法名
    private String methodName;

    //参数类型
    private Class<?>[] parameterTypes;

    //实际参数
    private Object[] args;

    public MyRequest(String className, String methodName, Class<?>[] parameterTypes, Object[] args) {
        this.id = UUID.randomUUID().toString();
        this.className = className;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.args = args;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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
