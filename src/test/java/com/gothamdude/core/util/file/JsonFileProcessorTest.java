package com.gothamdude.core.util.file;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class JsonFileProcessorTest {

    @Test
    @DisplayName("Should serialize Object to JSON string correctly")
    void testToJson() {
        TestUser user = new TestUser(1, "John Doe", "john@example.com");
        String json = JsonFileProcessor.toJson(user);
        assertThat(json).isNotBlank();
        assertThat(json).contains("\"id\" : 1");
        assertThat(json).contains("\"name\" : \"John Doe\"");
        // Verifies indentation is enabled
        assertThat(json).contains("  \"email\" : \"john@example.com\"");
    }

    @Test
    @DisplayName("Should deserialize JSON string to Object correctly")
    void testFromJson() {
        String json = "{\"id\":2,\"name\":\"Jane Doe\",\"email\":\"jane@example.com\"}";
        TestUser user = JsonFileProcessor.fromJson(json, TestUser.class);
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(2);
        assertThat(user.getName()).isEqualTo("Jane Doe");
    }

    @Test
    @DisplayName("Should ignore unknown properties during deserialization")
    void testFromJsonIgnoreUnknownProperties() {
        // 'extraField' does not exist in User
        String json = "{\"id\":3,\"name\":\"Extra Field Test\",\"extraField\":\"unknown\"}";
        TestUser user = JsonFileProcessor.fromJson(json, TestUser.class);
        assertThat(user).isNotNull();
        assertThat(user.getName()).isEqualTo("Extra Field Test");
        // FAIL_ON_UNKNOWN_PROPERTIES=false allows this to pass
    }

    @Test
    @DisplayName("Should throw RuntimeException when JSON is invalid")
    void testFromJsonInvalidJson() {
        String invalidJson = "{\"id\":3, \"name\":"; // Incomplete JSON
        assertThatThrownBy(() -> JsonFileProcessor.fromJson(invalidJson, TestUser.class))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error reading from JSON");
    }

}