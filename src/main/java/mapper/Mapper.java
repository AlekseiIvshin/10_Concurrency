package mapper;

public interface Mapper {

	<S, D> D map(S sourceObject, Class<D> destination);
}
