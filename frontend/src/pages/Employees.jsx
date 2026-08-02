import { useEffect, useState } from "react";
import apiClient from "../api/apiClient";

const emptyEmployee = {
    employeeCode: "",
    firstName: "",
    lastName: "",
    email: "",
    department: "",
    designation: "",
    phone: "",
    joiningDate: "",
    status: "ACTIVE"
};

export default function Employees() {

    const [employees, setEmployees] = useState([]);
    const [form, setForm] = useState(emptyEmployee);
    const [message, setMessage] = useState("");
    const [error, setError] = useState("");

    const loadEmployees = async () => {
        try {
            const response = await apiClient.get("/api/employees");
            setEmployees(response.data);
        } catch (err) {
            console.error(err);
            setError("Could not load employees.");
        }
    };

    useEffect(() => {
        loadEmployees();
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
            await apiClient.post("/api/employees", form);

            setForm(emptyEmployee);
            setMessage("Employee added successfully.");
            setError("");

            await loadEmployees();
        } catch (err) {
            console.error(err);
            setError(
                err.response?.data?.message || "Could not add employee."
            );
        }
    };

    const handleDelete = async id => {
        const confirmed = window.confirm(
            "Are you sure you want to delete this employee?"
        );

        if (!confirmed) {
            return;
        }

        try {
            await apiClient.delete(`/api/employees/${id}`);
            await loadEmployees();
        } catch (err) {
            console.error(err);
            setError(
                err.response?.data?.message || "Could not delete employee."
            );
        }
    };

    return (
        <div className="container py-4">
            <h2 className="mb-4">Employees</h2>

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

            <div className="card shadow-sm mb-4">
                <div className="card-body">
                    <h5 className="mb-3">Add Employee</h5>

                    <form onSubmit={handleSubmit}>
                        <div className="row g-3">
                            {Object.keys(emptyEmployee).map(field => (
                                <div className="col-md-4" key={field}>
                                    <label className="form-label">
                                        {formatLabel(field)}
                                    </label>

                                    <input
                                        className="form-control"
                                        type={field === "joiningDate" ? "date" : "text"}
                                        name={field}
                                        value={form[field]}
                                        onChange={handleChange}
                                        required
                                    />
                                </div>
                            ))}
                        </div>

                        <button className="btn btn-primary mt-3">
                            Add Employee
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
                            <th>Email</th>
                            <th>Department</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                        </thead>

                        <tbody>
                        {employees.map(employee => (
                            <tr key={employee.id}>
                                <td>{employee.id}</td>
                                <td>{employee.employeeCode}</td>
                                <td>
                                    {employee.firstName} {employee.lastName}
                                </td>
                                <td>{employee.email}</td>
                                <td>{employee.department}</td>
                                <td>{employee.status}</td>
                                <td>
                                    <button
                                        className="btn btn-sm btn-danger"
                                        onClick={() => handleDelete(employee.id)}
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