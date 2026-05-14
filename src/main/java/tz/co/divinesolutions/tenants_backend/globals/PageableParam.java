package tz.co.divinesolutions.tenants_backend.globals;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageableParam {
    List<SearchFieldsDto> searchFields= new ArrayList<>();
    private String sortBy;
    private String sortDirection;
    private Integer size;
    private Integer first;

    public PageableParam(String sortBy, String sortDirection, Integer size, Integer first) {
        this.sortBy = sortBy;
        this.sortDirection = sortDirection;
        this.size = size;
        this.first = first;
    }

    public PageableParam(Integer size, Integer first) {
        this.size = size;
        this.first = first;
    }
}