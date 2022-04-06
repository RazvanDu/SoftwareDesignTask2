/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.RazvanDu.project.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRespository extends CrudRepository<Order, Long> { // <1>
    Optional<Order> findById(Integer id);
    List<Order> findAllByUserID(Integer id);
    List<Order> findAllByRestaurantID(Integer id);
    @Query(value = "SELECT * FROM orderr orr WHERE orr.userID = ?1 AND (orr.status_order = 1 OR orr.status_order = 2 OR orr.status_order = 3)", nativeQuery = true)
    Optional<Order> findCurrentOrder(Integer userID);
}
