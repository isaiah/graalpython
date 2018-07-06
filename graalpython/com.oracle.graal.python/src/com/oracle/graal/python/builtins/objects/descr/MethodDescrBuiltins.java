package com.oracle.graal.python.builtins.objects.descr;

import com.oracle.graal.python.builtins.Builtin;
import com.oracle.graal.python.builtins.CoreFunctions;
import com.oracle.graal.python.builtins.PythonBuiltins;
import com.oracle.graal.python.builtins.objects.PNone;
import com.oracle.graal.python.builtins.objects.function.PKeyword;
import com.oracle.graal.python.builtins.objects.method.PBuiltinMethod;
import com.oracle.graal.python.builtins.objects.method.PMethod;
import com.oracle.graal.python.builtins.objects.type.PythonBuiltinClass;
import com.oracle.graal.python.builtins.objects.type.PythonClass;
import com.oracle.graal.python.nodes.SpecialMethodNames;
import com.oracle.graal.python.nodes.argument.CreateArgumentsNode;
import com.oracle.graal.python.nodes.call.CallDispatchNode;
import com.oracle.graal.python.nodes.call.special.CallUnaryMethodNode;
import com.oracle.graal.python.nodes.function.PythonBuiltinBaseNode;
import com.oracle.graal.python.nodes.function.PythonBuiltinNode;
import com.oracle.graal.python.nodes.function.builtins.PythonTernaryBuiltinNode;
import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.profiles.BranchProfile;

import java.util.List;

import static com.oracle.graal.python.nodes.SpecialMethodNames.__CALL__;
import static com.oracle.graal.python.nodes.SpecialMethodNames.__GET__;

@CoreFunctions(extendClasses = PMethodDescriptor.class)
public class MethodDescrBuiltins extends PythonBuiltins {
    @Override
    protected List<? extends NodeFactory<? extends PythonBuiltinBaseNode>> getNodeFactories() {
        return MethodDescrBuiltinsFactory.getFactories();
    }

    @Builtin(name = __GET__, fixedNumOfArguments = 3)
    @GenerateNodeFactory
    abstract static class MethodDescrGetNode extends PythonTernaryBuiltinNode {
        private PythonBuiltinClass noneClass;
        @Child
        CallUnaryMethodNode callNode = CallUnaryMethodNode.create();
        private final BranchProfile branchProfile = BranchProfile.create();

        // https://github.com/python/cpython/blob/e8b19656396381407ad91473af5da8b0d4346e88/Objects/descrobject.c#L149
        @Specialization
        Object get(PMethodDescriptor descr, Object obj, PythonClass type) {
//            if (descr_check(getCore(), descr, obj, type, getNoneType())) {
//                return descr;
//            }
            if (obj != PNone.NONE) {
                return descr.getMethod().bindTo(obj);
            }
            return descr;
        }
    }

    @Builtin(name = SpecialMethodNames.__REPR__, fixedNumOfArguments = 1)
    @GenerateNodeFactory
    abstract static class StrNode extends PythonBuiltinNode {
        @Specialization
        public String doGen(PMethodDescriptor self) {
            return self.toString();
        }
    }

    @Builtin(name = __CALL__, minNumOfArguments = 1, takesVariableArguments = true, takesVariableKeywords = true)
    @GenerateNodeFactory
    public abstract static class CallNode extends PythonBuiltinNode {
        @Child private CallDispatchNode dispatch = CallDispatchNode.create("callCall");
        @Child private CreateArgumentsNode createArgs = CreateArgumentsNode.create();

        @Specialization
        protected Object doIt(PMethodDescriptor self, Object[] arguments, PKeyword[] keywords) {
            return dispatch.executeCall(self.getMethod(), createArgs.execute(arguments), keywords);
        }
    }

}
