package marcia.daniel.sigtaxgov_selo_api.services;

import marcia.daniel.sigtaxgov_selo_api.models.Selo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RedisServiceTest {

    @Mock
    private RedisTemplate<String, Selo> redisTemplate;

    @Mock
    private ListOperations<String, Selo> listOperations;

    @InjectMocks
    private RedisService redisService;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForList()).thenReturn(listOperations);
        redisService.init();
    }

    @Test
    void shouldAddSeloRecenteCorrectly() {
        Selo selo = new Selo();
        selo.setCodigo("SEL-2025-0001");

        // Act
        redisService.addSeloRecente(selo);

        // Assert
        verify(listOperations).remove("recent_seals", 0, selo);
        verify(listOperations).leftPush("recent_seals", selo);
        verify(listOperations).trim("recent_seals", 0, 10);
    }

    @Test
    void shouldReturnRecentSelosFromRedis() {
        List<Selo> mockSelos = List.of(new Selo(), new Selo());
        when(listOperations.range("recent_seals", 0, -1)).thenReturn(mockSelos);

        List<Selo> result = redisService.getSelosRecentes();

        assertEquals(2, result.size());
        verify(listOperations).range("recent_seals", 0, -1);
    }
}
