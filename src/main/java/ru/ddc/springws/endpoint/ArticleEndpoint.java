package ru.ddc.springws.endpoint;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.ddc.springws.entity.Article;
import ru.ddc.springws.dto.AddArticleRequest;
import ru.ddc.springws.dto.AddArticleResponse;
import ru.ddc.springws.dto.ArticleInfo;
import ru.ddc.springws.dto.DeleteArticleRequest;
import ru.ddc.springws.dto.DeleteArticleResponse;
import ru.ddc.springws.dto.GetAllArticlesResponse;
import ru.ddc.springws.dto.GetArticleByIdRequest;
import ru.ddc.springws.dto.GetArticleByIdResponse;
import ru.ddc.springws.dto.ServiceStatus;
import ru.ddc.springws.dto.UpdateArticleRequest;
import ru.ddc.springws.dto.UpdateArticleResponse;
import ru.ddc.springws.service.IArticleService;

@Endpoint
public class ArticleEndpoint {
    private static final String NAMESPACE_URI = "ddc.ru/article-ws";
    @Autowired
    private IArticleService articleService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getArticleByIdRequest")
    @ResponsePayload
    public GetArticleByIdResponse getArticle(@RequestPayload GetArticleByIdRequest request) {
        GetArticleByIdResponse response = new GetArticleByIdResponse();
        ArticleInfo articleInfo = new ArticleInfo();
        BeanUtils.copyProperties(articleService.getArticleById(request.getArticleId()), articleInfo);
        response.setArticleInfo(articleInfo);
        return response;
    }
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllArticlesRequest")
    @ResponsePayload
    public GetAllArticlesResponse getAllArticles() {
        GetAllArticlesResponse response = new GetAllArticlesResponse();
        List<ArticleInfo> articleInfoList = new ArrayList<>();
        List<Article> articleList = articleService.getAllArticles();
        for (Article article : articleList) {
            ArticleInfo ob = new ArticleInfo();
            BeanUtils.copyProperties(article, ob);
            articleInfoList.add(ob);
        }
        response.getArticleInfo().addAll(articleInfoList);
        return response;
    }
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addArticleRequest")
    @ResponsePayload
    public AddArticleResponse addArticle(@RequestPayload AddArticleRequest request) {
        AddArticleResponse response = new AddArticleResponse();
        ServiceStatus serviceStatus = new ServiceStatus();
        Article article = new Article();
        article.setTitle(request.getTitle());
        article.setCategory(request.getCategory());
        boolean flag = articleService.addArticle(article);
        if (!flag) {
            serviceStatus.setStatusCode("CONFLICT");
            serviceStatus.setMessage("Content Already Available");
            response.setServiceStatus(serviceStatus);
        } else {
            ArticleInfo articleInfo = new ArticleInfo();
            BeanUtils.copyProperties(article, articleInfo);
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
        Article article = new Article();
        BeanUtils.copyProperties(request.getArticleInfo(), article);
        articleService.updateArticle(article);
        ServiceStatus serviceStatus = new ServiceStatus();
        serviceStatus.setStatusCode("SUCCESS");
        serviceStatus.setMessage("Content Updated Successfully");
        UpdateArticleResponse response = new UpdateArticleResponse();
        response.setServiceStatus(serviceStatus);
        return response;
    }
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteArticleRequest")
    @ResponsePayload
    public DeleteArticleResponse deleteArticle(@RequestPayload DeleteArticleRequest request) {
        Article article = articleService.getArticleById(request.getArticleId());
        ServiceStatus serviceStatus = new ServiceStatus();
        if (article == null ) {
            serviceStatus.setStatusCode("FAIL");
            serviceStatus.setMessage("Content Not Available");
        } else {
            articleService.deleteArticle(article);
            serviceStatus.setStatusCode("SUCCESS");
            serviceStatus.setMessage("Content Deleted Successfully");
        }
        DeleteArticleResponse response = new DeleteArticleResponse();
        response.setServiceStatus(serviceStatus);
        return response;
    }
}
