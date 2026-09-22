package org.esa.snap.idepix.s2msi.operators.cloudshadow;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CloudGapRecoveryTest {

    @Test
    public void requiresExplicitOptIn() {
        assertFalse(S2IdepixPostCloudShadowOp.shouldRunCloudGapRecovery(false, 10, 101, 1000, 1000));
        assertTrue(S2IdepixPostCloudShadowOp.shouldRunCloudGapRecovery(true, 10, 101, 1000, 1000));
    }

    @Test
    public void requiresAValidOffsetAndSufficientSourceArea() {
        assertFalse(S2IdepixPostCloudShadowOp.shouldRunCloudGapRecovery(true, 0, 101, 1000, 1000));
        assertFalse(S2IdepixPostCloudShadowOp.shouldRunCloudGapRecovery(true, 10, 101, 101, 1000));
    }
}
