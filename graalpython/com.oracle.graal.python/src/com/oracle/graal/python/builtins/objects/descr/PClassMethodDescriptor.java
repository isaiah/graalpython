package com.oracle.graal.python.builtins.objects.descr;

import com.oracle.graal.python.builtins.objects.method.PBuiltinMethod;
import com.oracle.graal.python.builtins.objects.type.PythonClass;

public class PClassMethodDescriptor extends PMethodDescriptor {
    public PClassMethodDescriptor(PythonClass cls, PythonClass type, PBuiltinMethod function) {
        super(cls, type, function);
    }
}
