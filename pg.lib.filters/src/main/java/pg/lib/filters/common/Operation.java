package pg.lib.filters.common;


import jakarta.persistence.criteria.*;

import java.time.LocalDateTime;

/**
 * The enum Operation.
 */
public enum Operation {

    /**
     * The Greater than.
     */
    GREATER_THAN {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.greaterThan(
                    root.get(criteria.getKey()),
                    criteria.getValue().toString()
            );
        }
    },

    /**
     * The Less than.
     */
    LESS_THAN {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.lessThan(
                    root.get(criteria.getKey()),
                    criteria.getValue().toString()
            );
        }
    },

    /**
     * The Greater than equal.
     */
    GREATER_THAN_EQUAL {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.greaterThanOrEqualTo(
                    root.get(criteria.getKey()),
                    criteria.getValue().toString()
            );
        }
    },

    /**
     * The Greater than equal join.
     */
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

    /**
     * The Less than equal.
     */
    LESS_THAN_EQUAL {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.lessThanOrEqualTo(
                    root.get(criteria.getKey()),
                    criteria.getValue().toString()
            );
        }
    },

    /**
     * The Not equal.
     */
    NOT_EQUAL {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.notEqual(root.get(
                    criteria.getKey()), criteria.getValue()
            );
        }
    },

    /**
     * The Equal.
     */
    EQUAL {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.equal(root.get(
                    criteria.getKey()), criteria.getValue()
            );
        }
    },
    /**
     * The Equal join.
     */
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

    /**
     * The Match join.
     */
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
    /**
     * The Match join list.
     */
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
    /**
     * The Match join list object.
     */
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

    /**
     * The Match.
     */
    MATCH {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.like(
                    builder.lower(root.get(criteria.getKey()))
                    , "%" + criteria.getValue().toString().toLowerCase() + "%"
            );
        }
    },

    /**
     * The Match start.
     */
    MATCH_START {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.like(
                    builder.lower(root.get(criteria.getKey())),
                    "%" + criteria.getValue().toString().toLowerCase()
            );
        }
    },

    /**
     * The Match end.
     */
    MATCH_END {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.like(
                    builder.lower(root.get(criteria.getKey())),
                    criteria.getValue().toString().toLowerCase() + "%"
            );
        }
    },

    /**
     * The Is member.
     */
    IS_MEMBER {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.isMember(
                    criteria.getValue(),
                    root.get(criteria.getKey())
            );
        }
    },

    /**
     * The In.
     */
    IN {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.in(
                    root.get(criteria.getKey())
            ).value(criteria.getValue());
        }
    },

    /**
     * The Not in.
     */
    NOT_IN {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.not(
                    root.get(criteria.getKey())
            ).in(criteria.getValue());
        }
    },

    /**
     * The Equal null.
     */
    EQUAL_NULL {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.isNull(root.get(criteria.getKey()));
        }
    },

    /**
     * The Not equal null.
     */
    NOT_EQUAL_NULL {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.isNotNull(root.get(criteria.getKey()));
        }
    },

    /**
     * The Greater than equal date.
     */
    GREATER_THAN_EQUAL_DATE {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.greaterThanOrEqualTo(
                    root.get(criteria.getKey()),
                    LocalDateTime.parse(criteria.getValue().toString())
            );
        }
    },

    /**
     * The Greater than date.
     */
    GREATER_THAN_DATE {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.greaterThan(
                    root.get(criteria.getKey()),
                    LocalDateTime.parse(criteria.getValue().toString()));
        }
    },

    /**
     * The Less than equal date.
     */
    LESS_THAN_EQUAL_DATE {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.lessThanOrEqualTo(
                    root.get(criteria.getKey()),
                    LocalDateTime.parse(criteria.getValue().toString())
            );
        }
    },

    /**
     * The Less than date.
     */
    LESS_THAN_DATE {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.lessThan(
                    root.get(criteria.getKey()),
                    LocalDateTime.parse(criteria.getValue().toString()));
        }
    },

    /**
     * The Equal date.
     */
    EQUAL_DATE {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.equal(root.get(
                    criteria.getKey()), LocalDateTime.parse(criteria.getValue().toString())
            );
        }
    },

    /**
     * The Not equal date.
     */
    NOT_EQUAL_DATE {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.notEqual(root.get(
                    criteria.getKey()), LocalDateTime.parse(criteria.getValue().toString())
            );
        }
    },

    /**
     * The Is empty.
     */
    IS_EMPTY {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.isEmpty(root.get(criteria.getKey()));
        }
    },

    /**
     * The Is not empty.
     */
    IS_NOT_EMPTY {
        @Override
        public <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder) {
            return builder.isNotEmpty(root.get(criteria.getKey()));
        }
    };

    /**
     * Gets predicate.
     *
     * @param <T>      the type parameter
     * @param root     the root
     * @param criteria the criteria
     * @param builder  the builder
     * @return the predicate
     */
    public abstract <T> Predicate getPredicate(final Root<T> root, final Criteria criteria, final CriteriaBuilder builder);
}