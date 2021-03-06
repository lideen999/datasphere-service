/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.datasphere.server.domain.workspace;

/**
 * Created by aladin on 2019. 12. 20..
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * The interface Book repository.
 */
@RepositoryRestResource(path = "books", itemResourceRel = "book", collectionResourceRel = "books",
                        excerptProjection = BookProjections.DefaultProjection.class)
public interface BookRepository extends JpaRepository<Book, String>,
    QuerydslPredicateExecutor<Book>,
    BookRepositoryExtends {

  @RestResource(exported = false)
  @Query("SELECT b FROM Book b, BookTree bt WHERE b.id = bt.id.descendant AND bt.id.ancestor = :bookId AND bt.depth > 0")
  List<Book> findAllSubBooks(@Param("bookId") String bookId);

  @RestResource(exported = false)
  @Query("SELECT b FROM Book b, BookTree bt WHERE b.id = bt.id.ancestor AND bt.id.descendant = :bookId ORDER BY bt.depth desc")
  List<Book> findAllAncestorBooks(@Param("bookId") String bookId);

}
