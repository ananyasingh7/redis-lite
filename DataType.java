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

    public static DataType getDataType(String message) {
        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Invalid message");
        }
        char prefix = message.charAt(0);
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
