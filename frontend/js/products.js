import api from './api.js';
import { addToCart, updateCartCount } from './cart.js';

let currentPage = 0;
const pageSize = 12;
let filters = {};

async function loadProducts(page = 0) {
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
        renderPagination(data);
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
            <img src="${p.imageUrl || 'https://via.placeholder.com/300x300?text=Sem+imagem'}"
                 alt="${p.name}" loading="lazy">
            <div class="product-info">
                <h3>${p.name}</h3>
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

function renderPagination(page) {
    const el = document.querySelector('#pagination');
    if (!el) return;

    el.innerHTML = `
        <button id="btn-prev" ${page.first ? 'disabled' : ''}>← Anterior</button>
        <span>Página ${page.number + 1} de ${page.totalPages}</span>
        <button id="btn-next" ${page.last ? 'disabled' : ''}>Próxima →</button>
    `;

    el.querySelector('#btn-prev')?.addEventListener('click', () => loadProducts(currentPage - 1));
    el.querySelector('#btn-next')?.addEventListener('click', () => loadProducts(currentPage + 1));
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
