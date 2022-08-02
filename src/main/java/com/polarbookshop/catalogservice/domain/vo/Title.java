package com.polarbookshop.catalogservice.domain.vo;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@EqualsAndHashCode
@ToString
public class Title {

    private String value;

    protected Title() {}

    public Title(String title) {
        this.value = this.getValidTitle(title);
    }

    public String getValue() {
        return value;
    }

    private String getValidTitle(String title) {

        if (StringUtils.isEmpty(title)) {
            throw new IllegalArgumentException("The book title must be defined.");
        }

        return title;
    }
}
