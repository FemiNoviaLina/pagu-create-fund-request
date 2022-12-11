package com.kelompok4.pagu.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "pengajuan_dana")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PengajuanDana {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nomor;

    private String judul;

    private String deskripsi;

    private String proposal;

    @ManyToOne
    @JoinColumn(name = "pengaju_id", updatable = false)
    private User pengajuId;

    @OneToMany(cascade = { CascadeType.ALL }, mappedBy="pengajuanId")
    private List<Histori> histori;

    @OneToMany(cascade = { CascadeType.ALL }, mappedBy="pengajuanId")
    private List<ItemAnggaran> itemAnggaran;

    @Column(name = "created_at")
    @CreationTimestamp
    protected Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    protected Date updatedAt;
}
