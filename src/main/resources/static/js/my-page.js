document.addEventListener('DOMContentLoaded', function () {
    const accessToken = localStorage.getItem('accessToken');

    if (!accessToken) {
        alert('로그인이 필요합니다.');
        window.location.href = '/login';
        return;
    }

    fetch('/api/v1/user', {
        headers: {
            'Authorization': 'Bearer ' + accessToken
        }
    })
        .then(res => {
            if (!res.ok) throw new Error('유저 정보 로딩 실패');
            return res.json();
        })
        .then(user => {
            document.getElementById('user-name').textContent = user.name;
            document.getElementById('user-nickname').textContent = user.nickname;
            document.getElementById('user-email').textContent = user.email;

            document.getElementById('nickname-update-btn').addEventListener('click', function () {
                const newNickname = document.getElementById('nickname-input').value.trim();
                if (newNickname.length < 2 || newNickname.length > 8) {
                    alert('닉네임은 2~8자 사이여야 합니다.');
                    return;
                }

                fetch(`/api/v1/user/${user.id}`, {
                    method: 'PATCH',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + accessToken
                    },
                    body: JSON.stringify({ nickname: newNickname })
                })
                    .then(res => {
                        if (!res.ok) throw new Error('닉네임 변경 실패');
                        return res.text();
                    })
                    .then(msg => {
                        alert('닉네임이 변경되었습니다.');
                        document.getElementById('user-nickname').textContent = newNickname;
                    })
                    .catch(err => {
                        console.error(err);
                        alert('닉네임 변경 중 오류가 발생했습니다.');
                    });
            });
        })
        .catch(err => {
            console.error('유저 정보 요청 실패:', err);
            alert('로그인 세션이 만료되었습니다.');
            window.location.href = '/login';
        });

    document.getElementById('logout-btn').addEventListener('click', function(e) {
        e.preventDefault();
        localStorage.clear();
        alert('로그아웃 되었습니다.');
        window.location.href = '/';
    });
});
