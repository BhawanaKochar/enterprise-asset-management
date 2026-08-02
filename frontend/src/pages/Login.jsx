export default function Login() {

    const handleLogin = () => {
        window.location.href =
            "http://localhost:8084/saml2/authenticate/authentication-service";
    };

    return (
        <div className="container py-5 text-center">
            <div className="card shadow-sm mx-auto login-card">
                <div className="card-body p-5">
                    <h1 className="mb-3">Enterprise Asset Management</h1>

                    <p className="text-muted mb-4">
                        Sign in using the company identity provider.
                    </p>

                    <button
                        className="btn btn-primary btn-lg"
                        onClick={handleLogin}
                    >
                        Login with SAML
                    </button>
                </div>
            </div>
        </div>
    );
}