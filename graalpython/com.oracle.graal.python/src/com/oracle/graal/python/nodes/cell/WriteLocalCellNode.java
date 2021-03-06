/*
 * Copyright (c) 2017, 2018, Oracle and/or its affiliates.
 * Copyright (c) 2013, Regents of the University of California
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided
 * with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.oracle.graal.python.nodes.cell;

import static com.oracle.graal.python.builtins.objects.PNone.NO_VALUE;

import com.oracle.graal.python.builtins.objects.cell.PCell;
import com.oracle.graal.python.nodes.PNode;
import com.oracle.graal.python.nodes.frame.ReadLocalVariableNode;
import com.oracle.graal.python.nodes.frame.WriteIdentifierNode;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "write_cell")
@NodeChildren({@NodeChild(value = "rhs", type = PNode.class)})
public abstract class WriteLocalCellNode extends PNode implements WriteIdentifierNode {
    @Child private PNode readLocal;

    private final FrameSlot frameSlot;

    WriteLocalCellNode(FrameSlot frameSlot) {
        this.frameSlot = frameSlot;
        this.readLocal = ReadLocalVariableNode.create(this.frameSlot);
    }

    public static PNode create(FrameSlot frameSlot, PNode right) {
        return WriteLocalCellNodeGen.create(frameSlot, right);
    }

    @Override
    public Object doWrite(VirtualFrame frame, Object value) {
        return executeWithValue(frame, value);
    }

    public abstract Object executeWithValue(VirtualFrame frame, Object value);

    @Specialization
    Object writeObject(VirtualFrame frame, Object value) {
        Object localValue = readLocal.execute(frame);
        if (localValue instanceof PCell) {
            PCell cell = (PCell) localValue;
            if (value == NO_VALUE) {
                cell.clearRef();
            } else {
                cell.setRef(value);
            }
            return value;
        }
        CompilerDirectives.transferToInterpreter();
        throw new IllegalStateException("Expected a cell, got: " + localValue.toString() + " instead.");
    }

    @Override
    public Object getIdentifier() {
        return frameSlot.getIdentifier();
    }
}
