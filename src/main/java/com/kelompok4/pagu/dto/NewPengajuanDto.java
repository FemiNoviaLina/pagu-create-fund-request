package com.kelompok4.pagu.dto;

import com.kelompok4.pagu.model.ItemAnggaran;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewPengajuanDto {
    private String judul;

    private String deskripsi;

    private MultipartFile proposal;

    private String note;

    private List<ItemAnggaran> itemAnggaran;
}
