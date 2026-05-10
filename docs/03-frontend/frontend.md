# Frontend

> [[index|← Voltar ao índice]]

---

## Stack

HTML5 + CSS3 + JavaScript ES Modules puro (sem framework).

**Por quê sem framework?** Ver [[decisoes-tecnicas]] (ADR-08). O foco do portfólio é o backend; o frontend existe para demonstrar consumo de API REST com autenticação JWT.

---

## Estrutura de arquivos

```
frontend/
├── index.html          → página inicial / vitrine de produtos
├── products.html       → listagem paginada com filtros
├── product.html        → detalhe de um produto
├── cart.html           → carrinho de compras
├── orders.html         → meus pedidos
├── login.html          → formulário de login
├── register.html       → formulário de cadastro
├── css/
│   └── styles.css      → tema dark + variáveis CSS + responsivo
└── js/
    ├── api.js          → HTTP client (fetch wrapper com token automático)
    ├── auth.js         → login, register, logout, requireAuth
    ├── cart.js         → gerenciamento de carrinho no localStorage
    └── products.js     → listagem com filtros e paginação
```

---

## Módulos JavaScript

### api.js — HTTP Client

Centraliza todas as chamadas à API. Injeta o token JWT automaticamente em cada request e trata erros de autenticação (401 redireciona para login).

```javascript
// Padrão de uso
const response = await api.get('/products?page=0&size=12');
const product  = await api.post('/orders', { items: [...] });
```

Internamente:
```javascript
const BASE_URL = 'http://localhost:8080';

async function request(method, path, body = null) {
    const token = localStorage.getItem('token');
    const headers = { 'Content-Type': 'application/json' };
    if (token) headers['Authorization'] = `Bearer ${token}`;

    const res = await fetch(`${BASE_URL}${path}`, {
        method,
        headers,
        body: body ? JSON.stringify(body) : null
    });

    if (res.status === 401) {
        localStorage.removeItem('token');
        window.location.href = '/login.html';
        return;
    }
    return res.json();
}

export const api = {
    get:    (path)       => request('GET',    path),
    post:   (path, body) => request('POST',   path, body),
    put:    (path, body) => request('PUT',    path, body),
    delete: (path)       => request('DELETE', path),
};
```

---

### auth.js — Autenticação

```javascript
// Salva token após login/register
export function saveAuth(token, name, role) {
    localStorage.setItem('token', token);
    localStorage.setItem('userName', name);
    localStorage.setItem('userRole', role);
}

// Redireciona se não estiver autenticado
export function requireAuth() {
    if (!localStorage.getItem('token')) {
        window.location.href = '/login.html';
    }
}

// Limpa token no logout
export function logout() {
    localStorage.clear();
    window.location.href = '/login.html';
}
```

---

### cart.js — Carrinho

O carrinho é armazenado inteiramente no `localStorage` como array JSON. O checkout envia os itens para `POST /orders`.

```javascript
// Estrutura do carrinho no localStorage
// Key: 'cart'  Value: JSON.stringify(items)
// items: [{ productId, name, price, quantity, imageUrl }, ...]

export function addToCart(product) { ... }
export function removeFromCart(productId) { ... }
export function clearCart() { ... }
export function getCart() { ... }
export async function checkout() {
    const items = getCart().map(item => ({
        productId: item.productId,
        quantity: item.quantity
    }));
    return api.post('/orders', { items });
}
```

---

### products.js — Listagem e Filtros

Gerencia a listagem paginada com filtros de nome, categoria e faixa de preço.

```javascript
// Parâmetros passados para GET /products
const params = new URLSearchParams({
    page:     currentPage,
    size:     12,
    name:     nameFilter,
    category: categoryFilter,
    minPrice: minPriceFilter,
    maxPrice: maxPriceFilter,
});
const data = await api.get(`/products?${params}`);
// data.content → array de produtos
// data.totalPages → para paginação
```

---

## CSS — Tema Dark

```css
:root {
    --bg-primary:   #0f0f0f;
    --bg-secondary: #1a1a1a;
    --bg-card:      #242424;
    --text-primary: #ffffff;
    --text-muted:   #999999;
    --accent:       #6366f1;    /* indigo — cor principal */
    --accent-hover: #4f46e5;
    --success:      #22c55e;
    --danger:       #ef4444;
    --border:       #333333;
    --radius:       8px;
}
```

Layout responsivo com CSS Grid e Flexbox. Sem bibliotecas CSS externas.

---

## Fluxo de autenticação no frontend

```
1. Usuário preenche login.html
2. POST /auth/login → recebe { token, name, role }
3. saveAuth() → armazena em localStorage
4. Redirect para index.html

5. Qualquer página que precise de auth chama requireAuth()
6. api.js injeta header Authorization em todo fetch
7. Se token expirado (401) → redirect automático para login.html
```

---

## TODO — melhorias pendentes

- [ ] Tratamento de erro consistente (mostrar mensagem de erro amigável ao usuário)
- [ ] Loading state nas chamadas de API
- [ ] Paginação de produtos no frontend (botões próx/ant)
- [ ] Validação de formulários no lado cliente antes do submit

---

## Relacionado

- [[api-endpoints]] — endpoints que o frontend consome
- [[autenticacao-jwt]] — como o token é gerado e validado
- [[docker]] — como o frontend é servido via Nginx
