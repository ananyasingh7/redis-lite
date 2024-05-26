public class RESPSerializer<T> implements Serializer<T> {
    @Override
    public String serialize(T message) {
        if (message == null) {
            return "$-1\\r\\n";
        }

        DataType dataType = getDataType(message);
        switch (dataType) {
            case SIMPLE_STRING:
                return "+" + message + "\\r\\n";
            case ERROR:
                break;
            case INTEGER:
                break;
            case BULK_STRING:
                break;
            case ARRAY:
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
                return (T) serializedMessage.substring(1, serializedMessage.length() - 4);
            case ERROR:
                break;
            case INTEGER:
                break;
            case BULK_STRING:
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
        if (message instanceof String) {
            String str = (String) message;
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