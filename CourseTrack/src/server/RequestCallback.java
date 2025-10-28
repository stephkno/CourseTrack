package server;

@FunctionalInterface
public interface RequestCallback<T, U> {
	void call(T t, U u);
}
