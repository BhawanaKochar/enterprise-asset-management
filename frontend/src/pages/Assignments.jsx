import { useEffect, useState } from "react";
import apiClient from "../api/apiClient";

export default function Assignments() {

    const [employees, setEmployees] = useState([]);
    const [assets, setAssets] = useState([]);
    const [employeeId, setEmployeeId] = useState("");
    const [assetId, setAssetId] = useState("");
    const [message, setMessage] = useState("");
    const [error, setError] = useState("");

    const loadData = async () => {
        try {
            const [employeeResponse, assetResponse] = await Promise.all([
                apiClient.get("/api/employees"),
                apiClient.get("/api/assets")
            ]);

            setEmployees(employeeResponse.data);

            setAssets(
                assetResponse.data.filter(
                    asset => asset.status?.toUpperCase() === "AVAILABLE"
                )
            );
        } catch (err) {
            console.error(err);
            setError("Could not load assignment data.");
        }
    };

    useEffect(() => {
        loadData();
    }, []);

    const assignAsset = async event => {
        event.preventDefault();

        try {
            await apiClient.post(
                `/api/assets/${assetId}/assign/${employeeId}`
            );

            setMessage("Asset assigned successfully.");
            setError("");
            setEmployeeId("");
            setAssetId("");

            await loadData();
        } catch (err) {
            console.error(err);
            setError(
                err.response?.data?.message || "Could not assign asset."
            );
        }
    };

    return (
        <div className="container py-4">
            <h2 className="mb-4">Assign Asset</h2>

            {message && (
                <div className="alert alert-success">
                    {message}
                </div>
            )}

            {error && (
                <div className="alert alert-danger">
                    {error}
                </div>
            )}

            <div className="card shadow-sm">
                <div className="card-body">
                    <form onSubmit={assignAsset}>
                        <div className="mb-3">
                            <label className="form-label">
                                Employee
                            </label>

                            <select
                                className="form-select"
                                value={employeeId}
                                onChange={event =>
                                    setEmployeeId(event.target.value)
                                }
                                required
                            >
                                <option value="">Select employee</option>

                                {employees.map(employee => (
                                    <option
                                        key={employee.id}
                                        value={employee.id}
                                    >
                                        {employee.firstName} {employee.lastName}
                                    </option>
                                ))}
                            </select>
                        </div>

                        <div className="mb-3">
                            <label className="form-label">
                                Available Asset
                            </label>

                            <select
                                className="form-select"
                                value={assetId}
                                onChange={event =>
                                    setAssetId(event.target.value)
                                }
                                required
                            >
                                <option value="">Select asset</option>

                                {assets.map(asset => (
                                    <option
                                        key={asset.id}
                                        value={asset.id}
                                    >
                                        {asset.assetCode}
                                    </option>
                                ))}
                            </select>
                        </div>

                        <button className="btn btn-primary">
                            Assign Asset
                        </button>
                    </form>
                </div>
            </div>
        </div>
    );
}