package server;

@FunctionalInterface
public interface Callback_T_U<T, U> {
	void call(T t, U u);
}
