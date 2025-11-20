package server;

@FunctionalInterface
public interface Callback_T<T> {
	void call(T t);
}