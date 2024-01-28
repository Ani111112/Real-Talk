package com.example.RealTalk.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CollectionHandler {
    @Autowired
    MongoTemplate mongoTemplate;
    public <T> Object save(T object, String...dbName) {
        if (object == null) throw new RuntimeException("Saving Object is Null");
        if (dbName != null && dbName.length > 0) {
            return mongoTemplate.save(object, dbName.toString());
        }else {
            return mongoTemplate.save(object);
        }
    }
    public <T> List<T> findAllDocuments(Class<T> classs) {
        return mongoTemplate.findAll(classs);
    }
    public <T> List<T> findDocumentByField(String fieldName, Object object, Class<T> classs) {
        Query query = new Query(Criteria.where(fieldName).is(object));
        return mongoTemplate.find(query, classs);
    }
    public <T>List<T> findDocumentsWithFieldQueries(String fieldName, List<String> messageIds, Class<T> classs) {
        Query query = new Query(Criteria.where(fieldName).in(messageIds));
        return mongoTemplate.find(query, classs);
    }
}
