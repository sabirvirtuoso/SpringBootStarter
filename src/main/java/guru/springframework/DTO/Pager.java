package guru.springframework.DTO;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
public class Pager {

    private static final int TOTAL_PAGINATION_BUTTONS_TO_SHOW = 5;

    private int startPage;
    private int endPage;

    public Pager(int totalPages, int currentPage) {
        int halfPagesToShow = TOTAL_PAGINATION_BUTTONS_TO_SHOW / 2;

        if (totalPages < TOTAL_PAGINATION_BUTTONS_TO_SHOW) {
            setStartPage(1);
            setEndPage(totalPages);
        } else if (currentPage - halfPagesToShow <= 0) { // From the first page until but not including the middle point
            setStartPage(1);
            setEndPage(TOTAL_PAGINATION_BUTTONS_TO_SHOW);
        } else if (currentPage + halfPagesToShow == totalPages) { // The middle point
            setStartPage(currentPage - halfPagesToShow);
            setEndPage(totalPages);
        } else if (currentPage + halfPagesToShow > totalPages) {
            setStartPage(totalPages - TOTAL_PAGINATION_BUTTONS_TO_SHOW + 1);
            setEndPage(totalPages);
        } else {
            setStartPage(currentPage - halfPagesToShow);
            setEndPage(currentPage + halfPagesToShow);
        }
    }
}
