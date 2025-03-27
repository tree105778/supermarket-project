# 슈퍼마켓 매니저

## 1. 개요

- **프로젝트명:** 슈퍼마켓 매니저
- **프로젝트 기간:** 2025/03/24 ~ 2025/03/27
- **팀장:** 이은혁
- **팀원:** 배주연, 주영찬, 김수영

## 2. 목적 및 배경

### 주제 선정 배경

최근 디지털 전환이 가속화됨에 따라 매장을 효율적으로 운영하는 것이 매우 중요해졌습니다. 특히, 작은 매장에서는 여전히 재고 관리 등을 수작업으로 수행하여 비효율이 발생하고 있습니다.

이에, 고객이 프로그램을 통해 직접 회원가입, 구매, 환불, 제품 조회를 할 수 있도록 하고, 점주는 프로그램을 통해 재고 관리와 회원 관리, 매출 확인을 할 수 있도록 하여 가게 운영의 효율성을 높이는 것을 목표로 주제를 선정하였습니다.

### 프로젝트 목표

매장에서 발생하는 제품 구매, 환불, 조회, 등록, 매출 확인 등의 작업을 프로그램을 통해 수행하여 매장 운영에 도움을 주는 프로그램을 만드는 것이 목표입니다.

### 학습 목표

- **Java:**
    - `domain`, `repository`, `service` 패키지를 구분하여 개발 수행
    - DB 접근은 repository에서만 수행하고, 구매, 조회 등 기능은 service를 통해 접근하여 데이터 보호 및 코드 은닉화 구현
- **SQL:**
    - ERD 테이블 설계부터 시작하여 DDL, DCL, TCL 명령어 사용
    - 테이블 설계와 쿼리문 작성에 익숙해짐
- **Jdbc:**
    - 구매, 환불 등 여러 테이블을 수정하는 작업을 트랜잭션으로 묶어 DB 보호 및 접속/수정 횟수 최소화

## 3. 개발 환경

- **프로그래밍 언어:** Java
- **DB:** 오라클
- **형상 관리 툴:** Github

## 4. 주 개발 범위

- **Java:**
    - 고객이나 점주의 서비스 요청에 따라 DB의 데이터를 조회 또는 수정
    - 조회한 정보는 요청에 맞게 터미널에 출력
- **DB:**
    - 고객, 제품, 주문 내역, 주문 목록, 카테고리를 테이블로 저장하여 Java에서 요청한 작업 수행

## 5. 주요 기능

### 고객 입장에서의 주요 기능

- 제품 구매
- 제품 환불
- 회원가입
- 회원탈퇴
- 물품 조회

### 점주 입장에서의 주요 기능

- 제품 등록
- 제품 삭제
- 회원 조회
- 제품 조회
- 카테고리 등록
- 거래 내역 조회
- 총 매출 조회

## 6. 요구사항 정의

[요구사항 정의 스프레드시트 링크](https://docs.google.com/spreadsheets/d/1uygJGV_SW0c8-6Sy_mWWPZzLg4lsyQ_AEPvNigL00vM/edit?gid=0#gid=0)

요구사항 명세서 및 데이터베이스 설계도와 설명은 위 링크에서 확인할 수 있습니다.
<br>
<br>
[프로젝트 계층구조](https://docs.google.com/presentation/d/1dyoHFJ-xoTuQ9VRrLUS5ivXBKY7gGyb7lBkpeh7nwCs/edit#slide=id.g343e0f6016a_0_1)


## 7. WBS (Work Breakdown Structure)

[WBS 스프레드시트 링크](https://docs.google.com/spreadsheets/d/1fmmWwUjyyIgcdNfXwb8PxTCprQLmnWHVWJf0GGN8lCo/edit?gid=0#gid=0)

WBS는 위 링크를 참조하시면 됩니다.

## 8. 트러블 슈팅 및 한계점

### 트러블 슈팅

- **환불 문제:**
    - 제품 환불 시 재고(stock)가 증가하지 않고 주문 내역의 구매 갯수(count)가 줄어들지 않아, 주문 내역의 총 금액(total_price)과 고객의 총 결제금액(total_pay)이 음수가 되는 문제가 발생함
    - 해결: 환불 진행 시 `Buy_list` 테이블의 `count`가 줄어들도록 수정함
- **트랜잭션 롤백 문제:**
    - 구매 및 환불 절차에서 4개의 테이블에 데이터 삽입, 수정이 동시에 진행되면서 하나의 트랜잭션 메소드로 구성했으나, 계속 롤백되는 현상 발생
    - 해결: 4개의 서브 메소드로 나눠 순차적으로 호출하는 트랜잭션으로 수정하여 문제 해결
- **시퀀스 CURRVAL 문제:**
    - 구매 진행 시 `buy_history`에 시퀀스를 이용하여 `buy_id(PK)`를 입력, 이후 `buy_history_seq.CURRVAL`로 `buy_list`에 `buy_id(FK)` 입력 시 SQL 구문 오류 발생
    - 원인: `Resultset`의 `getInt` 시 올바른 컬럼명인 `CURRVAL`을 사용하지 않음
    - 해결: `CURRVAL`을 올바른 컬럼명으로 인식하여 수정

### 한계점

- 포인트로 구매하는 등의 로직을 추가하지 못함
- 발주 관련 기능과 DB 구현 미완료
- 제품 환불 시 7일 이내의 구매 내역만 환불 가능 제약 추가 미흡

## 9. 회고

- 협업 시 브랜치 이용 방식에 대해 더 적합한 방법을 알게 됨

<br><br><br><br>
<br><br><br><br>
<br><br><br><br>

화면 명세서 

https://docs.google.com/document/d/1cVwC8ddqjBRXJL6mDwOaXxTKXfQ3ohlDYxNyQiJ_wEE/edit?tab=t.0

MSA

https://docs.google.com/document/d/1L5Hli3JZI4d1EzKvm9PSHh3Gl6xTAcdoNMjTHXr2B00/edit?tab=t.0

스토리보드

https://docs.google.com/document/d/1RHuH8_8yow3Y_Qr6LDOK4giunFfrJMhIYmtRphHgguw/edit?tab=t.0
