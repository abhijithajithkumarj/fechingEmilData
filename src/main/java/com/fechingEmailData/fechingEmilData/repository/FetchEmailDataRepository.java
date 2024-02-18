package com.fechingEmailData.fechingEmilData.repository;

import com.fechingEmailData.fechingEmilData.entity.FetchEmailData;
import org.springframework.data.mongodb.core.MongoAdminOperations;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FetchEmailDataRepository extends MongoRepository<FetchEmailData,String> {
}
