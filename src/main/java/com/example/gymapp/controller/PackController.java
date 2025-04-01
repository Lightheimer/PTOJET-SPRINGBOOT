package com.example.gymapp.controller;

import com.example.gymapp.model.Pack;
import com.example.gymapp.service.PackService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packs")
public class PackController {
    private final PackService packService;

    public PackController(PackService packService) {
        this.packService = packService;
    }

    @GetMapping
    public List<Pack> getAllPacks() {
        return packService.getAllPacks();
    }

    @PostMapping
    public Pack createPack(@RequestBody Pack pack) {
        return packService.createPack(pack);
    }

    @DeleteMapping("/{id}")
    public void deletePack(@PathVariable Long id) {
        packService.deletePack(id);
    }
}
