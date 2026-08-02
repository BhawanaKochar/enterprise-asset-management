import { useEffect, useState } from "react";
import apiClient from "../api/apiClient";

export default function Dashboard() {

    const [stats, setStats] = useState({
        employees: 0,
        assets: 0,
        assigned: 0,
        available: 0
    });

    const [error, setError] = useState("");

    useEffect(() => {
        const loadDashboard = async () => {
            try {
                const [employeeResponse, assetResponse] = await Promise.all([
                    apiClient.get("/api/employees"),
                    apiClient.get("/api/assets")
                ]);

                const employees = employeeResponse.data;
                const assets = assetResponse.data;

                setStats({
                    employees: employees.length,
                    assets: assets.length,
                    assigned: assets.filter(
                        asset => asset.status?.toUpperCase() === "ASSIGNED"
                    ).length,
                    available: assets.filter(
                        asset => asset.status?.toUpperCase() === "AVAILABLE"
                    ).length
                });
            } catch (err) {
                console.error(err);
                setError("Could not load dashboard data.");
            }
        };

        loadDashboard();
    }, []);

    return (
        <div className="container py-4">
            <h2 className="mb-4">Dashboard</h2>

            {error && (
                <div className="alert alert-danger">
                    {error}
                </div>
            )}

            <div className="row g-3">
                <StatCard title="Employees" value={stats.employees} />
                <StatCard title="Assets" value={stats.assets} />
                <StatCard title="Assigned" value={stats.assigned} />
                <StatCard title="Available" value={stats.available} />
            </div>
        </div>
    );
}

function StatCard({ title, value }) {
    return (
        <div className="col-md-3">
            <div className="card shadow-sm h-100">
                <div className="card-body">
                    <h6 className="text-muted">{title}</h6>
                    <h2>{value}</h2>
                </div>
            </div>
        </div>
    );
}