package com.oracle.graal.python.builtins.objects.descr;

import com.oracle.graal.python.builtins.objects.function.Arity;
import com.oracle.graal.python.builtins.objects.function.PythonCallable;
import com.oracle.graal.python.builtins.objects.method.PBuiltinMethod;
import com.oracle.graal.python.builtins.objects.object.PythonBuiltinObject;
import com.oracle.graal.python.builtins.objects.type.PythonClass;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.frame.FrameDescriptor;

public class PMethodDescriptor extends PythonBuiltinObject implements PythonCallable {
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

    @Override
    public RootCallTarget getCallTarget() {
        return method.getCallTarget();
    }

    @Override
    public FrameDescriptor getFrameDescriptor() {
        return method.getFrameDescriptor();
    }

    @Override
    public String getName() {
        return method.getName();
    }

    @Override
    public Arity getArity() {
        return method.getArity();
    }
}
