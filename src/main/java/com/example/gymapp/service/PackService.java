package com.example.gymapp.service;

import com.example.gymapp.model.Pack;
import com.example.gymapp.repository.PackRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PackService {
    private final PackRepository packRepository;

    public PackService(PackRepository packRepository) {
        this.packRepository = packRepository;
    }

    public List<Pack> getAllPacks() {
        return packRepository.findAll();
    }

    public Pack createPack(Pack pack) {
        return packRepository.save(pack);
    }

    public void deletePack(Long id) {
        packRepository.deleteById(id);
    }
}
