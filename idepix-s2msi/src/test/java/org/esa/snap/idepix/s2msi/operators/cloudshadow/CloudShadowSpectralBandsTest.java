package org.esa.snap.idepix.s2msi.operators.cloudshadow;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CloudShadowSpectralBandsTest {

    @Test
    public void mapsEstablishedIdepixPair() {
        assertEquals("B8A", CloudShadowSpectralBands.B8A_B3.getBandName(0));
        assertEquals("B3", CloudShadowSpectralBands.B8A_B3.getBandName(1));
    }

    @Test
    public void mapsFmaskInspiredNirSwir1Pair() {
        assertEquals("B8", CloudShadowSpectralBands.B8_B11.getBandName(0));
        assertEquals("B11", CloudShadowSpectralBands.B8_B11.getBandName(1));
    }
}
