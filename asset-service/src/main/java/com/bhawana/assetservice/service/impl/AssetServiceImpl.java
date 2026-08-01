package com.bhawana.assetservice.service.impl;

import com.bhawana.assetservice.client.EmployeeClient;
import com.bhawana.assetservice.dto.EmployeeDTO;
import com.bhawana.commonlibrary.exception.ResourceNotFoundException;
import com.bhawana.assetservice.entity.Asset;
import com.bhawana.assetservice.repository.AssetRepository;
import com.bhawana.assetservice.service.AssetService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AssetServiceImpl implements AssetService {

    private final AssetRepository repository;

//    private final RestTemplate restTemplate;
    private final EmployeeClient employeeClient;
//    public AssetServiceImpl(AssetRepository repository,
//                            RestTemplate restTemplate) {
//
//        this.repository = repository;
//        this.restTemplate = restTemplate;
//    }
    public AssetServiceImpl(
            AssetRepository repository,
            EmployeeClient employeeClient) {

        this.repository = repository;
        this.employeeClient = employeeClient;
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

    @Override
    public Asset assignAsset(Long assetId, Long employeeId) {

        Asset asset = getAssetById(assetId);

        if ("ASSIGNED".equalsIgnoreCase(asset.getStatus())) {
            throw new IllegalArgumentException("Asset is already assigned.");
        }

        String employeeServiceUrl =
                "http://localhost:8081/api/employees/" + employeeId;

//        EmployeeDTO employee = restTemplate.getForObject(
//                employeeServiceUrl,
//                EmployeeDTO.class);
        EmployeeDTO employee =
                employeeClient.getEmployee(employeeId);

        if (employee == null) {
            throw new ResourceNotFoundException(
                    "Employee not found with id : " + employeeId);
        }

        asset.setAssignedEmployeeId(employeeId);
        asset.setStatus("ASSIGNED");

        return repository.save(asset);
    }
    @Override
    public Asset returnAsset(Long assetId) {

        Asset asset = getAssetById(assetId);

        if (!"ASSIGNED".equalsIgnoreCase(asset.getStatus())) {
            throw new IllegalArgumentException("Asset is not assigned.");
        }

        asset.setAssignedEmployeeId(null);
        asset.setStatus("AVAILABLE");

        return repository.save(asset);
    }
}