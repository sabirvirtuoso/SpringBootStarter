package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private RecipeRepository recipeRepository;
    private IngredientToIngredientCommand ingredientToIngredientCommand;
    private IngredientCommandToIngredient ingredientCommandToIngredient;
    private UnitOfMeasureRepository unitOfMeasureRepository;

    public IngredientServiceImpl(RecipeRepository recipeRepository, IngredientToIngredientCommand ingredientConverter,
                                 IngredientCommandToIngredient ingredientCommandToIngredient, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientToIngredientCommand = ingredientConverter;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long id) {
        Optional<Recipe> recipeForIngredient = recipeRepository.findById(recipeId);

        if (!recipeForIngredient.isPresent()) {
            log.debug("Recipe Not present for id:" + recipeId);
        }

        Recipe recipe = recipeForIngredient.get();

        Optional<IngredientCommand> ingredientCommandOptional = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(id))
                .map(ingredient -> ingredientToIngredientCommand.convert(ingredient)).findFirst();

        if (!ingredientCommandOptional.isPresent()) {
            log.debug("Ingredient not present for id:" + id);
        }

        return ingredientCommandOptional.get();
    }

    @Override
    @Transactional
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(command.getRecipeId());

        if (!optionalRecipe.isPresent()) {
            log.debug("No recipe present for id:" + command.getRecipeId());

            return new IngredientCommand();
        }

        Recipe recipe = optionalRecipe.get();

        Optional<Ingredient> optionalIngredient = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(command.getId()))
                .findFirst();


        if (optionalIngredient.isPresent()) {
            Ingredient ingredient = optionalIngredient.get();
            ingredient.setDescription(command.getDescription());
            ingredient.setAmount(command.getAmount());
            ingredient.setUom(unitOfMeasureRepository.findById(command.getUnitOfMeasure().getId()).orElseThrow(() -> new RuntimeException("Unit Of Measure Not found")));
        } else {
            Ingredient ingredient = ingredientCommandToIngredient.convert(command);
            ingredient.setRecipe(recipe);

            recipe.addIngredient(ingredient);
        }

        Recipe savedRecipe = recipeRepository.save(recipe);

        Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
                .filter(recipeIngredients -> recipeIngredients.getId().equals(command.getId()))
                .findFirst();

        if(!savedIngredientOptional.isPresent()){
            //not totally safe... But best guess
            savedIngredientOptional = savedRecipe.getIngredients().stream()
                    .filter(recipeIngredients -> recipeIngredients.getDescription().equals(command.getDescription()))
                    .filter(recipeIngredients -> recipeIngredients.getAmount().equals(command.getAmount()))
                    .filter(recipeIngredients -> recipeIngredients.getUom().getId().equals(command.getUnitOfMeasure().getId()))
                    .findFirst();
        }

        return ingredientToIngredientCommand.convert(savedIngredientOptional.get());
    }
}
