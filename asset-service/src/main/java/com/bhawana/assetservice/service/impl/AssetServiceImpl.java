package com.bhawana.assetservice.service.impl;

import com.bhawana.commonlibrary.exception.ResourceNotFoundException;
import com.bhawana.assetservice.entity.Asset;
import com.bhawana.assetservice.repository.AssetRepository;
import com.bhawana.assetservice.service.AssetService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetServiceImpl implements AssetService {

    private final AssetRepository repository;

    public AssetServiceImpl(AssetRepository repository) {
        this.repository = repository;
    }

    @Override
    public Asset saveAsset(Asset asset) {

        repository.findByAssetCode(asset.getAssetCode())
                .ifPresent(e -> {
                    throw new IllegalArgumentException("Asset code already exists.");
                });

        repository.findBySerialNumber(asset.getSerialNumber())
                .ifPresent(a -> {
                    throw new IllegalArgumentException("Serial number already exists.");
                });

        return repository.save(asset);
    }

    @Override
    public List<Asset> getAllAssets() {
        return repository.findAll();
    }

    @Override
    public Asset getAssetById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Asset not found with id : " + id));
    }

    @Override
    public Asset updateAsset(Long id, Asset asset) {

        Asset existing = getAssetById(id);

        existing.setAssetCode(asset.getAssetCode());
        existing.setAssetName(asset.getAssetName());
        existing.setAssetType(asset.getAssetType());
        existing.setManufacturer(asset.getManufacturer());
        existing.setModel(asset.getModel());
        existing.setSerialNumber(asset.getSerialNumber());
        existing.setPurchaseDate(asset.getPurchaseDate());
        existing.setPurchaseCost(asset.getPurchaseCost());
        existing.setStatus(asset.getStatus());
        existing.setAssignedEmployeeId(asset.getAssignedEmployeeId());

        return repository.save(existing);
    }

    @Override
    public void deleteAsset(Long id) {

        Asset asset = getAssetById(id);

        repository.delete(asset);
    }
}