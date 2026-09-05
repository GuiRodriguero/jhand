package com.gui.jhand.hand;

import com.gui.jhand.core.poker.HandRank;
import com.gui.jhand.hand.web.HandResultFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

class HandResultSpecification {

	public static Specification<HandResult> withFilters(HandResultFilter filter) {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (hasText(filter.handId())) {
				predicates.add(cb.equal(root.get("handId"), filter.handId()));
			}

			if (hasText(filter.sessionId())) {
				predicates.add(cb.equal(root.get("sessionId"), filter.sessionId()));
			}

			if (hasText(filter.heroCards())) {
				predicates.add(cb.equal(root.get("heroCards"), filter.heroCards()));
			}

			if (hasText(filter.handRank())) {
				predicates.add(cb.equal(root.get("handRank"), HandRank.valueOf(filter.handRank())));
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}

}
