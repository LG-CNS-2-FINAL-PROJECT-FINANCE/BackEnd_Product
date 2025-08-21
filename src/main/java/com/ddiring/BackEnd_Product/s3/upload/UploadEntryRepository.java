package com.ddiring.BackEnd_Product.s3.upload;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UploadEntryRepository extends MongoRepository<UploadEntry, String> {
    List<UploadEntry> findAllByIdInAndUserSeqAndStatus(List<String> ids, String userSeq, UploadEntry.Status status);
}
