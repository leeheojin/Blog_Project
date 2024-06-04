package com.spring.springbootdeveloper.service;

import com.spring.springbootdeveloper.domain.Article;
import com.spring.springbootdeveloper.dto.AddArticleRequest;
import com.spring.springbootdeveloper.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BlogService {
    private final BlogRepository blogRepository;

    public Article save(AddArticleRequest request){
        return blogRepository.save(request.toEntity());
    }

    public List<Article> findAll(){
        return blogRepository.findAll();
    }

    public Article findById(Long id){
        return blogRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("not Found: "+id));
    }

    public void delete(Long id){
        blogRepository.deleteById(id);
    }
    
}
