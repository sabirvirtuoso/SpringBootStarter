package guru.springframework.repositories;

import guru.springframework.domain.Recipe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by jt on 6/13/17.
 */
public interface RecipeRepository extends PagingAndSortingRepository<Recipe, Long> {
}
