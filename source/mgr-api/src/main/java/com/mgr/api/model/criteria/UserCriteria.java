package com.mgr.api.model.criteria;


import com.mgr.api.model.Account;
import com.mgr.api.model.User;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserCriteria {
    private String username;
    private String fullName;
    private Integer status;
    private Integer gender;

    public Specification<User> getSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Join với bảng Account để lấy thông tin
            Join<User, Account> accountJoin = root.join("account");

            if (StringUtils.isNoneBlank(username)) {
                predicates.add(cb.like(cb.lower(accountJoin.get("username")), "%" + username.toLowerCase() + "%"));
            }
            if (StringUtils.isNoneBlank(fullName)) {
                predicates.add(cb.like(cb.lower(accountJoin.get("fullName")), "%" + fullName.toLowerCase() + "%"));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (gender != null) {
                predicates.add(cb.equal(root.get("gender"), gender));
            }

            // Luôn lọc theo kind = 2 để đảm bảo chỉ lấy User
            predicates.add(cb.equal(accountJoin.get("kind"), 2));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
