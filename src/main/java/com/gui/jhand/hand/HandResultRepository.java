package com.gui.jhand.hand;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

interface HandResultRepository extends JpaRepository<HandResult, String>, JpaSpecificationExecutor<HandResult> {

}
