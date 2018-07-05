package com.oracle.graal.python.builtins.objects.descr;

import com.oracle.graal.python.builtins.objects.method.PBuiltinMethod;
import com.oracle.graal.python.builtins.objects.object.PythonBuiltinObject;
import com.oracle.graal.python.builtins.objects.type.PythonClass;

public class PMethodDescriptor extends PythonBuiltinObject {
    private final PythonClass type;
    private final PBuiltinMethod method;

    public PBuiltinMethod getMethod() {
        return method;
    }

    public PMethodDescriptor(PythonClass cls, PythonClass type, PBuiltinMethod function) {
        super(cls);
        this.type = type;
        this.method = function;
    }

    @Override
    public String toString() {
        return "<method '" + method.getName() + "' of '" + type.getName() + "' objects>";
    }
}
