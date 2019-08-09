package com.eugeneburak.trycatch.repository;

import com.eugeneburak.trycatch.model.TryCatchUser;
import java.util.List;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

@EnableScan
@EnableScanCount
@Repository
@RepositoryRestResource(path = "users", collectionResourceRel = "tryCatchUsers")
public interface TryCatchUserRepository extends PagingAndSortingRepository<TryCatchUser, String> {

  @RestResource(path = "clientid", rel = "findByClientId")
  List<TryCatchUser> findByClientId(String key);

  TryCatchUser findFirstById(String id);

  @Override
  @RestResource(exported = false)
  Page<TryCatchUser> findAll(Pageable pageable);
}
