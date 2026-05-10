import api from './api.js';

export function isAuthenticated() {
    return !!localStorage.getItem('token');
}

export function getCurrentUser() {
    const raw = localStorage.getItem('user');
    return raw ? JSON.parse(raw) : null;
}

export function isAdmin() {
    const user = getCurrentUser();
    return user?.role === 'ADMIN';
}

export async function login(email, password) {
    const data = await api.post('/auth/login', { email, password });
    localStorage.setItem('token', data.token);
    localStorage.setItem('user', JSON.stringify({ name: data.name, email: data.email, role: data.role }));
    return data;
}

export async function register(name, email, password) {
    const data = await api.post('/auth/register', { name, email, password });
    localStorage.setItem('token', data.token);
    localStorage.setItem('user', JSON.stringify({ name: data.name, email: data.email, role: data.role }));
    return data;
}

export function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    localStorage.removeItem('cart');
    window.location.href = '/index.html';
}

export function requireAuth() {
    if (!isAuthenticated()) {
        window.location.href = '/pages/login.html';
    }
}
