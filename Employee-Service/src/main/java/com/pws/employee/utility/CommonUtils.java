package com.pws.employee.utility;

import com.pws.employee.ApiSuccess;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

/**
 * @Author Vinayak M
 * @Date 09/01/23
 */
public class CommonUtils {

    public static ResponseEntity<Object> buildResponseEntity(ApiSuccess apiSuccess) {
        return new ResponseEntity<>(apiSuccess, apiSuccess.getStatus());
    }

    public static Pageable getPageable(Integer page,Integer size, String sort,String order) {
    Pageable sorted;
	if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)
			&& order.equalsIgnoreCase("DESCENDING")) {
		sorted = PageRequest.of(page, size, Sort.by(sort).descending());
	} else if (!StringUtils.isEmpty(sort)) {
		sorted = PageRequest.of(page, size, Sort.by(sort));
	} else {
		sorted = PageRequest.of(page, size);
	}
	return sorted;
}
}