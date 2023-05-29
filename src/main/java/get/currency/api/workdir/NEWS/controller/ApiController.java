package get.currency.api.workdir.NEWS.controller;

import get.currency.api.workdir.NEWS.service.ArticleServiceForUpdateDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiController {
    private ArticleServiceForUpdateDB articleService;

    @Autowired
    public ApiController(ArticleServiceForUpdateDB articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/apiKey")
    @PreAuthorize("hasAuthority('developers:write')")
    public String getApiKey() {
        return articleService.getApiKey();
    }

    @PostMapping("/apiKey")
    @PreAuthorize("hasAuthority('developers:write')")
    public void setApiKey(@RequestParam("apiKey") String apiKey) {
        articleService.setApiKey(apiKey);
    }
}