package tv.marytv.video.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tv.marytv.video.entity.Category;
import tv.marytv.video.entity.Event;
import tv.marytv.video.entity.Item;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i LEFT JOIN FETCH i.children LEFT JOIN FETCH i.parent WHERE i.parent IS NULL")
    List<Item> findAllWithChildren();

    @Query("SELECT i FROM Item i LEFT JOIN FETCH i.children WHERE i.id = :id")
    Optional<Item> findByIdWithChildren(@Param("id") Long id);

    List<Item> findByCategoryId(Long categoryId);

    List<Item> findByEventId(Long eventId);

    Optional<Item> findByVideoUrl(String videoUrl);

    @Query("SELECT i FROM Item i WHERE i.isNew = true")
    List<Item> findAllNewItems();

    @Query("SELECT i FROM Item i WHERE i.isHeadline = true")
    List<Item> findAllHeadlineItems();

    @Query("SELECT i FROM Item i LEFT JOIN i.category c LEFT JOIN i.event e WHERE i.title LIKE %:keyword% OR i.description LIKE %:keyword% OR c.name LIKE %:keyword% OR e.name LIKE %:keyword%")
    List<Item> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT i FROM Item i WHERE i.parent.id = :parentId")
    Page<Item> findByParentId(@Param("parentId") Long parentId, Pageable pageable);

    @Query("SELECT i FROM Item i WHERE i.contentType = 'SERIES_HEADER' OR i.contentType = 'STANDALONE'")
    Page<Item> findShows(Pageable pageable);

    @Query("SELECT i FROM Item i WHERE i.isHeadline = true")
    Page<Item> findHeadliners(Pageable pageable);

    @Query("SELECT i FROM Item i WHERE i.contentType = 'SERIES_HEADER' OR i.contentType = 'STANDALONE' AND (:categoryId IS NULL OR i.category.id = :categoryId) AND (:fromDate IS NULL OR i.itemDate >= :fromDate) AND (:toDate IS NULL OR i.itemDate <= :toDate) AND (:keyword IS NULL OR i.title LIKE %:keyword% OR i.description LIKE %:keyword%) AND (:isHeadline IS NULL OR i.isHeadline = :isHeadline)")
    Page<Item> findShowsFiltered(@Param("categoryId") Long categoryId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("keyword") String keyword, @Param("isHeadline") Boolean isHeadline, Pageable pageable);
}