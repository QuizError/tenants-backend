package tz.co.divinesolutions.tenants_backend.globals;

import tz.co.divinesolutions.tenants_backend.enums.QueryOperator;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.*;

import static tz.co.divinesolutions.tenants_backend.enums.SearchOperationType.*;

@Component
@Slf4j
public class GenericSpecificationSearch<T> {


    public Specification<T> getSearchSpec(List<SearchFieldsDto> searchFieldsDtos) {
        return (root, criteriaQuery, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            Map<String, Join<?, ?>> joins = new HashMap<>();

            for (SearchFieldsDto searchFieldsDto : searchFieldsDtos) {
                Join<?, ?> theJoin = null;
                String fieldName = searchFieldsDto.getFieldName();

                // Check if field name contains dots, indicating a nested property
                if (fieldName.contains(".")) {

                    log.info("Field is relation {}", fieldName);
                    // Split field name by dots
                    String[] fieldNames = fieldName.split("\\.");

                    // Start from the root and join the related entities based on the nested properties
                    Join<?, ?> join = null;
                    int index = 0;
                    for (String field : fieldNames) {
                        if (index == fieldNames.length - 1) {
                            break;
                        }
                        Join<?, ?> finalJoin = join;

                        join = index == 0 && join == null ? joins.computeIfAbsent(field, key -> {
                            log.info("Joining to {}", key);
                            return  root.join(key, JoinType.LEFT);
                        }) : joins.computeIfAbsent(field, key -> finalJoin.join(key, JoinType.LEFT));

                        index++;

                    }

                    theJoin = join;
                    log.info("the join {}", theJoin);

                    // Use the final join and field name for further operations
                    fieldName = fieldNames[fieldNames.length - 1];
                }

                switch (searchFieldsDto.getSearchType()) {
                    case In:
                        predicates.add(Objects.requireNonNullElse(theJoin, root).get(fieldName).as(String.class).in(searchFieldsDto.getFieldValues()));

                        break;
                    case Equals:
                        predicates
                                .add(criteriaBuilder.equal(criteriaBuilder.lower(Objects.requireNonNullElse(theJoin, root).get(fieldName).as(String.class)),
                                        searchFieldsDto.getFieldValue().toString().toLowerCase()));
                        break;
                    case GreaterThan:
                        predicates.add(criteriaBuilder.greaterThan(Objects.requireNonNullElse(theJoin, root).get(fieldName),
                                searchFieldsDto.getFieldValue().toString().toLowerCase()));

                        break;
                    case LessThan:
                        predicates.add(criteriaBuilder.lessThan(Objects.requireNonNullElse(theJoin, root).get(fieldName),
                                searchFieldsDto.getFieldValue().toString().toLowerCase()));

                        break;
                    case Like:
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(Objects.requireNonNullElse(theJoin, root).get(fieldName).as(String.class)),
                                "%" + searchFieldsDto.getFieldValue().toString().toLowerCase() + "%"));

                        break;
                    case NotEquals:
                        predicates.add(
                                criteriaBuilder.notEqual(criteriaBuilder.lower(Objects.requireNonNullElse(theJoin, root).get(fieldName)),
                                        searchFieldsDto.getFieldValue().toString().toLowerCase()));
                        break;
                }
            }
            if (!searchFieldsDtos.isEmpty() && searchFieldsDtos.getFirst().getQueryOperator() == QueryOperator.AND) {
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            } else {
                return !searchFieldsDtos.isEmpty() ? criteriaBuilder.or(predicates.toArray(new Predicate[0])) : null;
            }
        };
    }

    public Specification<T> createSpecification(SearchFieldsDto input) {
        return switch (input.getSearchType()) {
            case Equals -> (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(input.getFieldName()),
                    input.getFieldValue());
            case NotEquals -> (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get(input.getFieldName()),
                    input.getFieldValue());
            case GreaterThan ->
                    (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(input.getFieldName()),
                            input.getFieldValue().toString());
            case LessThan -> (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get(input.getFieldName()),
                    input.getFieldValue().toString());
            case Like -> (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(input.getFieldName()),
                    "%" + input.getFieldValue() + "%");
            case In -> (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get(input.getFieldName()))
                    .value(input.getFieldValues());
            case Empty -> (root, query, criteriaBuilder) -> criteriaBuilder.isEmpty(root.get(input.getFieldName()));
            case Null -> (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get(input.getFieldName()));
            case NotNull -> (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get(input.getFieldName()));
            case NotEmpty ->
                    (root, query, criteriaBuilder) -> criteriaBuilder.isNotEmpty(root.get(input.getFieldName()));
            default -> throw new RuntimeException("Operation not supported yet");
        };
    }

    public Specification<T> getEqualSpec(String fieldName, Object fieldValue) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(getPath(root, fieldName), fieldValue);
    }

    public Specification<T> getNotEqualSpec(String fieldName, Object fieldValue) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(getPath(root, fieldName), fieldValue);
    }

    public Specification<T> getIsNullSpec(String fieldName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(getPath(root,fieldName));
    }

    public Specification<T> getIsNotNullSpec(String fieldName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(getPath(root, fieldName));
    }

    public Path<T> getPath(Root<T> root, String attributeName) {
        Path<T> path = root;
        if (attributeName.split("\\.").length > 0) {
            for (String part : attributeName.split("\\.")) {
                path = path.get(part);
            }
        } else {
            path = path.get(attributeName);
        }
        return path;
    }

}