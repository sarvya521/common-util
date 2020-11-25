package com.backend.boilerplate.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author sarvesh
 * @version 0.0.3
 * @since 0.0.3
 */
@JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"uuid", "resourceName", "resourceHttpMethod", "resourceEndpoint"})
@EqualsAndHashCode(of = "uuid")
@Data
@NoArgsConstructor
public class ClaimDto {

    private UUID uuid;

    private String resourceName;

    private String resourceHttpMethod;

    private String resourceEndpoint;
}
