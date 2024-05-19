public interface Serializer {
    String serialize(String message);
    String deserialize(String serializedMessage);
}
