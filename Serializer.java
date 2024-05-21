public interface Serializer<T> {
    String serialize(T message);
    T deserialize(String serializedMessage);
}