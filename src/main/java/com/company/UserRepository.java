package com.company;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


import java.util.List;

/**
 * Created by lukoyanov on 8/17/15.
 */

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByNameLike(@Param("name") String name);

}
