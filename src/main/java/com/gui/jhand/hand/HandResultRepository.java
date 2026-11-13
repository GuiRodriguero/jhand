package com.gui.jhand.hand;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

interface HandResultRepository extends JpaRepository<HandResult, String>, JpaSpecificationExecutor<HandResult> {

	List<HandResult> findAllBySessionIdOrderByTimeAscHandIdAsc(String sessionId);

}
