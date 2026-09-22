package org.esa.snap.idepix.s2msi.operators.cloudshadow;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CloudShadowResamplingTest {

    @Test
    public void usesMeanForContinuousBandsAndOrForClassificationFlags() {
        final Map<String, Object> parameters = S2IdepixCloudShadowOp.createCloudShadowResamplingParameters();

        assertEquals("Nearest", parameters.get("upsampling"));
        assertEquals("Mean", parameters.get("downsampling"));
        assertEquals("FlagOr", parameters.get("flagDownsampling"));
        assertEquals(60, parameters.get("targetResolution"));
        assertEquals(4, parameters.size());
    }
}
