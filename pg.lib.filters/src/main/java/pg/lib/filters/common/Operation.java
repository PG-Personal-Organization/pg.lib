package pg.lib.filters.common;


import javax.persistence.criteria.*;

import java.time.LocalDateTime;

public enum Operation {

    GREATER_THAN {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.greaterThan(
                    root.get(criteria.getKey()),
                    criteria.getValue().toString()
            );
        }
    },

    LESS_THAN {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.lessThan(
                    root.get(criteria.getKey()),
                    criteria.getValue().toString()
            );
        }
    },

    GREATER_THAN_EQUAL {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.greaterThanOrEqualTo(
                    root.get(criteria.getKey()),
                    criteria.getValue().toString()
            );
        }
    },

    GREATER_THAN_EQUAL_JOIN {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            String[] memberAndField = criteria.getKey().split("\\.", 2);

            Join<T, ?> join = root.join(memberAndField[0]);

            return builder.greaterThanOrEqualTo(
                    join.get(
                            memberAndField[1]
                    ),
                    criteria.getValue().toString()
            );
        }
    },

//    GREATER_THAN_EQUAL_IN_MAP {
//        @Override
//        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
//            MapJoin<T, ?, ?> map = root.joinMap(criteria.getKey());
//
//            return builder.greaterThanOrEqualTo(
//                    criteria.getValue(),
//                    map.value()
//            );
//        }
//    },

    LESS_THAN_EQUAL {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.lessThanOrEqualTo(
                    root.get(criteria.getKey()),
                    criteria.getValue().toString()
            );
        }
    },

    NOT_EQUAL {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.notEqual(root.get(
                    criteria.getKey()), criteria.getValue()
            );
        }
    },

    EQUAL {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.equal(root.get(
                    criteria.getKey()), criteria.getValue()
            );
        }
    },
    EQUAL_JOIN {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            String[] memberAndField = criteria.getKey().split("\\.", 2);

            Join<T, ?> join = root.join(memberAndField[0]);

            return builder.equal(
                    join.get(
                            memberAndField[1]
                    ),
                    criteria.getValue()
            );
        }
    },

    MATCH_JOIN {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            String[] memberAndField = criteria.getKey().split("\\.", 2);

            Join<T, ?> join = root.join(memberAndField[0]);

            return builder.like(
                    builder.lower(join.get(memberAndField[1]))
                    , "%" + criteria.getValue().toString().toLowerCase() + "%"
            );
        }
    },
    MATCH_JOIN_LIST {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            String[] memberAndField = criteria.getKey().split("\\.", 2);

            ListJoin<T, ?> join = root.joinList(memberAndField[0]);

            return builder.like(
                    builder.lower(join.get(memberAndField[1]))
                    , "%" + criteria.getValue().toString().toLowerCase() + "%"
            );

        }
    },
    MATCH_JOIN_LIST_OBJECT {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            String[] memberAndField = criteria.getKey().split("\\.", 3);

            ListJoin<T, ?> join = root.joinList(memberAndField[0]);

            Join<?, ?> secondJoin = join.join(memberAndField[1]);

            return builder.like(
                    builder.lower(secondJoin.get(memberAndField[2]))
                    , "%" + criteria.getValue().toString().toLowerCase() + "%"
            );
        }
    },

    MATCH {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.like(
                    builder.lower(root.get(criteria.getKey()))
                    , "%" + criteria.getValue().toString().toLowerCase() + "%"
            );
        }
    },

    MATCH_START {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.like(
                    builder.lower(root.get(criteria.getKey())),
                    "%" + criteria.getValue().toString().toLowerCase()
            );
        }
    },

    MATCH_END {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.like(
                    builder.lower(root.get(criteria.getKey())),
                    criteria.getValue().toString().toLowerCase() + "%"
            );
        }
    },

    IS_MEMBER {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.isMember(
                    criteria.getValue(),
                    root.get(criteria.getKey())
            );
        }
    },

    IN {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.in(
                    root.get(criteria.getKey())
            ).value(criteria.getValue());
        }
    },

    NOT_IN {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.not(
                    root.get(criteria.getKey())
            ).in(criteria.getValue());
        }
    },

    EQUAL_NULL {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.isNull(root.get(criteria.getKey()));
        }
    },

    NOT_EQUAL_NULL {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.isNotNull(root.get(criteria.getKey()));
        }
    },

    GREATER_THAN_EQUAL_DATE {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.greaterThanOrEqualTo(
                    root.get(criteria.getKey()),
                    LocalDateTime.parse(criteria.getValue().toString())
            );
        }
    },

    GREATER_THAN_DATE {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.greaterThan(
                    root.get(criteria.getKey()),
                    LocalDateTime.parse(criteria.getValue().toString()));
        }
    },

    LESS_THAN_EQUAL_DATE {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.lessThanOrEqualTo(
                    root.get(criteria.getKey()),
                    LocalDateTime.parse(criteria.getValue().toString())
            );
        }
    },

    LESS_THAN_DATE {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.lessThan(
                    root.get(criteria.getKey()),
                    LocalDateTime.parse(criteria.getValue().toString()));
        }
    },

    EQUAL_DATE {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.equal(root.get(
                    criteria.getKey()), LocalDateTime.parse(criteria.getValue().toString())
            );
        }
    },

    NOT_EQUAL_DATE {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.notEqual(root.get(
                    criteria.getKey()), LocalDateTime.parse(criteria.getValue().toString())
            );
        }
    },

    IS_EMPTY {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.isEmpty(root.get(criteria.getKey()));
        }
    },

    IS_NOT_EMPTY {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.isNotEmpty(root.get(criteria.getKey()));
        }
    };

    public abstract <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder);
}