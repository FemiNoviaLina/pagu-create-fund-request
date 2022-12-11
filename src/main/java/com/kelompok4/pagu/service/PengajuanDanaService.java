package com.kelompok4.pagu.service;

import com.kelompok4.pagu.config.FileStorageConfig;
import com.kelompok4.pagu.dto.NewPengajuanDto;
import com.kelompok4.pagu.exception.FileStorageException;
import com.kelompok4.pagu.exception.MyFileNotFoundException;
import com.kelompok4.pagu.model.Histori;
import com.kelompok4.pagu.model.PengajuanDana;
import com.kelompok4.pagu.model.User;
import com.kelompok4.pagu.repository.PengajuanDanaRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@Service
public class PengajuanDanaService {
    private PengajuanDanaRepository pengajuanDanaRepository;

    private final Path fileStorageLocation;

    public PengajuanDanaService(PengajuanDanaRepository pengajuanDanaRepository,
                                FileStorageConfig fileStorageConfig) {
        this.fileStorageLocation = Paths.get(fileStorageConfig.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", e);
        }

        this.pengajuanDanaRepository = pengajuanDanaRepository;
    }

    public void createPengajuanDana(NewPengajuanDto newPengajuanDto, User user) {
        String proposal;
        try {
            proposal = storeProposal(newPengajuanDto.getProposal());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Histori histori1 = Histori.builder()
                .status("Tahap I - Pengajuan")
                .note(newPengajuanDto.getNote())
                .pembuatId(user)
                .build();

        PengajuanDana pengajuanDana = PengajuanDana.builder()
                .judul(newPengajuanDto.getJudul())
                .deskripsi(newPengajuanDto.getDeskripsi())
                .proposal(proposal)
                .pengajuId(user)
                .itemAnggaran(newPengajuanDto.getItemAnggaran())
                .histori(List.of(histori1))
                .build();

        histori1.setPengajuanId(pengajuanDana);
        newPengajuanDto.getItemAnggaran().forEach( i -> i.setPengajuanId(pengajuanDana));

        pengajuanDanaRepository.save(pengajuanDana);
    }

    public List<PengajuanDana> getAllPengajuanDana(User user) {
        return pengajuanDanaRepository.findByPengajuId(user);
    }

    public PengajuanDana getPengajuanDana(Long nomor) {
        return pengajuanDanaRepository.findById(nomor).get();
    }

    public Resource getProposal(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

    private String storeProposal(MultipartFile file) {
        String fileExtention = StringUtils.cleanPath(file.getOriginalFilename()).split("\\.")[1];

        String fileName = "proposal" + Long.toString(new Date().getTime()) + "." +fileExtention;

        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;

        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
}
