# Ollsy

남성전용 쇼핑몰 사이트 ‘Ollsy’를 개발하는 '개인 프로젝트'

프론트엔드 부분은 별도의 프레임워크 없이 HTML, CSS, JS로 구현, 백엔드에 초점을 맞춰 백엔드 개발에 주력

단순 기능 구현이 아닌 성능, 동시성, 코드의 재사용성 유지보수를 고려하여 실제 서비스시 생길 문제에 초점을 두고 개발

## 📚서비스 UI
<img width="700" alt="Image" src="https://github.com/user-attachments/assets/05111b2e-4868-4836-a188-3675177aaa24" />
<img width="700" alt="Image" src="https://github.com/user-attachments/assets/d39eb232-2bc7-4798-93ce-d6a7941d8e7e" />
<img width="700" alt="Image" src="https://github.com/user-attachments/assets/1c83851f-a18d-4f34-b328-a44abc0409e0" />
<img width="700" alt="Image" src="https://github.com/user-attachments/assets/fd8ee5db-6186-480f-96a4-ff05917c6190" />
<img width="700" alt="Image" src="https://github.com/user-attachments/assets/dcf55b37-5164-460b-b1b5-6a9622c124bc" />

## 📘주요 기능
사용자
1.	로그인/ 로그아웃
2.	회원정보 조회/ 수정(닉네임)
3.	상품조회
4.	상품 정렬(신상품 순, 카테고리 별)
5.	장바구니 추가/ 삭제
6.	주문
7.	주문 조회/ 취소

관리자
1.	이미지 업로드/ 삭제
2.	상품 업로드/ 수정/ 삭제
3.	카테고리 생성/ 수정/ 삭제

## 📕 Architecture
<img width="800" alt="Image" src="https://github.com/user-attachments/assets/d67e53fd-59af-49f2-9164-5a5d5a8d31f4" />

## ⌗ERD
<img width="800" alt="Image" src="https://github.com/user-attachments/assets/0668f4f8-a267-4674-96dd-40402ef441ea" />

## 📗사용 기술 및 개발 환경
FrontEnd

<img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=HTML5&logoColor=white">
<img src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=CSS3&logoColor=white">
<img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=JavaScript&logoColor=white">

BackEnd

<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=OpenJDK&logoColor=white">
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">

DB

<img src="https://img.shields.io/badge/h2-09476B?style=for-the-badge&logo=h2&logoColor=white">
<img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white">


AWS, ETC

<img src="https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white"> 
<img src="https://img.shields.io/badge/Amazon%20EC2-FF9900?style=for-the-badge&logo=Amazon%20EC2&logoColor=white">
<img src="https://img.shields.io/badge/Amazon%20S3-569A31?style=for-the-badge&logo=Amazon%20S3&logoColor=white">
<img src="https://img.shields.io/badge/k6-7D64FF?style=for-the-badge&logo=k6&logoColor=white">

## 💪 트러블 슈팅 및 성능 개선
https://www.notion.so/devblog-sj/Ollsy-2162c0261ba780de8daee2cbeb83c0f4 (트러블 슈팅 및 성능 개선 모음) 

<details>
<summary>데이터베이스 락을 통한 동시성 제어 </summary>
<div markdown="1">

https://devblog-sj.notion.site/Ollsy-2162c0261ba780429befd19da42c21e9?pvs=74 블로그 작성

</div>
</details>

<details>
<summary>인덱스를 활용한 병목지점 DB 해결 및 성능 개선</summary>
<div markdown="1">

- 
    
    [https://devblog-sj.notion.site/MySQL-SQL-2162c0261ba7800a8a87f714e1df9bfe?pvs=74](https://www.notion.so/MySQL-SQL-2162c0261ba7800a8a87f714e1df9bfe?pvs=21) MySQL 성능 개선을 학습하며 작성한 블로그
    
    ### 병목지점 인덱스를 활용한 성능 개선
    
  메인 페이지의 신상품  순으로 데이터를 가져오는 API에서 인덱스를 사용해 성능 개선 →CREATE INDEX idx_created_at ON items (create_at) 인덱스 생성
    
<img width="500" alt="Image" src="https://github.com/user-attachments/assets/79b3b1cb-3bd7-437d-8b41-bdf442ef7e97" />

  인덱스 생성 후 부하테스트 결과
    
<img width="500" alt="Image" src="https://github.com/user-attachments/assets/77b71f7f-8299-4cae-95af-ab1b007ae46f" />

    
  기존 Throughput 1.7TPS Latency가 10초 → Throughput 6.6TPS Latency가 3초(인덱스 활용)

    
<img width="500" alt="Image" src="https://github.com/user-attachments/assets/103e3c8a-5005-4769-a697-51561f4f5873" />
    
  임의로 개선하는게 아니라 병목지점이라고 판단이 서면 개선해야 한다.
    
  인덱스를 사용한 이유:

  인덱스를 사용하면 읽기의 성능이 좋아지지만 많은 인덱스를 가진다면 해당 테이블의 쓰기/ 수정의 성능 저하가 발생하기 때문에 쇼핑몰 특성상 쓰기/ 수정보다 읽기가 많다. 또한 트래픽이 가장 많이 몰릴 메인페이지의 API에 사용되는 테이블에 인덱스를 활용하여 성능을 개선했다.

</div>
</details>

<details>
<summary>N+1 문제 테스트 및 개선 </summary>
<div markdown="1">

https://devblog-sj.notion.site/Ollsy-N-1-2162c0261ba7809691b4d04bbe150820?pvs=74 블로그 작성

</div>
</details>
