package com.backend.boilerplate.dto;

import com.backend.boilerplate.constant.FeatureAction;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author sarvesh
 * @version 0.0.3
 * @since 0.0.3
 */
@JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"uuid", "name", "featureActions"})
@EqualsAndHashCode(of = "uuid")
@Data
@NoArgsConstructor
public class FeatureDto {
    private UUID uuid;

    private String name;

    private List<FeatureAction> featureActions = new ArrayList<>();

    public FeatureDto(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }
}
