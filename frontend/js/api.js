const API_BASE = (window.APP_CONFIG && window.APP_CONFIG.apiBase) || 'http://localhost:8080';

function getToken() {
    return localStorage.getItem('token');
}

async function request(method, path, body = null) {
    const headers = { 'Content-Type': 'application/json' };
    const token = getToken();
    if (token) headers['Authorization'] = `Bearer ${token}`;

    const options = { method, headers };
    if (body) options.body = JSON.stringify(body);

    const res = await fetch(`${API_BASE}${path}`, options);

    if (res.status === 401) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        window.location.href = '/pages/login.html';
        return;
    }

    const data = res.status !== 204 ? await res.json() : null;

    if (!res.ok) {
        throw new Error(data?.message || 'Erro na requisição');
    }

    return data;
}

const api = {
    get:    (path)         => request('GET',    path),
    post:   (path, body)   => request('POST',   path, body),
    put:    (path, body)   => request('PUT',    path, body),
    delete: (path)         => request('DELETE', path),
};

export default api;
