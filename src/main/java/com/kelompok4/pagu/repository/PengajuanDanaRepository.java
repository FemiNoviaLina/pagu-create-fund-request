package com.kelompok4.pagu.repository;

import com.kelompok4.pagu.model.PengajuanDana;
import com.kelompok4.pagu.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PengajuanDanaRepository extends JpaRepository<PengajuanDana, Long> {
    public List<PengajuanDana> findByPengajuId(User pengajuId);
}

