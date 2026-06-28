// 加载购物车侧边栏
function loadCartSidebar() {
    var xhr = new XMLHttpRequest();
    xhr.open('GET', '/cart/viewJson', true);
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            var container = document.getElementById('cartItems');
            var totalSpan = document.getElementById('cartTotal');
            if (!container) return;
            
            if (xhr.status === 200) {
                try {
                    var items = JSON.parse(xhr.responseText);
                    if (Array.isArray(items) && items.length > 0) {
                        var html = '';
                        var total = 0;
                        items.forEach(function(item) {
                            var price = parseFloat(item.price) || 0;
                            var qty = parseInt(item.quantity) || 0;
                            total += price * qty;
                            var goods = item.goods || {};
                            var pic = goods.picture || '';
                            html += '<div class="d-flex align-items-center gap-3 mb-3 pb-2 border-bottom">';
                            html += '<img src="/images/' + pic + '" alt="" style="width:60px;height:60px;object-fit:cover;border-radius:8px;" onerror="this.src=\'/images/default.jpg\'">';
                            html += '<div class="flex-grow-1">';
                            html += '<p class="mb-1 fw-bold">' + (goods.name || '') + '</p>';
                            html += '<span class="price-current">¥' + price.toFixed(2) + ' × ' + qty + '</span>';
                            html += '</div></div>';
                        });
                        container.innerHTML = html;
                        if (totalSpan) totalSpan.innerText = '¥' + total.toFixed(2);
                    } else {
                        container.innerHTML = '<p class="text-center text-muted py-4">购物车是空的</p>';
                        if (totalSpan) totalSpan.innerText = '¥0.00';
                    }
                } catch(e) {
                    container.innerHTML = '<p class="text-center text-muted py-4">加载失败</p>';
                }
            } else {
                container.innerHTML = '<p class="text-center text-muted py-4">请先登录</p>';
                if (totalSpan) totalSpan.innerText = '¥0.00';
            }
        }
    };
    xhr.send();
}

// 切换购物车侧边栏显示
function toggleCart() {
    var sidebar = document.getElementById('cartSidebar');
    var overlay = document.getElementById('cartOverlay');
    if (sidebar && overlay) {
        sidebar.classList.toggle('open');
        overlay.classList.toggle('open');
        if (sidebar.classList.contains('open')) {
            loadCartSidebar();
        }
    }
}

// 添加到购物车
function addToCart(goodsId, quantity, btn) {
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/cart/add', true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                var response = xhr.responseText.trim();
                if (response === 'ok') {
                    if (btn) {
                        var originalText = btn.textContent;
                        btn.textContent = '已添加';
                        btn.style.background = '#27ae60';
                        setTimeout(function() { 
                            btn.textContent = originalText;
                            btn.style.background = '';
                        }, 800);
                    }
                    loadCartSidebar();
                } else if (response === 'need-login') {
                    alert('请先登录后再操作');
                    window.location.href = '/user/toLogin';
                } else {
                    alert(response || '添加失败，请先登录');
                    window.location.href = '/user/toLogin';
                }
            } else if (xhr.status === 302 || xhr.status === 301) {
                alert('请先登录');
                window.location.href = '/user/toLogin';
            } else {
                alert('添加购物车失败，请重试');
            }
        }
    };
    xhr.send('goodsId=' + goodsId + '&quantity=' + quantity);
}

// 收藏/取消收藏
function focusGoods(goodsId, btn) {
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/cart/focus', true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                var response = xhr.responseText.trim();
                if (response === 'ok') {
                    if (btn) {
                        btn.classList.add('heart-beat');
                        setTimeout(function() { 
                            btn.classList.remove('heart-beat'); 
                        }, 400);
                    }
                } else {
                    alert(response || '请先登录');
                    window.location.href = '/user/toLogin';
                }
            } else if (xhr.status === 302 || xhr.status === 301) {
                alert('请先登录');
                window.location.href = '/user/toLogin';
            } else {
                alert('收藏失败，请重试');
            }
        }
    };
    xhr.send('goodsId=' + goodsId);
}

// 支付订单
function payOrder(orderId) {
    if (!confirm('确认支付？')) return;
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/cart/pay/' + orderId, true);
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                if (xhr.responseText.trim() === 'ok') {
                    alert('支付成功');
                    location.reload();
                } else {
                    alert(xhr.responseText || '支付失败');
                }
            } else {
                alert('支付失败');
            }
        }
    };
    xhr.send();
}

// 修改密码
function updatePwd() {
    var oldPwd = document.getElementById('oldPwd').value;
    var newPwd = document.getElementById('newPwd').value;
    if (!oldPwd || !newPwd) {
        alert('请填写完整');
        return;
    }
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/user/updatePwd', true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                if (xhr.responseText.trim() === 'ok') {
                    alert('密码已修改，请重新登录');
                    window.location.href = '/user/logout';
                } else {
                    alert(xhr.responseText || '修改失败');
                }
            } else {
                alert('修改失败');
            }
        }
    };
    xhr.send('oldPwd=' + encodeURIComponent(oldPwd) + '&newPwd=' + encodeURIComponent(newPwd));
}

// 删除购物车项
function deleteCartItem(itemId) {
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/cart/delete/' + itemId, true);
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                location.reload();
            } else {
                alert('删除失败');
            }
        }
    };
    xhr.send();
}

// 清空购物车
function clearCart() {
    if (!confirm('确定要清空购物车吗？')) return;
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/cart/clear', true);
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                location.reload();
            } else {
                alert('清空失败');
            }
        }
    };
    xhr.send();
}

// 提交订单
function submitOrder() {
    if (!confirm('确认提交订单吗？')) return;
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/cart/submitOrder', true);
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                var res = xhr.responseText.trim();
                if (res === 'ok') {
                    alert('订单提交成功');
                    location.reload();
                } else if (res === 'empty') {
                    alert('购物车为空');
                } else if (res === 'stock') {
                    alert('库存不足');
                } else {
                    alert(res || '提交失败');
                }
            } else {
                alert('提交失败，请重试');
            }
        }
    };
    xhr.send();
}

// 页面加载完成后初始化
document.addEventListener('DOMContentLoaded', function() {
    // 确保Bootstrap下拉菜单正常工作
    var dropdownElementList = document.querySelectorAll('[data-bs-toggle="dropdown"]');
    dropdownElementList.forEach(function(dropdownToggleEl) {
        // 检查是否已经有Bootstrap实例
        if (typeof bootstrap !== 'undefined' && bootstrap.Dropdown) {
            new bootstrap.Dropdown(dropdownToggleEl);
        }
    });
    
    // 如果是购物车页面，加载购物车数据
    if (document.getElementById('cartItems')) {
        loadCartSidebar();
    }
});