package com.oracle.graal.python.builtins.objects.descr;

import com.oracle.graal.python.builtins.Builtin;
import com.oracle.graal.python.builtins.CoreFunctions;
import com.oracle.graal.python.builtins.PythonBuiltins;
import com.oracle.graal.python.builtins.objects.PNone;
import com.oracle.graal.python.builtins.objects.PythonAbstractObject;
import com.oracle.graal.python.builtins.objects.object.PythonObject;
import com.oracle.graal.python.builtins.objects.type.PythonClass;
import com.oracle.graal.python.nodes.function.PythonBuiltinBaseNode;
import com.oracle.graal.python.nodes.function.builtins.PythonTernaryBuiltinNode;
import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.dsl.Specialization;

import java.util.List;

import static com.oracle.graal.python.nodes.SpecialMethodNames.__GET__;

@CoreFunctions(extendClasses = PClassMethodDescriptor.class)
public class ClassMethodDescrBuiltins extends MethodDescrBuiltins {
    @Override
    protected List<? extends NodeFactory<? extends PythonBuiltinBaseNode>> getNodeFactories() {
        return ClassMethodDescrBuiltinsFactory.getFactories();
    }

    @Builtin(name = __GET__, fixedNumOfArguments = 3)
    @GenerateNodeFactory
    abstract static class ClassMethodDescrGetNode extends PythonTernaryBuiltinNode {
        @Specialization
        Object get(PClassMethodDescriptor self, Object obj, Object type) {
            if (type == null || type == PNone.NONE) {
                if (obj != null && obj != PNone.NONE) {
                    type = ((PythonObject) obj).getPythonClass();
                } else {
                    // throw type error
                    return null;
                }
            }
            return self.getMethod().bindTo(type);
        }
    }
}
