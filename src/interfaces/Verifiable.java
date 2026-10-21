package interfaces;

public interface Verifiable {
    String generateCode(String requestId, String ownerId);
    boolean verify(String requestId, String borrowerId, String code);
}
