package guru.springframework.services;

import jdk.nashorn.internal.runtime.options.Option;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CRUDService<T> {

    List<T> listAll();

    Optional<T> getById(Long id);

    T saveOrUpdate(T domainObject);

    void delete(Long id);

}
