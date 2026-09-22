package org.esa.snap.idepix.s2msi.operators.cloudshadow;

/**
 * Spectral pairs that can provide the dark-pixel evidence inside the
 * geometry-derived potential-shadow corridor.
 */
enum CloudShadowSpectralBands {
    B8A_B3("B8A", "B3"),
    B8_B11("B8", "B11"),
    FMASK_FILL_DEPTH("B8", "B11");

    private final String firstBand;
    private final String secondBand;

    CloudShadowSpectralBands(String firstBand, String secondBand) {
        this.firstBand = firstBand;
        this.secondBand = secondBand;
    }

    static CloudShadowSpectralBands fromParameter(String parameterValue) {
        return valueOf(parameterValue);
    }

    String getBandName(int index) {
        if (index == 0) {
            return firstBand;
        }
        if (index == 1) {
            return secondBand;
        }
        throw new IllegalArgumentException("Spectral-pair index must be 0 or 1: " + index);
    }

    boolean usesFmaskFillDepth() {
        return this == FMASK_FILL_DEPTH;
    }
}
