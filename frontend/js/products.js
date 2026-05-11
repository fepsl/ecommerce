import api from './api.js';
import { addToCart, updateCartCount } from './cart.js';

let currentPage = 0;
const pageSize = 12;
let filters = {};

function renderSkeletons(count = 8) {
    const grid = document.querySelector('#products-grid');
    if (!grid) return;
    grid.innerHTML = Array(count).fill(`
        <div class="product-card">
            <div class="skeleton skeleton-img"></div>
            <div class="product-info">
                <div class="skeleton skeleton-line" style="width:70%"></div>
                <div class="skeleton skeleton-line" style="width:40%"></div>
            </div>
        </div>
    `).join('');
}

async function loadProducts(page = 0) {
    renderSkeletons();

    const params = new URLSearchParams({
        page,
        size: pageSize,
        ...(filters.name     && { name:     filters.name }),
        ...(filters.category && { category: filters.category }),
        ...(filters.minPrice && { minPrice: filters.minPrice }),
        ...(filters.maxPrice && { maxPrice: filters.maxPrice }),
    });

    try {
        const data = await api.get(`/products?${params}`);
        renderProducts(data.content);
        renderPagination(data.number, data.totalPages);
        currentPage = page;
        return data;
    } catch (err) {
        const grid = document.querySelector('#products-grid');
        if (grid) grid.innerHTML = `<p class="empty-state">Não foi possível carregar os produtos. Tente novamente.</p>`;
    }
}

function renderProducts(products) {
    const grid = document.querySelector('#products-grid');
    if (!grid) return;

    if (products.length === 0) {
        grid.innerHTML = '<p class="empty-state">Nenhum produto encontrado.</p>';
        return;
    }

    grid.innerHTML = products.map(p => `
        <div class="product-card">
            <a href="product.html?id=${p.id}">
                <img src="${p.imageUrl || ''}"
                     alt="${p.name}"
                     loading="lazy"
                     style="background:var(--surface-3)"
                     onerror="this.onerror=null;this.removeAttribute('src')">
            </a>
            <div class="product-info">
                <a href="product.html?id=${p.id}"><h3>${p.name}</h3></a>
                <p class="category">${p.category?.name ?? ''}</p>
                <p class="price">R$ ${p.price.toFixed(2).replace('.', ',')}</p>
                <p class="stock ${p.stock === 0 ? 'out' : ''}">
                    ${p.stock > 0 ? `${p.stock} em estoque` : 'Sem estoque'}
                </p>
                <button class="btn btn-primary add-to-cart"
                        data-id="${p.id}"
                        data-name="${p.name}"
                        data-price="${p.price}"
                        ${p.stock === 0 ? 'disabled' : ''}>
                    Adicionar ao carrinho
                </button>
            </div>
        </div>
    `).join('');

    grid.querySelectorAll('.add-to-cart').forEach(btn => {
        btn.addEventListener('click', () => {
            addToCart({ id: btn.dataset.id, name: btn.dataset.name, price: parseFloat(btn.dataset.price) });
            showToast(`"${btn.dataset.name}" adicionado ao carrinho`);
        });
    });
}

function renderPagination(current, total) {
    const el = document.querySelector('#pagination');
    if (!el) return;

    if (total <= 1) { el.innerHTML = ''; return; }

    // Páginas vizinhas: atual ± 2
    const pages = [];
    for (let i = Math.max(0, current - 2); i <= Math.min(total - 1, current + 2); i++) {
        pages.push(i);
    }

    const pageButtons = pages.map(p => `
        <button class="btn-page ${p === current ? 'active' : ''}" data-page="${p}">${p + 1}</button>
    `).join('');

    el.innerHTML = `
        <div class="pagination">
            <button class="btn-page" data-page="${current - 1}" ${current === 0 ? 'disabled' : ''}>← Anterior</button>
            ${pageButtons}
            <button class="btn-page" data-page="${current + 1}" ${current === total - 1 ? 'disabled' : ''}>Próximo →</button>
        </div>
    `;

    el.querySelectorAll('.btn-page:not(:disabled)').forEach(btn => {
        btn.addEventListener('click', () => loadProducts(Number(btn.dataset.page)));
    });
}

function showToast(message) {
    const toast = document.createElement('div');
    toast.className = 'toast';
    toast.textContent = message;
    document.body.appendChild(toast);
    setTimeout(() => toast.remove(), 3000);
}

async function loadCategories() {
    try {
        const categories = await api.get('/categories');
        const select = document.querySelector('#filter-category');
        if (!select) return;
        select.innerHTML = '<option value="">Todas as categorias</option>' +
            categories.map(c => `<option value="${c.id}">${c.name}</option>`).join('');
    } catch {
        // filtro de categoria simplesmente fica sem opções se a API falhar
    }
}

document.addEventListener('DOMContentLoaded', async () => {
    updateCartCount();
    await loadCategories();
    await loadProducts();

    document.querySelector('#filter-form')?.addEventListener('submit', e => {
        e.preventDefault();
        filters = {
            name:     document.querySelector('#filter-name')?.value || '',
            category: document.querySelector('#filter-category')?.value || '',
            minPrice: document.querySelector('#filter-min')?.value || '',
            maxPrice: document.querySelector('#filter-max')?.value || '',
        };
        currentPage = 0;
        loadProducts(0);
    });
});
