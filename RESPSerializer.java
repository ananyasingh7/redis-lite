import java.util.ArrayList;
import java.util.List;

public class RESPSerializer<T> implements Serializer<T> {
    @Override
    public String serialize(Object message) {
        if (message == null) {
            return "$-1\\r\\n";
        }

        DataType dataType = getDataType(message);
        switch (dataType) {
            case SIMPLE_STRING:
                return "+" + message + "\\r\\n";
            case ERROR:
                return "-" + message + "\\r\\n";
            case INTEGER:
                return ":" + message + "\\r\\n";
            case BULK_STRING:
                return "$" + ((String) message).length() + "\\r\\n" + message + "\\r\\n";
            case ARRAY:
                if (!(message instanceof List<?> list)) {
                    throw new IllegalArgumentException("Must be a List");
                }
                StringBuilder builder = new StringBuilder();
                builder.append("*").append(list.size()).append("\\r\\n");
                for(Object item: list) {
                    String serializedItem = serialize(item);
                    builder.append(serializedItem);
                }
                return builder.toString();
            default:
                throw new IllegalArgumentException("Unsupported data type: " + dataType);
        }
    }

    @Override
    public T deserialize(String serializedMessage) {
        if (serializedMessage == null || serializedMessage.isEmpty()) {
            throw new IllegalArgumentException("Invalid serialized message");
        }

        String unescapedMessage = serializedMessage.replace("\\r\\n", "\r\n");

        DataType dataType = DataType.getDataType(unescapedMessage);

        switch (dataType) {
            case SIMPLE_STRING:
                return (T) unescapedMessage.substring(1, unescapedMessage.length() - 2);
            case ERROR:
                return (T) unescapedMessage.substring(1, unescapedMessage.length() - 2);
            case INTEGER:
                return (T) Integer.valueOf(unescapedMessage.substring(1, unescapedMessage.length() - 2));
            case BULK_STRING:
                int lengthEnd = unescapedMessage.indexOf("\r\n");
                if (lengthEnd == -1) {
                    throw new IllegalArgumentException("Invalid bulk string format: missing \\r\\n after length");
                }
                int length = Integer.parseInt(unescapedMessage.substring(1, lengthEnd));
                if (length == -1) {
                    return null; // Null bulk string
                }
                return (T) unescapedMessage.substring(lengthEnd + 2, unescapedMessage.length() - 2);
            case ARRAY:
                List<Object> result = new ArrayList<>();
                String remaining = unescapedMessage.substring(1);
                int count = Integer.parseInt(remaining.substring(0, remaining.indexOf("\r\n")));
                remaining = remaining.substring(remaining.indexOf("\r\n") + 2);
                for (int i = 0; i < count; i++) {
                    int nextNewline = remaining.indexOf("\r\n");
                    if (nextNewline == -1) {
                        throw new IllegalArgumentException("Invalid array element: missing \\r\\n");
                    }
                    String element = remaining.substring(0, nextNewline + 2);
                    result.add(deserialize(element));
                    remaining = remaining.substring(nextNewline + 2);
                }
                return (T) result;
            default:
                throw new IllegalArgumentException("Unsupported data type: " + dataType);
        }
    }

    private DataType getDataType(Object message) {
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