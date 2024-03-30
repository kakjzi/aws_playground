package playground.aws.redis;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.SplittableRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ApiController {
    private final AvailablePointRedisRepository availablePointRedisRepository;

    @GetMapping("/")
    public String ok() {
        return "ok";
    }

    @GetMapping("/save")
    public String save() {
        String randomId = createId();
        LocalDateTime now = LocalDateTime.now();

        AvailablePoint availablePoint = AvailablePoint.builder()
            .id(randomId)
            .point(1L)
            .refreshTime(now)
            .build();

        log.info(">>>>>>> [save] availablePoint={}", availablePoint);

        availablePointRedisRepository.save(availablePoint);

        return "save";
    }

    @GetMapping("/get")
    public long get() {
        String id = createId();
        Optional<AvailablePoint> optionalAvailablePoint = availablePointRedisRepository.findById(id);

        return optionalAvailablePoint.map(AvailablePoint::getPoint).orElse(0L);
    }

    // 임의의 키를 생성하기 위해 1 ~ 1_000_000_000 사이 랜덤값 생성
    private String createId() {
        SplittableRandom random = new SplittableRandom();
        return String.valueOf(random.nextInt(1, 1_000_000_000));
    }
}
