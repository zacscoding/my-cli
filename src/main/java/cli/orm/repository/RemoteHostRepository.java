package cli.orm.repository;

import cli.orm.entity.RemoteHostEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Remote host repository
 *
 * @GitHub : https://github.com/zacscoding
 */
public interface RemoteHostRepository extends JpaRepository<RemoteHostEntity, Long> {

    Optional<RemoteHostEntity> findByHostName(String hostName);
}
