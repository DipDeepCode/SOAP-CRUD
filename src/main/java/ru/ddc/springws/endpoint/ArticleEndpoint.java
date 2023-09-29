package ru.ddc.springws.endpoint;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.ddc.springws.dto.*;
import ru.ddc.springws.entity.Article;
import ru.ddc.springws.service.IArticleService;

@Endpoint
public class ArticleEndpoint {
    private static final String NAMESPACE_URI = "ddc.ru/spring-ws";
    private final IArticleService articleService;
    private final ObjectFactory objectFactory;
    private final ModelMapper mapper;

    public ArticleEndpoint(IArticleService articleService,
                           ObjectFactory objectFactory,
                           ModelMapper mapper) {
        this.articleService = articleService;
        this.objectFactory = objectFactory;
        this.mapper = mapper;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getArticleByIdRequest")
    @ResponsePayload
    public GetArticleByIdResponse getArticle(@RequestPayload GetArticleByIdRequest request) {
        GetArticleByIdResponse response = objectFactory.createGetArticleByIdResponse();
        Article article = articleService.getArticleById(request.getArticleId());
        ArticleInfo articleInfo = mapper.map(article, ArticleInfo.class);
        response.setArticleInfo(articleInfo);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllArticlesRequest")
    @ResponsePayload
    public GetAllArticlesResponse getAllArticles() {
        GetAllArticlesResponse response = objectFactory.createGetAllArticlesResponse();
        List<ArticleInfo> articleInfoList = new ArrayList<>();
        List<Article> articleList = articleService.getAllArticles();
        for (Article article : articleList) {
            ArticleInfo articleInfo = mapper.map(article, ArticleInfo.class);
            articleInfoList.add(articleInfo);
        }
        response.getArticleInfo().addAll(articleInfoList);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addArticleRequest")
    @ResponsePayload
    public AddArticleResponse addArticle(@RequestPayload AddArticleRequest request) {
        AddArticleResponse response = objectFactory.createAddArticleResponse();
        ServiceStatus serviceStatus = objectFactory.createServiceStatus();
        Article article = mapper.map(request, Article.class);
        boolean flag = articleService.addArticle(article);
        if (!flag) {
            serviceStatus.setStatusCode("CONFLICT");
            serviceStatus.setMessage("Content Already Available");
            response.setServiceStatus(serviceStatus);
        } else {
            ArticleInfo articleInfo = mapper.map(article, ArticleInfo.class);
            response.setArticleInfo(articleInfo);
            serviceStatus.setStatusCode("SUCCESS");
            serviceStatus.setMessage("Content Added Successfully");
            response.setServiceStatus(serviceStatus);
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateArticleRequest")
    @ResponsePayload
    public UpdateArticleResponse updateArticle(@RequestPayload UpdateArticleRequest request) {
        Article article = articleService.getArticleById(request.getArticleInfo().getArticleId());
        ServiceStatus serviceStatus = objectFactory.createServiceStatus();
        if (article == null) {
            serviceStatus.setStatusCode("FAIL");
            serviceStatus.setMessage("Content Not Available");
        } else {
            articleService.updateArticle(mapper.map(request.getArticleInfo(), Article.class));
            serviceStatus.setStatusCode("SUCCESS");
            serviceStatus.setMessage("Content Updated Successfully");
        }
        UpdateArticleResponse response = objectFactory.createUpdateArticleResponse();
        response.setServiceStatus(serviceStatus);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteArticleRequest")
    @ResponsePayload
    public DeleteArticleResponse deleteArticle(@RequestPayload DeleteArticleRequest request) {
        Article article = articleService.getArticleById(request.getArticleId());
        ServiceStatus serviceStatus = objectFactory.createServiceStatus();
        if (article == null) {
            serviceStatus.setStatusCode("FAIL");
            serviceStatus.setMessage("Content Not Available");
        } else {
            articleService.deleteArticle(article);
            serviceStatus.setStatusCode("SUCCESS");
            serviceStatus.setMessage("Content Deleted Successfully");
        }
        DeleteArticleResponse response = objectFactory.createDeleteArticleResponse();
        response.setServiceStatus(serviceStatus);
        return response;
    }
}
