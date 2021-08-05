package sumdu.kvp.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sumdu.kvp.bot.model.Topic;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {

    @Query("FROM Topic WHERE father_id IS NULL")
    List<Topic> findByFatherIdIsNull();

    @Query("FROM Topic WHERE father_id = ?1")
    List<Topic> findByFatherId(Integer fatherId);

    @Query(value = "SELECT * FROM Topic WHERE id = (SELECT father_id FROM Topic WHERE id = ?1)", nativeQuery = true)
    Topic  findFatherByChild(Integer id);

    Topic findByName(String name);

    @Modifying
    @Query(value = "insert into Topic (name, text, type, father_id) VALUES (?1, ?2, ?3, ?4)", nativeQuery = true)
    @Transactional
    void saveWithFather(String name, String text, String type, Integer fatherId);

    @Modifying
    @Query(value = "UPDATE Topic SET name = ?1, text = ?2, type = ?3, father_id = ?4 WHERE id = ?5", nativeQuery = true)
    @Transactional
    void updateWithFather(String name, String text, String type, Integer fatherId, Integer id);
}
