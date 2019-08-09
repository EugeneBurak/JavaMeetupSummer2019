package com.eugeneburak.trycatch.repository;

import com.eugeneburak.trycatch.model.TryCatchUserPoint;
import java.util.List;
import java.util.Optional;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

@EnableScan
@Repository
@RepositoryRestResource(path = "points", collectionResourceRel = "tryCatchUserPoints")
public interface TryCatchUserPointRepository extends CrudRepository<TryCatchUserPoint, String> {

  Optional<TryCatchUserPoint> findByOrderId(String orderId);

  List<TryCatchUserPoint> findAllByConfirmedIsTrue();

  @Override
  @RestResource(exported = false)
  List<TryCatchUserPoint> findAll();
}
