package tz.co.divinesolutions.tenants_backend.globals;

import tz.co.divinesolutions.tenants_backend.enums.QueryOperator;
import tz.co.divinesolutions.tenants_backend.enums.SearchOperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchFieldsDto {
    private String fieldName;
    private Object fieldValue;

    private List<Object> fieldValues;
    private SearchOperationType searchType;
    private QueryOperator queryOperator = QueryOperator.OR;

    public SearchFieldsDto(String fieldName, Object fieldValue, SearchOperationType searchType) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.searchType = searchType;
    }
}