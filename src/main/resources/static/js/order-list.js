document.addEventListener('DOMContentLoaded', () => {
    const container = document.getElementById('order-list');

    const page = 0;
    const size = 3;

    fetch(`/api/v1/order?page=${page}&size=${size}`)
        .then(res => res.json())
        .then(result => {
            const orders = result.content;

            if (!orders || orders.length === 0) {
                container.innerHTML = '<p>주문 내역이 없습니다.</p>';
                return;
            }

            orders.forEach(order => {
                const div = document.createElement('div');
                div.className = 'order-box';
                div.innerHTML = `
                    <p><strong>주문 번호:</strong> ${order.id}</p>
                    <p><strong>총 금액:</strong> ${order.totalPrice.toLocaleString()}원</p>
                    <p><strong>주문 일시:</strong> ${order.orderAt}</p>
                    <button onclick="location.href='/order-detail.html?id=${order.id}'">상세 보기</button>
                `;
                container.appendChild(div);
            });

            // ✅ 필요하면 페이지 정보도 출력 가능
            console.log(`현재 페이지: ${result.number + 1} / 총 페이지: ${result.totalPages}`);
        })
        .catch(err => {
            console.error('주문 목록 가져오기 실패:', err);
            container.innerHTML = '<p>주문 정보를 불러올 수 없습니다.</p>';
        });
});
