package com.socialmedia.api.specification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchCriteria {
    @NotBlank
    private String key;
    @NotEmpty
    private Object value;
    @NotEmpty
    private SearchOperation operation;
}
