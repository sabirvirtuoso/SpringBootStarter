package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.services.ImageService;
import guru.springframework.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
@Slf4j
public class ImageController {

    private RecipeService recipeService;
    private ImageService imageService;

    public ImageController(RecipeService recipeService, ImageService imageService) {
        this.recipeService = recipeService;
        this.imageService = imageService;
    }

    @GetMapping("/recipe/{recipeId}/image")
    public String showUploadForm(@PathVariable String recipeId, Model model) {
        RecipeCommand recipe = recipeService.findCommandById(new Long(recipeId));

        model.addAttribute("recipe", recipe);

        return "recipe/imageUploadForm";
    }

    @PostMapping("/recipe/{recipeId}/image")
    public String saveImage(@PathVariable String recipeId, @RequestParam("imagefile") MultipartFile file) {
        imageService.saveImageFile(new Long(recipeId), file);

        return "redirect:/recipe/" + recipeId + "/show";
    }


    @GetMapping("/recipe/{recipeId}/recipeImage")
    public void renderImageFromDB(@PathVariable String recipeId, HttpServletResponse response) throws IOException {
        RecipeCommand recipeCommand = recipeService.findCommandById(new Long(recipeId));

        byte[] bytesObject = new byte[recipeCommand.getImage().length];

        int i = 0;

        for (Byte b : recipeCommand.getImage()) {
            bytesObject[i++] = b;
        }

        response.setContentType("image/jpeg");

        InputStream inputStream = new ByteArrayInputStream(bytesObject);
        IOUtils.copy(inputStream, response.getOutputStream());
    }
}
