package com.xmrlab.android.hid.configfs;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public final class GadgetState {

    public final boolean xmrlabGadgetExists;
    public final boolean xmrlabGadgetBound;
    public final String boundUdc;
    public final Set<GadgetFunction> linkedFunctions;
    public final String massStorageFile;

    public GadgetState(boolean xmrlabGadgetExists,
                       boolean xmrlabGadgetBound,
                       String boundUdc,
                       Set<GadgetFunction> linkedFunctions,
                       String massStorageFile) {
        this.xmrlabGadgetExists = xmrlabGadgetExists;
        this.xmrlabGadgetBound = xmrlabGadgetBound;
        this.boundUdc = boundUdc;
        this.linkedFunctions = linkedFunctions == null
                ? Collections.unmodifiableSet(EnumSet.noneOf(GadgetFunction.class))
                : Collections.unmodifiableSet(EnumSet.copyOf(linkedFunctions));
        this.massStorageFile = massStorageFile;
    }
}
