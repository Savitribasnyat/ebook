package com.bookrentalsystem.bks.dto.transaction;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class FilterTransaction {

    private String from;

    private String to;

    public FilterTransaction(String from, String to) {
        this.from = from;
        this.to = to;
    }
}
