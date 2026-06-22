function toggleCart() {
    $('#cartSidebar').toggleClass('open');
    $('#cartOverlay').toggleClass('open');
    if ($('#cartSidebar').hasClass('open')) {
        loadCartSidebar();
    }
}

function loadCartSidebar() {
    fetch('/cart/view', { headers: { 'X-Requested-With': 'XMLHttpRequest' } })
        .then(response => response.json())
        .then(data => {
            const container = document.getElementById('cartItems');
            const totalSpan = document.getElementById('cartTotal');
            if (!container) return;
            if (Array.isArray(data) && data.length > 0) {
                let html = '';
                let total = 0;
                data.forEach(item => {
                    total += item.price * item.quantity;
                    html += `<div class="d-flex align-items-center gap-3 mb-3 pb-2 border-bottom">
                        <img src="${item.image || '/images/default.jpg'}" alt="${item.name}" style="width:60px;height:60px;object-fit:cover;border-radius:8px;">
                        <div class="flex-grow-1">
                            <p class="mb-1 fw-bold">${item.name}</p>
                            <span class="price-current">¥${item.price.toFixed(2)} × ${item.quantity}</span>
                        </div>
                    </div>`;
                });
                container.innerHTML = html;
                if (totalSpan) totalSpan.innerText = '¥' + total.toFixed(2);
            } else {
                container.innerHTML = '<p class="text-center text-muted">购物车是空的</p>';
                if (totalSpan) totalSpan.innerText = '¥0.00';
            }
        })
        .catch(() => {
            document.getElementById('cartItems').innerHTML = '<p class="text-center text-danger">加载失败</p>';
        });
}

function addToCart(goodsId, quantity = 1, btn) {
    fetch('/cart/add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ goodsId, quantity })
    })
    .then(res => res.text())
    .then(text => {
        if (text === 'ok') {
            if (btn) {
                $(btn).text('已添加').css('background', '#27ae60');
                setTimeout(() => { $(btn).text('加入购物车').css('background', ''); }, 800);
            }
            loadCartSidebar(); // 刷新侧边栏
        } else {
            alert(text);
        }
    })
    .catch(() => alert('添加失败'));
}

function focusGoods(goodsId, btn) {
    fetch('/cart/focus', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ goodsId })
    })
    .then(res => res.text())
    .then(text => {
        if (text === 'ok') {
            $(btn).addClass('heart-beat');
            setTimeout(() => $(btn).removeClass('heart-beat'), 400);
        } else {
            alert(text);
        }
    })
    .catch(() => alert('操作失败'));
}

function payOrder(orderId) {
    if (!confirm('确认支付？')) return;
    fetch('/cart/pay/' + orderId, { method: 'POST' })
    .then(res => res.text())
    .then(text => {
        if (text === 'ok') {
            alert('支付成功');
            location.reload();
        } else {
            alert(text);
        }
    })
    .catch(() => alert('支付失败'));
}

function updatePwd() {
    const oldPwd = document.getElementById('oldPwd').value;
    const newPwd = document.getElementById('newPwd').value;
    if (!oldPwd || !newPwd) {
        alert('请填写完整');
        return;
    }
    fetch('/user/updatePwd', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ oldPwd, newPwd })
    })
    .then(res => res.text())
    .then(text => {
        if (text === 'ok') {
            alert('密码已修改，请重新登录');
            window.location.href = '/user/logout';
        } else {
            alert(text);
        }
    })
    .catch(() => alert('修改失败'));
}

// 确保 DOM 加载完后事件可用（如需额外绑定）
document.addEventListener('DOMContentLoaded', () => {
    // 所有函数已全局定义，此处可留空或添加初始化逻辑
});
