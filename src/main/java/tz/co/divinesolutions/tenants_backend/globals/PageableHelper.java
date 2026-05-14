package tz.co.divinesolutions.tenants_backend.globals;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Configuration
public class PageableHelper {

    public Pageable buildPageable(PageableParam param) {

        Sort sort = Sort.unsorted();

        if (param.getSortBy() != null && param.getSortDirection() != null) {
            sort = param.getSortDirection().equalsIgnoreCase("DESC")
                    ? Sort.by(param.getSortBy()).descending()
                    : Sort.by(param.getSortBy()).ascending();
        }
        int size = param.getSize() != null ? param.getSize() : 10;
        int first = param.getFirst() != null ? param.getFirst() : 0;

        int page = first / size; // convert offset → page

        return PageRequest.of(page, size, sort);
    }

    public <T> PageResponse<T> toPageResponse(Page<T> page) {
        PageResponse<T> response = new PageResponse<>();
        response.setContent(page.getContent());
        response.setPage(page.getNumber());
        response.setSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLast(page.isLast());
        return response;
    }
}
