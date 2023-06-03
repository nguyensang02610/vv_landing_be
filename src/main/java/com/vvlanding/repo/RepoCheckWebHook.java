package com.vvlanding.repo;

import com.vvlanding.table.CheckWebhook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoCheckWebHook extends JpaRepository<CheckWebhook,Long> {

}
