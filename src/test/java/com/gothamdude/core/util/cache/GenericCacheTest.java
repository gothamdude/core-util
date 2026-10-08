package com.gothamdude.core.util.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GenericCacheTest {

    private GenericCache<String, String> cache;

    @BeforeEach
    void setUp() {
        cache = new GenericCache<>();
    }

    /**
     * Builds a mocked {@link GenericCache.CacheValue} with a caller-controlled creation
     * timestamp, letting expiration logic be tested deterministically without sleeping.
     */
    @SuppressWarnings("unchecked")
    private static GenericCache.CacheValue<String> cacheValue(String value, LocalDateTime createdAt) {
        GenericCache.CacheValue<String> cacheValue = mock(GenericCache.CacheValue.class);
        when(cacheValue.getValue()).thenReturn(value);
        when(cacheValue.getCreatedAt()).thenReturn(createdAt);
        return cacheValue;
    }

    // -------------------------------------------------------------------------
    // Construction
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("construction")
    class Construction {

        @Test
        void defaultConstructor_usesDefaultTimeout() {
            assertThat(cache.cacheTimeout).isEqualTo(GenericCache.DEFAULT_CACHE_TIMEOUT);
        }

        @Test
        void defaultConstructor_startsWithEmptyMap() {
            assertThat(cache.cacheMap).isNotNull().isEmpty();
        }

        @Test
        void customConstructor_usesProvidedTimeout() {
            GenericCache<String, String> custom = new GenericCache<>(5_000L);
            assertThat(custom.cacheTimeout).isEqualTo(5_000L);
        }
    }

    // -------------------------------------------------------------------------
    // put / get
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("put / get")
    class PutGet {

        @Test
        void get_returnsStoredValue() {
            cache.put("key", "value");
            assertThat(cache.get("key")).contains("value");
        }

        @Test
        void get_returnsEmptyForMissingKey() {
            assertThat(cache.get("missing")).isEmpty();
        }

        @Test
        void put_overwritesExistingValue() {
            cache.put("key", "first");
            cache.put("key", "second");
            assertThat(cache.get("key")).contains("second");
        }

        @Test
        void put_allowsNullValue_getReturnsEmpty() {
            cache.put("key", null);
            // The key is present, but a null value maps to Optional.empty().
            assertThat(cache.containsKey("key")).isTrue();
            assertThat(cache.get("key")).isEmpty();
        }

        @Test
        void get_triggersCleanBeforeLookup() {
            GenericCache<String, String> spyCache = spy(new GenericCache<>());
            spyCache.get("anything");
            verify(spyCache).clean();
        }
    }

    // -------------------------------------------------------------------------
    // containsKey / remove / clear
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("containsKey / remove / clear")
    class MutationOps {

        @Test
        void containsKey_trueAfterPut_falseOtherwise() {
            assertThat(cache.containsKey("key")).isFalse();
            cache.put("key", "value");
            assertThat(cache.containsKey("key")).isTrue();
        }

        @Test
        void remove_deletesEntry() {
            cache.put("key", "value");
            cache.remove("key");
            assertThat(cache.containsKey("key")).isFalse();
            assertThat(cache.get("key")).isEmpty();
        }

        @Test
        void remove_missingKey_isNoOp() {
            cache.remove("missing");
            assertThat(cache.cacheMap).isEmpty();
        }

        @Test
        void clear_removesAllEntries() {
            cache.put("a", "1");
            cache.put("b", "2");

            cache.clear();

            assertThat(cache.cacheMap).isEmpty();
            assertThat(cache.containsKey("a")).isFalse();
            assertThat(cache.containsKey("b")).isFalse();
        }
    }

    // -------------------------------------------------------------------------
    // Expiration / clean
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("expiration")
    class Expiration {

        @Test
        void get_returnsEmptyWhenEntryHasExpired() {
            GenericCache<String, String> spyCache = spy(new GenericCache<>(60_000L));
            doReturn(cacheValue("value", LocalDateTime.now().minusMinutes(5)))
                    .when(spyCache).createCacheValue("value");

            spyCache.put("key", "value");

            assertThat(spyCache.get("key")).isEmpty();
            assertThat(spyCache.containsKey("key")).isFalse();
        }

        @Test
        void get_returnsValueWhenEntryHasNotExpired() {
            GenericCache<String, String> spyCache = spy(new GenericCache<>(60_000L));
            doReturn(cacheValue("value", LocalDateTime.now()))
                    .when(spyCache).createCacheValue("value");

            spyCache.put("key", "value");

            assertThat(spyCache.get("key")).contains("value");
        }

        @Test
        void clean_removesOnlyExpiredEntries() {
            GenericCache<String, String> spyCache = spy(new GenericCache<>(60_000L));
            doReturn(cacheValue("fresh", LocalDateTime.now()))
                    .when(spyCache).createCacheValue("fresh");
            doReturn(cacheValue("stale", LocalDateTime.now().minusMinutes(5)))
                    .when(spyCache).createCacheValue("stale");

            spyCache.put("freshKey", "fresh");
            spyCache.put("staleKey", "stale");

            spyCache.clean();

            assertThat(spyCache.containsKey("freshKey")).isTrue();
            assertThat(spyCache.containsKey("staleKey")).isFalse();
        }

        @Test
        void clean_onEmptyCache_isNoOp() {
            cache.clean();
            assertThat(cache.cacheMap).isEmpty();
        }

        @Test
        void entryExpiresExactlyAfterTimeout() {
            GenericCache<String, String> spyCache = spy(new GenericCache<>(1_000L));
            // Created just over the 1s timeout ago -> expired.
            doReturn(cacheValue("value", LocalDateTime.now().minusSeconds(2)))
                    .when(spyCache).createCacheValue("value");

            spyCache.put("key", "value");

            assertThat(spyCache.get("key")).isEmpty();
        }
    }
}
