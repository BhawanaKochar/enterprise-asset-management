package com.bhawana.assetservice.controller;

import com.bhawana.assetservice.entity.Asset;
import com.bhawana.assetservice.service.AssetService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetService service;

    public AssetController(AssetService service) {
        this.service = service;
    }

    @PostMapping
    public Asset createAsset(@Valid @RequestBody Asset asset) {
        return service.saveAsset(asset);
    }

    @GetMapping
    public List<Asset> getAllAssets() {
        return service.getAllAssets();
    }

    @GetMapping("/{id}")
    public Asset getAssetById(@PathVariable Long id) {
        return service.getAssetById(id);
    }

    @PutMapping("/{id}")
    public Asset updateAsset(@PathVariable Long id,
                             @RequestBody Asset asset) {
        return service.updateAsset(id, asset);
    }

    @DeleteMapping("/{id}")
    public String deleteAsset(@PathVariable Long id) {
        service.deleteAsset(id);
        return "Asset deleted successfully";
    }
}