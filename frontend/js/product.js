import api from './api.js';
import { addToCart } from './cart.js';

function showToast(message) {
    const toast = document.createElement('div');
    toast.className = 'toast';
    toast.textContent = message;
    document.body.appendChild(toast);
    setTimeout(() => toast.remove(), 3000);
}

function cardHtml(p) {
    return `
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
        </div>`;
}

function bindCartButtons(container) {
    container.querySelectorAll('.add-to-cart').forEach(btn => {
        btn.addEventListener('click', () => {
            addToCart({ id: btn.dataset.id, name: btn.dataset.name, price: parseFloat(btn.dataset.price) });
            showToast(`"${btn.dataset.name}" adicionado ao carrinho`);
        });
    });
}

async function loadRelated(categoryId, currentId) {
    try {
        const data = await api.get(`/products?category=${categoryId}&size=4`);
        const related = data.content.filter(p => p.id !== currentId).slice(0, 3);
        if (related.length === 0) return;

        const section = document.querySelector('#related-section');
        const grid = document.querySelector('#related-grid');
        grid.innerHTML = related.map(cardHtml).join('');
        bindCartButtons(grid);
        section.style.display = '';
    } catch {
        // seção simplesmente não aparece se relacionados falharem
    }
}

document.addEventListener('DOMContentLoaded', async () => {
    const id = new URLSearchParams(window.location.search).get('id');
    const container = document.querySelector('#product-container');

    if (!id) {
        container.innerHTML = `
            <div style="padding:3rem 0">
                <div class="empty-state">
                    <p class="empty-title">Este produto não está disponível.</p>
                    <p class="empty-sub">O link pode estar incorreto ou o produto foi removido.</p>
                    <a href="products.html" class="btn btn-secondary">← Voltar para produtos</a>
                </div>
            </div>`;
        return;
    }

    try {
        const p = await api.get(`/products/${id}`);

        document.title = `${p.name} — VELN`;

        container.innerHTML = `
            <div class="product-detail">
                <div class="img-zoom-wrap">
                    <img id="product-img"
                         src="${p.imageUrl || ''}"
                         alt="${p.name}"
                         style="width:100%;aspect-ratio:3/4;object-fit:cover;display:block;background:var(--surface-3)"
                         onerror="this.onerror=null;this.removeAttribute('src')">
                </div>
                <div class="product-detail-info">
                    <p class="breadcrumb"><a href="products.html">← Produtos</a></p>
                    <p class="detail-category">${p.category?.name ?? ''}</p>
                    <h1>${p.name}</h1>
                    <p class="detail-price">R$ ${p.price.toFixed(2).replace('.', ',')}</p>
                    <p class="detail-description">${p.description || ''}</p>
                    <p class="detail-stock ${p.stock === 0 ? 'out' : ''}">
                        ${p.stock > 0 ? `${p.stock} em estoque` : 'Sem estoque'}
                    </p>
                    <button id="add-btn"
                            class="btn btn-primary"
                            data-id="${p.id}"
                            data-name="${p.name}"
                            data-price="${p.price}"
                            ${p.stock === 0 ? 'disabled' : ''}>
                        Adicionar ao carrinho
                    </button>
                </div>
            </div>`;

        document.querySelector('#add-btn').addEventListener('click', e => {
            const btn = e.currentTarget;
            addToCart({ id: btn.dataset.id, name: btn.dataset.name, price: parseFloat(btn.dataset.price) });
            showToast(`"${btn.dataset.name}" adicionado ao carrinho`);
        });

        if (p.category?.id) {
            await loadRelated(p.category.id, p.id);
        }

    } catch {
        container.innerHTML = `
            <div style="padding:3rem 0">
                <div class="empty-state">
                    <p class="empty-title">Este produto não está disponível.</p>
                    <p class="empty-sub">O produto pode ter sido removido ou o link está incorreto.</p>
                    <a href="products.html" class="btn btn-secondary">← Voltar para produtos</a>
                </div>
            </div>`;
    }
});
