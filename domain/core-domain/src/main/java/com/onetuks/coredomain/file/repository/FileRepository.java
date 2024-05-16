package com.onetuks.coredomain.file.repository;

import com.onetuks.coreobj.file.FileWrapper;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository {

  void putFile(FileWrapper file);

  void deleteFile(String uri);
}
