package ua.yehor.rest.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.yehor.rest.todo.model.TaskEntity;
import ua.yehor.rest.todo.model.UserEntity;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    List<TaskEntity> findAllByUserEntityId(Long id);

    void deleteByName(String name);

    TaskEntity findByUserEntityAndName(UserEntity userEntity, String name);
}
