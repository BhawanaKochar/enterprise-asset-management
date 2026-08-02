import { NavLink } from "react-router-dom";

export default function Navbar() {
    return (
        <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
            <div className="container">
                <NavLink className="navbar-brand" to="/dashboard">
                    EAM
                </NavLink>

                <div className="navbar-nav">
                    <NavLink className="nav-link" to="/dashboard">
                        Dashboard
                    </NavLink>

                    <NavLink className="nav-link" to="/employees">
                        Employees
                    </NavLink>

                    <NavLink className="nav-link" to="/assets">
                        Assets
                    </NavLink>

                    <NavLink className="nav-link" to="/assignments">
                        Assignments
                    </NavLink>

                    <NavLink className="nav-link" to="/login">
                        Login
                    </NavLink>
                </div>
            </div>
        </nav>
    );
}