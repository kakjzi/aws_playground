package playground.aws.redis;

import org.springframework.data.repository.CrudRepository;

// Redis 데이터를 다루는 Repository 인터페이스
public interface AvailablePointRedisRepository extends CrudRepository<AvailablePoint, String> {
}
