package com.spring.springbootdeveloper.Controller;

import com.spring.springbootdeveloper.domain.Article;
import com.spring.springbootdeveloper.dto.AddArticleRequest;
import com.spring.springbootdeveloper.dto.ArticleResponse;
import com.spring.springbootdeveloper.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BlogApiController {
    private final BlogService blogService;

    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {
        Article savedArticle = blogService.save(request);

        //요청한 자원이 성공적으로 생성되었으며 저장된 블로그 글 정보를 응답 객체에 담아 전송
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedArticle);
    }

    @GetMapping("/api/articles{id}")
    public ResponseEntity<List<ArticleResponse>> findAllArticles(@PathVariable Long id){
        List<ArticleResponse> articles=blogService.findAll()
                .stream()
                .map(ArticleResponse::new)
                .toList();
        return ResponseEntity.ok()
                .body(articles);
    }



}
