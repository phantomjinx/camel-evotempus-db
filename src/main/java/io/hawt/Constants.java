package io.hawt;

public interface Constants {

    String RAW_DEST_DIR = "/home/phantomjinx/evotempus/raw";

    String ENHANCED_DEST_DIR = "/home/phantomjinx/evotempus/enhanced";

    String GEOLOGICAL_TIMESCALE = "Geological Timescale";

    String INTERVALS = "intervals";

    String SUBJECTS = "subjects";

    enum Interval {
        ROOT("Root"),
        SUPER_EON("SuperEon"),
        EON("Eon"),
        ERA("Era"),
        PERIOD("Period"),
        SUB_PERIOD("Sub-Period"),
        EPOCH("Epoch"),
        AGE("Age");

        private String id;

        Interval(String id) {
            this.id = id;
        }

        public static Interval interval(String id) {
            for (Interval iv : Interval.values()) {
                if (iv.id.equalsIgnoreCase(id))
                    return iv;
            }

            throw new IllegalStateException("No Interval Enum with id " + id);
        }
    }
}
