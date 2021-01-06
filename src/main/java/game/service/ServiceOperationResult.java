package game.service;

public class ServiceOperationResult<T> {

    private final boolean isSuccess;

    private T target;
    private String errorMessage;
    private Enum<?> errorEnum;

    private ServiceOperationResult(T _target) {
        this.target = _target;
        this.isSuccess = true;
    }

    private ServiceOperationResult(String _errorMessage) {
        this.isSuccess = false;
        this.errorMessage = _errorMessage;
    }

    private ServiceOperationResult(Enum<?> _errorEnum) {
        this.isSuccess = false;
        this.errorEnum = _errorEnum;
    }

    public T target() {
        return this.target;
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }

    public String errorMessage() {
        return this.errorMessage;
    }

    public boolean errorEnumEquals(Enum<?> _requestedEnum) {
        return _requestedEnum == this.errorEnum;
    }

    public String errorEnumValue() {
        return this.errorEnum.toString();
    }

    static public <T> ServiceOperationResult<T> success(T _target) {
        return new ServiceOperationResult<T>(_target);
    }

    static public <T> ServiceOperationResult<T> failAsString(String _errorMessage) {
        return new ServiceOperationResult<T>(_errorMessage);
    }

    static public <T> ServiceOperationResult<T> failAsEnum(Enum<?> _errorEnum) {
        return new ServiceOperationResult<T>(_errorEnum);
    }
}
