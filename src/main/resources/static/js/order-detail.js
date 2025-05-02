document.addEventListener('DOMContentLoaded', () => {
    const container = document.getElementById('order-detail');
    const orderId = new URLSearchParams(location.search).get('id');

    fetch(`/api/v1/order/${orderId}`)
        .then(res => res.json())
        .then(order => {
            const itemsHtml = order.orderItemResponseList.map(item => `
                <li>
                    ${item.name} - ${item.price.toLocaleString()}원 x ${item.quantity}
                </li>
            `).join('');

            container.innerHTML = `
                <p><strong>주문 번호:</strong> ${order.id}</p>
                <p><strong>총 금액:</strong> ${order.totalPrice.toLocaleString()}원</p>
                <p><strong>주문 일시:</strong> ${order.orderAt}</p>
                <ul>${itemsHtml}</ul>
                <button onclick="cancelOrder(${order.id})">❌ 주문 취소</button>
            `;
        })
        .catch(err => {
            console.error('주문 상세 불러오기 실패:', err);
            container.innerHTML = '<p>주문 정보를 불러올 수 없습니다.</p>';
        });
});

function cancelOrder(orderId) {
    if (!confirm('정말 주문을 취소하시겠습니까?')) return;

    fetch(`/api/v1/order/${orderId}`, {
        method: 'DELETE'
    })
        .then(() => {
            alert('주문이 취소되었습니다.');
            window.location.href = '/order-list.html';
        })
        .catch(err => {
            console.error('주문 취소 실패:', err);
            alert('주문 취소에 실패했습니다.');
        });
}
