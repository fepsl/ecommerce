import api from './api.js';
import { requireAuth } from './auth.js';

const CART_KEY = 'cart';

export function getCart() {
    const raw = localStorage.getItem(CART_KEY);
    return raw ? JSON.parse(raw) : [];
}

export function addToCart(product) {
    const cart = getCart();
    const existing = cart.find(item => item.id === product.id);

    if (existing) {
        existing.quantity += 1;
    } else {
        cart.push({ ...product, quantity: 1 });
    }

    localStorage.setItem(CART_KEY, JSON.stringify(cart));
    updateCartCount();
}

export function removeFromCart(productId) {
    const cart = getCart().filter(item => item.id !== productId);
    localStorage.setItem(CART_KEY, JSON.stringify(cart));
    updateCartCount();
}

export function updateQuantity(productId, quantity) {
    const cart = getCart();
    const item = cart.find(i => i.id === productId);
    if (item) {
        item.quantity = Math.max(1, quantity);
        localStorage.setItem(CART_KEY, JSON.stringify(cart));
    }
    updateCartCount();
}

export function clearCart() {
    localStorage.removeItem(CART_KEY);
    updateCartCount();
}

export function getCartTotal() {
    return getCart().reduce((total, item) => total + item.price * item.quantity, 0);
}

export function updateCartCount() {
    const count = getCart().reduce((sum, item) => sum + item.quantity, 0);
    const el = document.querySelector('#cart-count');
    if (el) el.textContent = count;
}

export async function checkout() {
    requireAuth();
    const cart = getCart();
    if (cart.length === 0) throw new Error('Carrinho vazio');

    const items = cart.map(item => ({ productId: item.id, quantity: item.quantity }));
    const order = await api.post('/orders', { items });
    clearCart();
    return order;
}
