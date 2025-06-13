document.addEventListener('DOMContentLoaded', function () {
    const urlParams = new URLSearchParams(window.location.search);
    const itemId = urlParams.get('id');

    const itemDetailContainer = document.getElementById('item-detail');
    const orderBtn = document.getElementById('order-btn');

    fetch(`/api/v1/item/${itemId}`)
        .then(response => {
            if (!response.ok) throw new Error('응답 실패');
            return response.json();
        })
        .then(item => {
            itemDetailContainer.innerHTML = `
                <div class="item-image">
                    <img src="${item.itemImageUrl}" alt="${item.name}">
                </div>
                <div class="item-info">
                    <h2 class="item-name">${item.name}</h2>
                    <p class="item-description">${item.description}</p>
                    <div class="item-price">가격: ${item.price.toLocaleString()}원</div>
                    <div class="item-stock">재고: ${item.stock}개</div>
                </div>
            `;
        })
        .catch(error => {
            console.error('상품 상세 가져오기 오류:', error);
            itemDetailContainer.innerHTML = '<div class="loading">상품을 불러오지 못했습니다.</div>';
        });

    // 안전하게 버튼 가져오기
    const cartBtn = document.getElementById('cart-btn');
    const purchaseBtn = document.getElementById('purchase-btn');

    if (cartBtn) {
        cartBtn.addEventListener('click', function () {
            let cart = JSON.parse(localStorage.getItem('cart')) || [];

            const existingItem = cart.find(item => item.itemId === Number(itemId));
            if (existingItem) {
                existingItem.quantity += 1;
            } else {
                cart.push({ itemId: Number(itemId), quantity: 1 });
            }

            localStorage.setItem('cart', JSON.stringify(cart));
            alert('장바구니에 담았습니다!');
        });
    }

    if (purchaseBtn) {
        purchaseBtn.addEventListener('click', function () {
            alert('구매 기능은 아직 준비 중입니다!');
        });
    }

    // 주문 버튼 클릭 시
    if (orderBtn) {
        orderBtn.addEventListener('click', function () {
            const orderData = {
                orderItemList: [{
                    itemId: Number(itemId),
                    quantity: 1
                }]
            };

            fetch('/api/v1/order', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${localStorage.getItem('accessToken')}` // 로그인 상태 필요 시
                },
                body: JSON.stringify(orderData)
            })
                .then(response => {
                    if (!response.ok) throw new Error('주문 실패');
                    return response.json();
                })
                .then(data => {
                    alert('주문이 완료되었습니다!');
                    window.location.href = `/order-detail.html?id=${data.id}`;
                })
                .catch(error => {
                    alert('로그인 후 주문해 주세요.');
                    console.error(error);
                });
        });
    }
});
