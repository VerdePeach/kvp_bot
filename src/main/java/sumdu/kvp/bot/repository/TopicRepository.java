package sumdu.kvp.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sumdu.kvp.bot.model.Topic;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {

    @Query("FROM Topic WHERE father_id IS NULL")
    Optional<List<Topic>> findByFatherIdIsNull();

    @Query("FROM Topic WHERE father_id = ?1")
    Optional<List<Topic>> findByFatherId(Integer fatherId);

    @Query(value = "SELECT * FROM Topic WHERE id = (SELECT father_id FROM Topic WHERE id = ?1)", nativeQuery = true)
    Optional<Topic>  findFatherByChild(Integer id);

    Optional<Topic> findByName(String name);

    @Modifying
    @Query(value = "insert into Topic (name, text, father_id) VALUES (?1, ?2, ?3)", nativeQuery = true)
    @Transactional
    void saveWithFather(String name, String text, Integer fatherId);
}
