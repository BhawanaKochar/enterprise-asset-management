package com.bhawana.assetservice.service;

import com.bhawana.assetservice.entity.Asset;

import java.util.List;

public interface AssetService {

    Asset saveAsset(Asset asset);

    List<Asset> getAllAssets();

    Asset getAssetById(Long id);

    Asset updateAsset(Long id, Asset asset);

    void deleteAsset(Long id);
}