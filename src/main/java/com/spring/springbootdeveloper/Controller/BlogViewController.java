package com.spring.springbootdeveloper.Controller;



import com.spring.springbootdeveloper.domain.Article;
import com.spring.springbootdeveloper.dto.ArticleListViewResponse;
import com.spring.springbootdeveloper.dto.ArticleViewResponse;
import com.spring.springbootdeveloper.service.BlogService;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BlogViewController {
    private final BlogService blogService;

    @GetMapping("/articles")
    public String getArticles(Model model) {
        List<ArticleListViewResponse> articles=blogService.findAll().stream()
                .map(ArticleListViewResponse::new)
                .toList();
        model.addAttribute("articles",articles);

        return "articleList";
    }

    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable Long id, Model model) {
        Article article = blogService.findById(id);
        model.addAttribute("article",new ArticleViewResponse(article));
        return "article";
    }

    @GetMapping("/new-article")
    public String newArticle(@RequestParam(required = false) Long id,Model model) {
        if(id==null){// id가 없으면 새 글 생성
            model.addAttribute("article",new ArticleViewResponse());
        }else{ // id가 있으면 기존 글 수정
            Article article= blogService.findById(id);
            model.addAttribute("article",new ArticleViewResponse(article));
        }
        return "newArticle";
    }

}
