package com.oracle.graal.python.builtins.objects.method;

import com.oracle.graal.python.builtins.Builtin;
import com.oracle.graal.python.builtins.CoreFunctions;
import com.oracle.graal.python.builtins.PythonBuiltins;
import com.oracle.graal.python.nodes.function.PythonBuiltinBaseNode;
import com.oracle.graal.python.nodes.function.PythonBuiltinNode;
import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.dsl.Specialization;

import java.util.List;

import static com.oracle.graal.python.nodes.SpecialMethodNames.__REPR__;
import static com.oracle.graal.python.nodes.SpecialMethodNames.__STR__;

@CoreFunctions(extendClasses = PBuiltinMethod.class)
public class BuiltinMethodBuiltins extends PythonBuiltins {
    @Override
    protected List<? extends NodeFactory<? extends PythonBuiltinBaseNode>> getNodeFactories() {
        return BuiltinMethodBuiltinsFactory.getFactories();
    }

    @Builtin(name = __STR__, fixedNumOfArguments = 1)
    @GenerateNodeFactory
    abstract static class StrNode extends PythonBuiltinNode {
        @Specialization
        public Object gen(Object self) {
            return self.toString();
        }
    }

    @Builtin(name = __REPR__, fixedNumOfArguments = 1)
    @GenerateNodeFactory
    abstract static class RepNode extends StrNode {
    }
}
