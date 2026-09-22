package org.esa.snap.idepix.s2msi.operators.cloudshadow;

import java.util.Arrays;
import java.util.PriorityQueue;

/** Fmask-style regional-minimum fill-depth evidence, evaluated on the 60 m shadow grid. */
final class FmaskFillDepth {

    private FmaskFillDepth() {
    }

    static byte[] potentialShadow(float[] nir, float[] swir1, int[] flags, int width, int height, float threshold) {
        final boolean[] clearLand = new boolean[flags.length];
        int count = 0;
        for (int i = 0; i < flags.length; i++) {
            clearLand[i] = (flags[i] & PreparationMaskBand.LAND_FLAG) != 0 &&
                    (flags[i] & (PreparationMaskBand.CLOUD_FLAG | PreparationMaskBand.INVALID_FLAG)) == 0 &&
                    Float.isFinite(nir[i]) && Float.isFinite(swir1[i]);
            if (clearLand[i]) count++;
        }
        if (count == 0) return new byte[flags.length];
        final float nirBackground = percentile(nir, clearLand, count, 0.175);
        final float swirBackground = percentile(swir1, clearLand, count, 0.175);
        final float[] nirFilled = fillRegionalMinima(nir, clearLand, width, height, nirBackground);
        final float[] swirFilled = fillRegionalMinima(swir1, clearLand, width, height, swirBackground);
        final byte[] result = new byte[flags.length];
        for (int i = 0; i < result.length; i++) {
            if (clearLand[i] && Math.min(nirFilled[i] - nir[i], swirFilled[i] - swir1[i]) > threshold) {
                result[i] = 1;
            }
        }
        return result;
    }

    private static float percentile(float[] values, boolean[] selected, int count, double fraction) {
        final float[] subset = new float[count]; int p = 0;
        for (int i = 0; i < values.length; i++) if (selected[i]) subset[p++] = values[i];
        Arrays.sort(subset);
        return subset[(int) Math.floor((subset.length - 1) * fraction)];
    }

    // Priority flooding produces the grayscale imfill surface: an enclosed dark minimum is raised to its spill level.
    private static float[] fillRegionalMinima(float[] input, boolean[] clearLand, int width, int height, float background) {
        final int n = input.length;
        final float[] filled = new float[n]; Arrays.fill(filled, Float.NaN);
        final boolean[] visited = new boolean[n];
        final PriorityQueue<Entry> queue = new PriorityQueue<>();
        for (int y = 0; y < height; y++) for (int x = 0; x < width; x++) {
            if (x == 0 || y == 0 || x == width - 1 || y == height - 1) add(x + y * width, input, clearLand, background, filled, visited, queue);
        }
        final int[] dx = {-1, 1, 0, 0}; final int[] dy = {0, 0, -1, 1};
        while (!queue.isEmpty()) {
            final Entry current = queue.remove();
            int x = current.index % width, y = current.index / width;
            for (int k = 0; k < 4; k++) {
                int nx = x + dx[k], ny = y + dy[k];
                if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                    int next = nx + ny * width;
                    if (!visited[next]) {
                        float value = clearLand[next] ? input[next] : background;
                        visited[next] = true; filled[next] = Math.max(value, current.level); queue.add(new Entry(next, filled[next]));
                    }
                }
            }
        }
        return filled;
    }

    private static void add(int index, float[] input, boolean[] clearLand, float background, float[] filled,
                            boolean[] visited, PriorityQueue<Entry> queue) {
        if (!visited[index]) { visited[index] = true; filled[index] = clearLand[index] ? input[index] : background; queue.add(new Entry(index, filled[index])); }
    }
    private record Entry(int index, float level) implements Comparable<Entry> {
        @Override public int compareTo(Entry other) { return Float.compare(level, other.level); }
    }
}
