public interface Serializer<T> {
    String serialize(Object message);
    T deserialize(String serializedMessage);
}