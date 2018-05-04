package guru.springframework.controllers;

import guru.springframework.DTO.Pager;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.pretty.MessageHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Created by jt on 6/1/17.
 */
@Slf4j
@Controller
public class IndexController {

    private static final int INDIVIDUAL_PAGE_ELEMENT_COUNT = 1;

    private final RecipeService recipeService;

    public IndexController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping({"", "/", "/index"})
    public String getIndexPage(@RequestParam("pageSize") Optional<Integer> pageSize, @RequestParam("page") Optional<Integer> page,  Model model)
    {
        log.debug("Getting Index page");

        int calculatedPageSize = pageSize.orElse(INDIVIDUAL_PAGE_ELEMENT_COUNT);
        int calculatedPageNumber = (page.orElse(0) < 1) ? 0 : page.get() - 1;

        Page<Recipe> recipeList = recipeService.getPaginatedRecipes(PageRequest.of(calculatedPageNumber, calculatedPageSize));
        Pager pager = new Pager(recipeList.getTotalPages(), recipeList.getNumber());

        // To be used for unpaginated recipe list
        //model.addAttribute("recipes", recipeService.getRecipes());

        model.addAttribute("recipes", recipeList);
        model.addAttribute("pager", pager);

        return "index";
    }
}
