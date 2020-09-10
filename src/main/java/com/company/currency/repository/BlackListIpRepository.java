package com.company.currency.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.company.currency.entity.BlackListIp;

public interface BlackListIpRepository extends MongoRepository<BlackListIp, String> {
	public Optional<BlackListIp> findByIp(String ip) throws Exception;
}
