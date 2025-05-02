document.addEventListener('DOMContentLoaded', function () {
    const dropdownBtn = document.querySelector('.dropdown-btn');
    const container = document.getElementById('items-container');
    const loginLink = document.getElementById('login-link');

    //로그인 상태 확인
    const accessToken = localStorage.getItem('accessToken');
    if (accessToken) {
        fetch('/api/v1/user', {
            headers: {
                'Authorization': 'Bearer ' + accessToken
            }
        })
            .then(res => {
                if (!res.ok) throw new Error('인증 실패');
                return res.json();
            })
            .then(user => {
                // ✅ "내 정보"로 텍스트 및 링크 변경
                loginLink.textContent = '내 정보';
                loginLink.href = '/my-page.html';
            })
            .catch(err => {
                console.warn('유저 인증 실패:', err);
                localStorage.removeItem('accessToken'); // 토큰 무효 시 제거
            });
    }

    //신상품 드롭다운
    dropdownBtn.addEventListener('click', function () {
        console.log('신상품 메뉴 클릭됨');
    });

    //상품 목록 불러오기
    fetch('/api/v1/item/new')
        .then(response => response.json())
        .then(items => {
            items.slice(0, 3).forEach(item => {
                const itemDiv = document.createElement('div');
                itemDiv.className = 'product-card';
                itemDiv.innerHTML = `
                    <div class="product-image">
                        <img src="${item.itemImageUrl}" alt="${item.name}">
                    </div>
                    <div class="product-info">
                        <h3>${item.name}</h3>
                        <p>${item.price.toLocaleString()}원</p>
                    </div>
                `;
                itemDiv.addEventListener('click', () => {
                    window.location.href = `/item-detail.html?id=${item.id}`;
                });
                container.appendChild(itemDiv);
            });
        })
        .catch(error => console.error('상품 목록 오류:', error));
});
