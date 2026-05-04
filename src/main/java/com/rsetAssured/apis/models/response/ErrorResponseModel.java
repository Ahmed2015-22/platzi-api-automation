package com.rsetAssured.apis.models.response;


import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "path",
            "timestamp",
            "name",
            "message",
            "code"
    })
    @Generated("jsonschema2pojo")
    public class ErrorResponseModel {

        @JsonProperty("path")
        public String path;
        @JsonProperty("timestamp")
        public String timestamp;
        @JsonProperty("name")
        public String name;
        @JsonProperty("message")
        public Object message;  // API returns String OR List<String> depending on error type
        @JsonProperty("code")
        public String code;

        /**
         * Helper to get message as a single string regardless of whether
         * the API returned a String or a List of Strings.
         */
        public String getMessageAsString() {
            if (message == null) return null;
            if (message instanceof String) return (String) message;
            if (message instanceof java.util.List) {
                return String.join(", ", ((java.util.List<?>) message).stream()
                        .map(Object::toString)
                        .toList());
            }
            return message.toString();
        }
}
