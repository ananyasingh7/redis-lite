public class RESPSerializer<T> implements Serializer<T> {
    @Override
    public String serialize(T message) {
        if (message == null) {
            return "$-1\r\n";
        }

        DataType dataType = getDataType(message);
        switch (dataType) {
            case SIMPLE_STRING:
                // Implement serialization for SIMPLE_STRING
                break;
            case ERROR:
                // Implement serialization for ERROR
                break;
            case INTEGER:
                // Implement serialization for INTEGER
                break;
            case BULK_STRING:
                // Implement serialization for BULK_STRING
                break;
            case ARRAY:
                // Implement serialization for ARRAY
                break;
            default:
                throw new IllegalArgumentException("Unsupported data type: " + dataType);
        }

        // Return the serialized message
        return null;
    }

    @Override
    public T deserialize(String serializedMessage) {
        if (serializedMessage == null || serializedMessage.isEmpty()) {
            throw new IllegalArgumentException("Invalid serialized message");
        }

        char prefix = serializedMessage.charAt(0);
        DataType dataType = DataType.getDataType(prefix);

        switch (dataType) {
            case SIMPLE_STRING:
                // Implement deserialization for SIMPLE_STRING
                break;
            case ERROR:
                // Implement deserialization for ERROR
                break;
            case INTEGER:
                // Implement deserialization for INTEGER
                break;
            case BULK_STRING:
                // Implement deserialization for BULK_STRING
                break;
            case ARRAY:
                // Implement deserialization for ARRAY
                break;
            default:
                throw new IllegalArgumentException("Unsupported data type: " + dataType);
        }

        // Return the deserialized object
        return null;
    }

    private DataType getDataType(T message) {
        if (message instanceof String str) {
            if (str.startsWith("+")) {
                return DataType.SIMPLE_STRING;
            } else if (str.startsWith("-")) {
                return DataType.ERROR;
            } else if (str.startsWith(":")) {
                return DataType.INTEGER;
            } else {
                return DataType.BULK_STRING;
            }
        } else if (message instanceof Integer) {
            return DataType.INTEGER;
        } else {
            // Add more conditions for other data types if needed
            throw new IllegalArgumentException("Unsupported data type: " + message.getClass());
        }
    }
}