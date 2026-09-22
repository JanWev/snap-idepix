package org.esa.snap.idepix.s2msi.operators.cloudshadow;

/** Selected cloud-path offset and its evidence for one cloud. */
final class CloudShadowMatch {

    enum Source {
        SCENE_PRIOR,
        LOCAL_SEARCH
    }

    private final int cloudId;
    private final int offset;
    private final double confidence;
    private final CloudShadowMatchScore score;
    private final Source source;

    CloudShadowMatch(int cloudId, int offset, double confidence, CloudShadowMatchScore score, Source source) {
        if (offset < 0) {
            throw new IllegalArgumentException("Cloud-shadow offset must not be negative.");
        }
        if (score == null || source == null) {
            throw new IllegalArgumentException("Score and match source must not be null.");
        }
        this.cloudId = cloudId;
        this.offset = offset;
        this.confidence = confidence;
        this.score = score;
        this.source = source;
    }

    int getCloudId() {
        return cloudId;
    }

    int getOffset() {
        return offset;
    }

    double getConfidence() {
        return confidence;
    }

    CloudShadowMatchScore getScore() {
        return score;
    }

    Source getSource() {
        return source;
    }
}
