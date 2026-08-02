import { useEffect, useState } from "react";
import apiClient from "../api/apiClient";

const emptyAsset = {
    assetCode: "",
    assetName: "",
    assetType: "",
    manufacturer: "",
    model: "",
    serialNumber: "",
    purchaseDate: "",
    status: "AVAILABLE"
};

export default function Assets() {

    const [assets, setAssets] = useState([]);
    const [form, setForm] = useState(emptyAsset);
    const [error, setError] = useState("");

    const loadAssets = async () => {
        try {
            const response = await apiClient.get("/api/assets");
            setAssets(response.data);
        } catch (err) {
            console.error(err);
            setError("Could not load assets.");
        }
    };

    useEffect(() => {
        loadAssets();
    }, []);

    const handleChange = event => {
        const { name, value } = event.target;

        setForm(current => ({
            ...current,
            [name]: value
        }));
    };

    const handleSubmit = async event => {
        event.preventDefault();

        try {
            await apiClient.post("/api/assets", form);
            setForm(emptyAsset);
            setError("");
            await loadAssets();
        } catch (err) {
            console.error(err);
            setError(
                err.response?.data?.message || "Could not add asset."
            );
        }
    };

    const handleDelete = async id => {
        const confirmed = window.confirm(
            "Are you sure you want to delete this asset?"
        );

        if (!confirmed) {
            return;
        }

        try {
            await apiClient.delete(`/api/assets/${id}`);
            await loadAssets();
        } catch (err) {
            console.error(err);
            setError(
                err.response?.data?.message || "Could not delete asset."
            );
        }
    };

    return (
        <div className="container py-4">
            <h2 className="mb-4">Assets</h2>

            {error && (
                <div className="alert alert-danger">
                    {error}
                </div>
            )}

            <div className="card shadow-sm mb-4">
                <div className="card-body">
                    <h5 className="mb-3">Add Asset</h5>

                    <form onSubmit={handleSubmit}>
                        <div className="row g-3">
                            {Object.keys(emptyAsset).map(field => (
                                <div className="col-md-4" key={field}>
                                    <label className="form-label">
                                        {formatLabel(field)}
                                    </label>

                                    <input
                                        className="form-control"
                                        type={field === "purchaseDate" ? "date" : "text"}
                                        name={field}
                                        value={form[field]}
                                        onChange={handleChange}
                                        required
                                    />
                                </div>
                            ))}
                        </div>

                        <button className="btn btn-primary mt-3">
                            Add Asset
                        </button>
                    </form>
                </div>
            </div>

            <div className="card shadow-sm">
                <div className="card-body table-responsive">
                    <table className="table table-striped align-middle">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Code</th>
                            <th>Name</th>
                            <th>Type</th>
                            <th>Serial Number</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                        </thead>

                        <tbody>
                        {assets.map(asset => (
                            <tr key={asset.id}>
                                <td>{asset.id}</td>
                                <td>{asset.assetCode}</td>
                                <td>{asset.assetName}</td>
                                <td>{asset.assetType}</td>
                                <td>{asset.serialNumber}</td>
                                <td>{asset.status}</td>
                                <td>
                                    <button
                                        className="btn btn-sm btn-danger"
                                        onClick={() => handleDelete(asset.id)}
                                    >
                                        Delete
                                    </button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
}

function formatLabel(value) {
    return value
        .replace(/([A-Z])/g, " $1")
        .replace(/^./, char => char.toUpperCase());
}