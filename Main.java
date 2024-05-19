public class Main {
    public static void main(String[] args) {
        Serializer serializer = new RESPSerializer();

        // Test cases for serialization and deserialization
        String[] testCases = {
                "$-1\r\n",
                "*1\r\n$4\r\nping\r\n",
                "*2\r\n$4\r\necho\r\n$11\r\nhello world\r\n",
                "*2\r\n$3\r\nget\r\n$3\r\nkey\r\n",
                "+OK\r\n",
                "-Error message\r\n",
                "$0\r\n\r\n",
                "+hello world\r\n"
        };

        for (String testCase : testCases) {
            System.out.println("Test Case: " + testCase);
            String deserialized = serializer.deserialize(testCase);
            System.out.println("Deserialized: " + deserialized);
            String serialized = serializer.serialize(deserialized);
            System.out.println("Serialized: " + serialized);
            System.out.println();
        }

        // Test case for invalid serialized message format
        try {
            serializer.deserialize("invalid message");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid serialized message format: " + e.getMessage());
        }
    }
}