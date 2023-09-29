package ru.ddc.springws.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ddc.springws.entity.Article;
import ru.ddc.springws.repository.ArticleRepository;

import java.util.List;

@Service
public class ArticleService implements IArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public Article getArticleById(long articleId) {
        return articleRepository.findById(articleId).orElse(null);
    }

    @Override
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    @Override
    public synchronized boolean addArticle(Article article) {
        List<Article> list = articleRepository.findByTitleAndCategory(article.getTitle(), article.getCategory());
        if (!list.isEmpty()) {
            return false;
        } else {
            article = articleRepository.save(article);
            return true;
        }
    }

    @Override
    public void updateArticle(Article article) {
        articleRepository.save(article);
    }

    @Override
    public void deleteArticle(Article article) {
        articleRepository.delete(article);
    }
}
