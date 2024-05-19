public enum DataType {
    SIMPLE_STRING('+'),
    ERROR('-'),
    INTEGER(':'),
    BULK_STRING('$'),
    ARRAY('*');

    private final char prefix;

    DataType(char prefix) {
        this.prefix = prefix;
    }

    public static DataType getDataType(char prefix) {
        for (DataType dataType : DataType.values()) {
            if (dataType.prefix == prefix) {
                return dataType;
            }
        }
        throw new IllegalArgumentException("Invalid prefix: " + prefix);
    }

    public char getPrefix() {
        return prefix;
    }
}
