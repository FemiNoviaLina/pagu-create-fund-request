package com.kelompok4.pagu.model;

import lombok.*;
import org.hibernate.Interceptor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "item_anggaran")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemAnggaran {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nama;

    private Double harga;

    private Integer jumlah;

    @ManyToOne
    @JoinColumn(name = "pengajuan_id", updatable = false)
    private PengajuanDana pengajuanId;

    @Column(name = "created_at")
    @CreationTimestamp
    protected Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    protected Date updatedAt;
}
