package ru.ddc.springws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ddc.springws.entity.Article;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByTitleAndCategory(String title, String category);
}
