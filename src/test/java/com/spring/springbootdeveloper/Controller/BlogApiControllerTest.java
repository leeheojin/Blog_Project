package com.spring.springbootdeveloper.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.springbootdeveloper.domain.Article;
import com.spring.springbootdeveloper.dto.AddArticleRequest;
import com.spring.springbootdeveloper.dto.UpdateArticleRequest;
import com.spring.springbootdeveloper.repository.BlogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BlogApiControllerTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    BlogRepository blogRepository;

    @BeforeEach
    public void mockMvcSetup(){
        this.mockMvc= MockMvcBuilders.webAppContextSetup(context)
                .build();
        blogRepository.deleteAll();
    }

    @DisplayName("addArticle : 블로그 글 추가에 성공한다.")
    @Test
    public void addArticle() throws Exception{
        final String url ="/api/articles";
        final String title="test title";
        final String content="test content";
        final AddArticleRequest userRequest = new AddArticleRequest(title,content);

        //객체 json으로 직렬화
        final String requestBody=objectMapper.writeValueAsString(userRequest);

        ResultActions result=mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        result.andExpect(status().isCreated());

        List<Article> articles=blogRepository.findAll();

        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);
    }



    @DisplayName("findAllArtices: 블로그 글 목록 조회에 성공한다.")
    @Test
    public void findAllArticles() throws Exception{
        final String url ="/api/articles";
        final String title="test title";
        final String content="test content";

        blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        final ResultActions resultActions=mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(content))
                .andExpect(jsonPath("$[0].title").value(title));
    }

    @DisplayName("findArticle: 블로그 글 조회에 성공한다.")
    @Test
    public void findArticle() throws Exception{
        //given
        final String url ="/api/articles/{id}";
        final String title="test title";
        final String content="test content";

        Article savedArticle= blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());
        //when
        final ResultActions resultActions=mockMvc.perform(get(url,savedArticle.getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.title").value(title));
    }

    @DisplayName("deletedArticle: 블로그 글 삭제에 성공한다.")
    @Test
    public void deleteArticle() throws Exception{
        //given
        final String url ="/api/articles/{id}";
        final String title="test title";
        final String content="test content";

        Article article=blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        //when
        mockMvc.perform(delete(url,article.getId()))
                .andExpect(status().isOk());

        //then
        List<Article> articles=blogRepository.findAll();

        assertThat(articles).isEmpty();
    }

    @DisplayName("updateArticle: 블로그 글 수정에 설공한다.")
    @Test
    public void updateArticle() throws Exception{
        //given
        final String url ="/api/articles/{id}";
        final String title="test title";
        final String content="test content";

        Article savedArticle=blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        final String newTitle="new title";
        final String newContent="new content";

        UpdateArticleRequest request = new UpdateArticleRequest(newTitle,newContent);
        //when
        ResultActions result=mockMvc.perform(put(url,savedArticle.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result.andExpect(status().isOk());

        Article article=blogRepository.findById(savedArticle.getId()).get();

        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
    }
}