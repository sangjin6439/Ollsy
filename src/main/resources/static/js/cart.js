document.addEventListener('DOMContentLoaded', function () {
    const cartItemsContainer = document.getElementById('cart-items');
    const totalPriceEl = document.getElementById('total-price');
    const orderBtn = document.getElementById('order-btn');

    renderCart();

    function renderCart() {
        cartItemsContainer.innerHTML = '';
        let cart = JSON.parse(localStorage.getItem('cart')) || [];

        if (cart.length === 0) {
            cartItemsContainer.innerHTML = '<p class="empty-cart">장바구니가 비어 있습니다.</p>';
            totalPriceEl.textContent = '0';
            return;
        }

        let total = 0;

        Promise.all(cart.map(item =>
            fetch(`/api/v1/item/${item.itemId}`)
                .then(res => res.json())
                .then(data => {
                    const itemTotal = data.price * item.quantity;
                    total += itemTotal;

                    const itemDiv = document.createElement('div');
                    itemDiv.className = 'cart-item';
                    itemDiv.innerHTML = `
                        <div><img src="${data.itemImageUrl}" alt="${data.name}"></div>
                        <div>${data.name}</div>
                        <div>${item.quantity}개</div>
                        <div>${data.price.toLocaleString()}원</div>
                        <div>${itemTotal.toLocaleString()}원</div>
                        <div><button class="delete-btn" data-id="${item.itemId}">삭제</button></div>
                    `;
                    cartItemsContainer.appendChild(itemDiv);
                })
        )).then(() => {
            totalPriceEl.textContent = total.toLocaleString();
            attachDeleteHandlers();
        });
    }

    function attachDeleteHandlers() {
        const deleteButtons = document.querySelectorAll('.delete-btn');
        deleteButtons.forEach(btn => {
            btn.addEventListener('click', function () {
                const itemId = Number(this.getAttribute('data-id'));
                let cart = JSON.parse(localStorage.getItem('cart')) || [];
                cart = cart.filter(item => item.itemId !== itemId);
                localStorage.setItem('cart', JSON.stringify(cart));
                renderCart();
            });
        });
    }

    //주문하기 버튼 클릭
    orderBtn.addEventListener('click', function () {
        const cart = JSON.parse(localStorage.getItem('cart')) || [];

        if (cart.length === 0) {
            alert('장바구니가 비어 있습니다.');
            return;
        }

        const accessToken = localStorage.getItem('accessToken');
        if (!accessToken) {
            alert('로그인이 필요합니다.');
            return;
        }

        fetch('/api/v1/order', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + accessToken
            },
            body: JSON.stringify({ orderItemList: cart })
        })
            .then(res => {
                if (!res.ok) throw new Error('주문 실패');
                return res.json();
            })
            .then(data => {
                alert('주문 완료!');
                localStorage.removeItem('cart');
                window.location.href = `/order-detail.html?id=${data.id}`;
            })
            .catch(error => {
                console.error('주문 오류:', error);
                alert('주문 중 오류가 발생했습니다.');
            });
    });
});
