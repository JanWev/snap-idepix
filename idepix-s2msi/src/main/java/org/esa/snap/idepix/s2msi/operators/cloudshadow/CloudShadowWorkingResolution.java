package org.esa.snap.idepix.s2msi.operators.cloudshadow;

enum CloudShadowWorkingResolution {
    FIXED_60M, TWENTY_METRES_OR_INPUT;
    int getWorkingResolution(int inputResolution) {
        return this == FIXED_60M ? 60 : (inputResolution == 10 ? 20 : inputResolution);
    }
}
