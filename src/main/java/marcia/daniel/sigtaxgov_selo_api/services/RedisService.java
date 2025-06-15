package marcia.daniel.sigtaxgov_selo_api.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import marcia.daniel.sigtaxgov_selo_api.models.Selo;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisService {

    private static final String RECENT_SEALS_KEY = "recent_seals";
    private static final long MAX_RECENT_SEALS = 10;

    private final RedisTemplate<String, Selo> redisTemplate;

    ListOperations<String, Selo> listOperations;

    @PostConstruct
    public void init(){
        listOperations = redisTemplate.opsForList();
    }

    public void addSeloRecente(Selo selo){
        listOperations.remove(RECENT_SEALS_KEY, 0, selo);

        listOperations.leftPush(RECENT_SEALS_KEY, selo);

        listOperations.trim(RECENT_SEALS_KEY, 0, MAX_RECENT_SEALS);
    }

    public List<Selo> getSelosRecentes(){
        return listOperations.range(RECENT_SEALS_KEY, 0, -1);
    }

}
