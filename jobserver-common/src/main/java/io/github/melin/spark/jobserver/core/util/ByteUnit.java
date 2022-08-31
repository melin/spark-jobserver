package io.github.melin.spark.jobserver.core.util;

/**
 * Created by libinsong on 2017/6/6.
 */
public enum ByteUnit {
    BYTE(1),
    KiB(1024L),
    MiB((long) Math.pow(1024L, 2L)),
    GiB((long) Math.pow(1024L, 3L)),
    TiB((long) Math.pow(1024L, 4L)),
    PiB((long) Math.pow(1024L, 5L));

    ByteUnit(long multiplier) {
        this.multiplier = multiplier;
    }

    // Interpret the provided number (d) with suffix (u) as this unit type.
    // E.g. KiB.interpret(1, MiB) interprets 1MiB as its KiB representation = 1024k
    public long convertFrom(long d, ByteUnit u) {
        return u.convertTo(d, this);
    }

    // Convert the provided number (d) interpreted as this unit type to unit type (u).
    public long convertTo(long d, ByteUnit u) {
        if (multiplier > u.multiplier) {
            long ratio = multiplier / u.multiplier;
            if (Long.MAX_VALUE / ratio < d) {
                throw new IllegalArgumentException("Conversion of " + d + " exceeds Long.MAX_VALUE in "
                        + name() + ". Try a larger unit (e.g. MiB instead of KiB)");
            }
            return d * ratio;
        } else {
            // Perform operations in this order to avoid potential overflow
            // when computing d * multiplier
            return d / (u.multiplier / multiplier);
        }
    }

    public double toBytes(long d) {
        if (d < 0) {
            throw new IllegalArgumentException("Negative size value. Size must be positive: " + d);
        }
        return d * multiplier;
    }

    public long toKiB(long d) {
        return convertTo(d, KiB);
    }

    public long toMiB(long d) {
        return convertTo(d, MiB);
    }

    public long toGiB(long d) {
        return convertTo(d, GiB);
    }

    public long toTiB(long d) {
        return convertTo(d, TiB);
    }

    public long toPiB(long d) {
        return convertTo(d, PiB);
    }

    private final long multiplier;
}

