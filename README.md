# 스프링 DB - 1편

## :round_pushpin: H2 데이터베이스 설정

> 해당 프로젝트에서 어떤 버전의 H2 데이터베이스를 사용하는지 확인한다.

<img width="296" alt="스크린샷 2022-04-16 오후 5 37 25" src="https://user-images.githubusercontent.com/23515771/163668391-4c8c194e-755c-4789-93e7-d5a8aaed2269.png">

해당 프로젝트에서 사용하는 H2 데이터베이스가 `1.4.200` 버전임을 확인하고, https://www.h2database.com/html/main.html 에
접속해서 `All Downloads -> Archive Downloads` 로 들어간다.

<img width="376" alt="스크린샷 2022-04-16 오후 5 39 17" src="https://user-images.githubusercontent.com/23515771/163668447-50cd2181-598d-48f4-b9d4-e10e490f41c2.png">

`1.4.200` 버전의 `Platform-Independent Zip` 을 다운로드 하고, 압축을 해제한다.

> H2 데이터베이스 실행하기

`h2/bin` 으로 디렉토리를 이동하여, `chmod 755 h2.sh` 명령을 통해 실행 권한을 허용한다.

<img width="486" alt="스크린샷 2022-04-16 오후 5 42 01" src="https://user-images.githubusercontent.com/23515771/163668509-f75c002c-836b-4be6-a2ec-807d0ca11aea.png">

그리고 나서 `./h2.sh` 를 실행한다.

<img width="355" alt="스크린샷 2022-04-16 오후 5 42 36" src="https://user-images.githubusercontent.com/23515771/163668530-784adca8-6f8b-4cf9-8268-fe0423085f22.png">

> H2 데이터베이스 접속

Host를 반드시 `localhost:8082` 로 변경해서 접속해야 한다.

> H2 데이터베이스 연결하기

최초 한번은 파일로 접근하는 방식으로 연결을 해야한다.

<img width="464" alt="스크린샷 2022-04-16 오후 5 44 51" src="https://user-images.githubusercontent.com/23515771/163668590-fc822574-b484-4c3e-bc23-8b5cd57d6122.png">

그리고 나서 `~` 경로에 `test.mv.db` 가 생성되었는지를 꼭 확인해야 한다.

<img width="482" alt="스크린샷 2022-04-16 오후 5 46 59" src="https://user-images.githubusercontent.com/23515771/163668624-e76d62de-635e-4de0-a8e7-818064346016.png">

그리고 이후 부터는 `jdbc:h2:tcp://localhost/~/test` 로 접속하면 된다.

<img width="474" alt="스크린샷 2022-04-16 오후 5 49 48" src="https://user-images.githubusercontent.com/23515771/163668682-7866b74b-e115-4e5f-b275-3011d1859008.png">

## :round_pushpin: JDBC 표준 인터페이스

JDBC(Java Database Connectivity)는 자바에서 데이터베이스에 접속할 수 있도록 하는 자바 API다. 대표적으로 다음 3가지 기능을 표준 인터페이스로 정의해서
제공한다.

- `java.sql.Connection` - 연결
- `java.sql.Statement` - SQL을 담은 내용
- `java.sql.ResultSet` - SQL 요청 응답

이 JDBC 인터페이스를 각각의 DB 벤더에서 자신의 DB에 맞도록 구현해서 라이브러리를 제공하는데, 이것을 **`JDBC 드라이버`** 라고 한다.

<img width="1704" alt="image" src="https://user-images.githubusercontent.com/23515771/163669304-058ccd6a-d793-4b49-baec-46dfcc6e7f40.png">

`애플리케이션 로직` 에서는 `JDBC 표준 인터페이스` 를 그대로 사용하면 되고, `드라이버` 구현체만 바꾸면 된다. (개인적인 생각으로 **`전략 패턴`** 을 기반으로 구현한
것 같다.)

## :round_pushpin: JDBC를 편리하게 사용하는 다양한 기술들

### SQL Mapper

> 장점

- SQL 응답 결과를 객체로 편리하게 변환해준다.
- JDBC의 반복 코드를 제거해준다.

> 단점

- 개발자가 직접 SQL을 작성해야 한다.

> 대표 기술

- Spring JdbcTemplate, MyBatis

### ORM

객체를 관계형 데이터베이스 테이블과 매핑해주는 기술이다. 이 기술 덕분에 개발자는 SQL을 직접 작성하지 않아도 된다.

> 대표 기술

- JPA, Hibernate, Eclipse Link

## :round_pushpin: JDBC DriverManager 연결 이해

### DriverManager 커넥션 요청 흐름

<img width="832" alt="스크린샷 2022-04-16 오후 7 50 18" src="https://user-images.githubusercontent.com/23515771/163672129-c9a5933a-3e2a-4679-8346-a957af8e9662.png">

드라이버는 `jdbc:h2:tcp://localhost/~/test` 의 URL을 통해 내가 처리할 수 있는 연결인지 `H2, MySQL, Oracle` 를 차례대로 확인한다.

- `jdbc:h2` 는 H2 데이터베이스에 접근하기 위한 규칙

## :round_pushpin: 커넥션 풀 이해

<img width="847" alt="스크린샷 2022-04-16 오후 11 10 56" src="https://user-images.githubusercontent.com/23515771/163678190-adb165cd-a731-43dd-9548-bde8b442c5e5.png">

1. 애플리케이션 로직은 DB 드라이버를 통해 커넥션을 조회한다.
2. DB 드라이버는 DB와 `TCP/IP` 커넥션을 연결한다. 물론 이 과정에서 `3 way handshake` 같은 `TCP/IP` 연결을 위한 네트워크 동작이 발생한다.
3. DB 드라이버는 TCP/IP 커넥션이 연결되면 ID, PW와 기타 부가정보를 DB에 전달한다.
4. DB는 ID, PW를 통해 내부 인증을 완료하고, 내부에 DB 세션을 생성한다.
5. DB는 커넥션 생성이 완료되었다는 응답을 보낸다.
6. DB 드라이버는 커넥션 객체를 생성해서 클라이언트에 반환한다.

**`이렇게 커넥션을 새로 만드는 것은 과정도 복잡하고, 시간도 많이 소모되는 일이다.`** 진짜 문제는 고객이 애플리케이션을 사용할 때, SQL을 실행하는 시간 뿐만
아니라 **`커넥션을 새로 만드는 시간이 추가`** 되기 때문에 **`응답 속도에 영향을 준다.`** 이러한 문제를 한 번에 해결하는 아이디어가 커넥션을 미리 생성해두고
사용하는 **`커넥션 풀`** 이라는
방법이다.

### 커넥션 풀 초기화

<img width="845" alt="스크린샷 2022-04-16 오후 11 18 07" src="https://user-images.githubusercontent.com/23515771/163678477-c1917d4d-5fa5-46b5-a84f-1d6b3f0cc567.png">

애플리케이션을 시작하는 시점에 커넥션 풀은 필요한 만큼 커넥션을 생성하여, 풀에 보관한다.

### 커넥션 풀의 연결 상태

<img width="841" alt="스크린샷 2022-04-16 오후 11 19 34" src="https://user-images.githubusercontent.com/23515771/163678520-1708784f-0706-4f1e-985d-d5873d20e44a.png">

커넥션 풀의 커넥션은 TCP/IP로 DB와 연결되어 있는 상태이기 때문에 언제든지 SQL을 전달할 수 있다.

### 커넥션 풀 사용

<img width="837" alt="스크린샷 2022-04-16 오후 11 21 14" src="https://user-images.githubusercontent.com/23515771/163678592-725d91bb-3e27-437a-b55a-656e6b7a637e.png">

애플리케이션 로직에서는 커넥션을 생성하지 않고, 이미 생성되어 있는 커넥션을 풀에서 가져다 사용하면 된다.

<img width="846" alt="스크린샷 2022-04-16 오후 11 22 58" src="https://user-images.githubusercontent.com/23515771/163678660-e51c3afd-148b-4416-8b68-8cd24c02e223.png">

커넥션을 다 사용하고 나면, 커넥션을 종료하는 것이 아니라 커넥션이 살아있는 상태로 커넥션 풀에 반환해야 한다.

### 정리

- 적절한 커넥션 풀 숫자는 서비스의 특징과 애플리케이션의 서버 스펙, DB 서버 스펙에 따라 다르기 때문에 성능 테스트를 통해서 정해야한다.
- 커넥션 풀은 서버당 최대 커넥션 수를 제한할 수 있다. 따라서 DB에 무한정 연결이 생성되는 것을 막아주어서 DB를 보호하는 효과도 있다.
- 이런 커넥션 풀은 얻는 이점이 매우 크기 때문에 **`실무에서는 항상 기본으로 사용`** 한다.
- 커넥션 풀은 개념적으로 단순해서 직접 구현할 수도 있지만, 사용도 편리하고 성능도 뛰어난 오픈소스 커넥션 풀이 많기 때문에 오픈소스를 사용하는 것이 좋다.
- 대표적인 커넥션 풀 오픈소스는 `commons-dbcp2`, `tomcat-jdbc pool`, `HikariCP` 등이 있다.
- 성능과 사용의 편리함 측면에서 최근에는 `HikariCP` 를 주로 사용한다. 스프링 부트 2.0 부터는 기본 커넥션 풀로 `HikariCP` 를 제공한다. 성능, 사용의
  편리함, 안전성 측면에서 이미 검증이
  되었기 때문에 커넥션 풀을 사용할 때는 고민할 것 없이 `HikariCP` 를 사용하면 된다. 실무에서도 레거시 프로젝트가 아닌 이상 대부분 `HikariCP` 를 사용한다.

## :round_pushpin: DataSource 이해

### DriverManager를 통해 커넥션 획득하다가 커넥션 풀로 변경시 문제

![스크린샷 2022-04-16 오후 11 33 21](https://user-images.githubusercontent.com/23515771/163679073-5a373344-abaf-40ac-a138-93ae88b6d323.png)

DriverManager를 사용하다가 HikariCP를 사용하게 되면, 애플리케이션 코드가 변경되는 상황이 발생하게 된다. 왜냐하면 의존관계가 `DriverManager`
에서 `HikariCP` 로 변경되기
때문이다.

### 커넥션 획득하는 방법을 추상화

<img width="839" alt="스크린샷 2022-04-16 오후 11 35 45" src="https://user-images.githubusercontent.com/23515771/163679123-8f8bf71d-a6d5-4d45-9ee3-8f8204f9532a.png">

자바에서는 이러한 문제를 해결하기 위해서 `java.sql.DataSource` 라는 인터페이스를 제공한다. `DataSource`
는 **`커넥션을 획득하는 방법을 추상화한 인터페이스`** 이다.

```java
public interface DataSource {

    Connection getConnection() throws SQLException;
}
```

### 설정과 사용의 분리

- **`설정`**: DataSource 를 만들고 필요한 속성들을 사용해서 `URL`, `USERNAME`, `PASSWORD` 같은 부분을 입력하는 것을 말한다. 이렇게 설정과
  관련된 속성들은 한 곳에 있는 것이
  향후 변경에 더 유연하게 대처할 수 있다.
- **`사용`**: 설정은 신경쓰지 않고, `DataSource` 의 `getConnection()` 만 호출해서 사용하면 된다.

### 설정과 사용의 분리 설명

- 이 부분이 작아보이지만 큰 차이를 만들어내는데, 필요한 데이터를 `DataSource` 가 만들어지는 시점에 미리 다 넣어두게 되면, `DataSource` 를 사용하는
  곳에서는
  `dataSource.getConnection()` 만 호출하면 되므로, `URL`, `USERNAME`, `PASSWORD` 같은 속성들에 의존하지 않아도 된다.
  그냥 `DataSource` 만 주입받아서
  `getConnection()` 만 호출하면 된다.
- 쉽게 이야기해서 리포지토리(Repository)는 `DataSource` 만 의존하고, 이런 속성을 몰라도 된다.
- 애플리케이션을 개발해보면 보통 설정은 한 곳에서 하지만, 사용은 수 많은 곳에서 하게 된다.
- 덕분에 객체를 설정하는 부분과, 사용하는 부분을 좀 더 명확하게 분리할 수 있다.

### ${pool name} connection adder 스레드

커넥션 풀에 커넥션을 채우는 작업은 상대적으로 오래 걸리는 작업이다. 애플리케이션을 실행할 때, 커넥션 풀을 채울때 까지 마냥 대기하고 있다면 실행시간이 늦어진다. 따라서 별도의
스레드를 사용해서 커넥션 풀을
생성하게 되며, 애플리케이션 실행 시간에 영향을 주지 않는다.

## :round_pushpin: 트랜잭션에서 자동 커밋, 수동 커밋

### 자동 커밋

```sql
set autocommit true; -- 자동 커밋 모드 설정
insert into member(member_id, money)
values ('data1', 10000); -- 개별적으로 쿼리가 실행되고, 커밋 됨 (트랜잭션 1)
insert into member(member_id, money)
values ('data2', 10000); -- 개별적으로 쿼리가 실행되고, 커밋 됨 (트랜잭션 2)
```

자동 커밋으로 설정하면, `각각의 쿼리 실행 직후에 자동으로 커밋을 호출한다.` 커밋이나 롤백을 호출하지 않아도 되는 장점이 있지만, 자동으로 커밋이 되기 때문에 트랜잭션 기능을
제대로 사용할 수 없다.

### 수동 커밋

```sql
set autocommit false; -- 수동 커밋 모드 설정
insert into member(member_id, money)
values ('data1', 10000); -- 트랜잭션 1에서 처리됨
insert into member(member_id, money)
values ('data2', 10000); -- 트랜잭션 1에서 처리됨
commit; -- 수동 커밋 설정인 상태에서 작업이 완료되면, 꼭 commit, rollback 처리 할 것
```

보통 자동 커밋 모드가 기본으로 설정된 경우가 많기 때문에 **`수동 커밋 모드로 설정하는 것을 트랜잭션 시작`** 한다고 표현할 수 있다. 수동 커밋 모드를 설정하면, 작업
이후에 `commit`
또는 `rollback` 을 꼭 호출해야 한다.

## :round_pushpin: DB 락 - 개념 이해

세션1이 트랜잭션을 시작하고 데이터를 수정하는 동안 아직 커밋을 수행하지 않았는데, 세션2에서 동시에 같은 데이터를 수정하게 되면 여러가지 문제가 발생한다. 바로 트랜잭션의
원자성이 깨지는 것이다. 여기에 더해서
세션1이 중간에 롤백을 하게 되면 세션2는 잘못된 데이터를 수정하는 문제가 발생한다.

이런 문제를 방지하려면, 세션이 트랜잭션을 시작하고 데이터를 수정하는 동안에는 커밋이나 롤백 전까지 다른 세션에서 해당 데이터를 수정할 수 없게 막아야 한다.

<img width="840" alt="스크린샷 2022-04-17 오후 1 20 10" src="https://user-images.githubusercontent.com/23515771/163700257-20d92b77-b324-4d47-bdfc-6c3dd3b6201a.png">

- 세션1은 `memberA` 의 금액을 500원으로 변경하고 싶고, 세션2는 같은 `memberA` 의 금액을 1000원으로 변경하고 싶다.
- 데이터베이스는 이런 문제를 해결하기 위해 락(Lock)이라는 개념을 제공한다.
- 다음 예시를 통해 동시에 데이터를 수정하는 문제를 락으로 어떻게 해결하는지 자세히 알아보자.

<img width="841" alt="스크린샷 2022-04-17 오후 1 21 05" src="https://user-images.githubusercontent.com/23515771/163700291-14f664b6-8387-4a07-ac86-9bd6d29db68b.png">

1. 세션1은 트랜잭션을 시작한다.
2. 세션1은 `memberA` 의 `money` 를 500으로 변경을 시도한다. 이때 해당 로우의 락을 먼저 획득해야 한다. 락이 남아 있으므로 세션1은 락을 획득한다. (
   세션1이 세션2보다 조금 더 빨리
   요청했다.)
3. 세션1은 락을 획득했으므로 해당 로우에 `update sql` 을 수행한다.

<img width="842" alt="스크린샷 2022-04-17 오후 1 21 54" src="https://user-images.githubusercontent.com/23515771/163700312-081acfae-4f2d-43e7-98e5-ca2933b1c1fa.png">

4. 세션2는 트랜잭션을 시작한다.
5. 세션2도 `memberA` 의 `money` 데이터를 변경하려고 시도한다. 이때 해당 로우의 락을 먼저 획득해야 한다. 락이 없으므로 락이 돌아올 때 까지 대기한다.
    - 참고로 세션2가 락을 무한정 대기하는 것은 아니다. 락 대기 시간을 넘어가면 `락 타임아웃 오류` 가 발생한다. 락 대기 시간은 설정할 수 있다.

<img width="841" alt="스크린샷 2022-04-17 오후 1 22 55" src="https://user-images.githubusercontent.com/23515771/163700328-418708c0-1228-45d7-9e2e-4c00f36fcf18.png">

6. 세션1은 커밋을 수행한다. 커밋으로 트랜잭션이 종료되었으므로 락도 반납한다.

<img width="835" alt="스크린샷 2022-04-17 오후 1 23 33" src="https://user-images.githubusercontent.com/23515771/163700345-46c69400-a3ab-4e7e-9acd-ab567a11b3ab.png">

- 락을 획득하기 위해 대기하던 세션2가 락을 확득한다.

<img width="837" alt="스크린샷 2022-04-17 오후 1 23 59" src="https://user-images.githubusercontent.com/23515771/163700363-8b1e7f8f-1b1f-40e6-aec1-f6ec964578da.png">

7. 세션2는 `update sql` 을 수행한다.

<img width="839" alt="스크린샷 2022-04-17 오후 1 24 28" src="https://user-images.githubusercontent.com/23515771/163700376-ef5b8971-d1d5-46fb-ae74-ff40f4c997bf.png">

8. 세션2는 커밋을 수행하고 트랜잭션이 종료되었으므로 락을 반납한다.

## :round_pushpin: 트랜잭션 사용 후, 커넥션 종료시 주의할 점

### 커넥션 풀을 사용하지 않는 경우

```java
public class Service {

    public void logic() throws SQLException {
        Connection conn = dataSource.getConnection();

        try {
            conn.setAutoCommit(false); // 트랜잭션 시작
            bizLogic(); // 비즈니스 로직 수행
            conn.commit(); // 커밋
        } catch (Exception e) {
            conn.rollback(); // 롤백
            throw new IllegalStateException(e);
        } finally {
            release(conn); // 커넥션 풀을 사용하는 경우, 커넥션 반납
        }
    }

    private void release(Connection conn) {
        if (conn != null) {
            try {
                conn.setAutoCommit(true); // 커넥션 풀을 고려해서 자동 커밋으로 다시 되돌린다.
                conn.close();
            } catch (Exception e) {
                log.error("error", e);
            }
        }
    }
}
```

커넥션 풀을 사용하지 않는 경우, 그냥 `conn.close()` 를 하면 그냥 `커넥션이 종료` 된다. 그런데 커넥션 풀을 사용하면서 트랜잭션을 사용하는
경우, `setAutoCommit(false)` 상태로
변경하고(`트랜잭션 시작을 의미한다.`), 비즈니스 로직을 수행한 다음 commit, rollback 으로 작업을 마무리 한다. 이 때, 연결을 종료하지
않고 **`커넥션을 반납하게 된다.`**
그래서 `setAutoCommit` 의 기본 값인 `true` 로 변경해줘야 다음 작업에서도 문제가 없게 되며, `true` 로 변경하는 것이 안전하다.

## :round_pushpin: Spring의 트랜잭션 추상화

<img width="822" alt="스크린샷 2022-04-18 오후 9 56 15" src="https://user-images.githubusercontent.com/23515771/163811498-82397d78-5c62-4844-88c5-4feede91d0a9.png">

우리는 스프링이 제공하는 트랜잭션 추상화 기술을 사용하면 된다. 심지어 데이터 접근 기술에 따른 트랜잭션 구현체도 대부분 만들어두어서 가져다 사용하기만 하면 된다. 스프링 트랜잭션
추상화의
핵심은 **`PlatformTransactionManager`** 인터페이스이다.

- **`org.springframework.transaction.PlatformTransactionManager`**

```java
package org.springframework.transaction;

public interface PlatformTransactionManager extends TransactionManager {

    TransactionStatus getTransaction(@Nullable TransactionDefinition definition)
        throws TransactionException;

    void commit(TransactionStatus status) throws TransactionException;

    void rollback(TransactionStatus status) throws TransactionException;
}
```

## :round_pushpin: 트랜잭션 매니저와 트랜잭션 동기화 매니저

<img width="822" alt="스크린샷 2022-04-18 오후 10 10 37" src="https://user-images.githubusercontent.com/23515771/163813036-4a516c7f-f46c-4968-b37a-cd1208a2e24c.png">

- 스프링은 트랜잭션 동기화 매니저를 제공한다. 이것은 쓰레드 로컬(**`ThreadLocal`**)을 사용해서 커넥션을 동기화해준다. 트랜잭션 매니저는 내부에서 이 트랜잭션
  동기화 매니저를 사용한다.
- 트랜잭션 동기화 매니저는 쓰레드 로컬을 사용하기 때문에 멀티쓰레드 상황에 안전하게 커넥션을 동기화 할 수 있다. 따라서 커넥션이 필요하면 트랜잭션 동기화 매니저를 통해 커넥션을
  획득하면 된다. 따라서 이전처럼
  파라미터로 커넥션을 전달하지 않아도 된다.

### 동작 방식

1. 트랜잭션을 시작하려면 커넥션이 필요하다. `트랜잭션 매니저` 는 `데이터소스` 를 통해 `커넥션을 만들고 트랜잭션을 시작한다.`
2. 트랜잭션 매니저는 `트랜잭션이 시작된 커넥션` 을 `트랜잭션 동기화 매니저에 보관` 한다.
3. 리포지토리는 트랜잭션 동기화 매니저에 보관된 커넥션을 꺼내서 사용한다. 따라서 파라미터로 커넥션을 전달하지 않아도 된다.
4. 트랜잭션이 종료되면 트랜잭션 매니저는 트랜잭션 동기화 매니저에 보관된 커넥션을 통해 트랜잭션을 종료하고, 커넥션도 닫는다.

### 트랜잭션 동기화 매니저

**`TransactionSynchronizationManager`** 추상 클래스

- **`org.springframework.transaction.support.TransactionSynchronizationManager`**

### DataSourceUtils.getConnection()

- `DataSourceUtils.getConnection()` 는 다음과 같이 동작한다.
    - 트랜잭션 동기화 매니저가 `관리하는 커넥션이 있으면`, `해당 커넥션`을 반환한다.
    - 트랜잭션 동기화 매니저가 `관리하는 커넥션이 없는 경우`, `새로운 커넥션을 생성` 해서 반환한다.

### DataSourceUtils.releaseConnection()

- `close()` 에서 `DataSourceUtils.releaseConnection()` 를 사용하도록 변경된 부분을 특히 주의해야 한다. 커넥션을 `conn.close()`
  를
  사용해서 `직접 닫아버리면 커넥션이 유지되지 않는 문제` 가 발생한다. `이 커넥션은 이후 로직은 물론이고, 트랜잭션을 종료(커밋, 롤백)할 때 까지 살아있어야 한다.`
- `DataSourceUtils.releaseConnection()` 을 사용하면 커넥션을 바로 닫는 것이 아니다.
    - **`트랜잭션을 사용하기 위해 동기화된 커넥션은 커넥션을 닫지 않고, 그대로 유지해준다.`**
    - 트랜잭션 동기화 매니저가 관리하는 커넥션이 없는 경우, 해당 커넥션을 닫는다.

### 트랜잭션 매니저의 동작 흐름

<img width="820" alt="스크린샷 2022-04-18 오후 10 51 00" src="https://user-images.githubusercontent.com/23515771/163817907-d2415e7d-ff24-440c-9f10-3f4086ed792b.png">

1. 서비스 계층에서 `transactionManager.getTransaction()` 을 호출해서 트랜잭션을 시작한다.
2. 트랜잭션을 시작하려면 먼저 데이터베이스 커넥션이 필요하다. 트랜잭션 매니저는 내부에서 데이터소스를 사용해서 커넥션을 생성한다.
3. 커넥션을 수동 커밋 모드로 변경해서 (`setAutoCommit(false)`) 실제 데이터베이스 트랜잭션을 시작한다.
4. 커넥션을 트랜잭션 동기화 매니저에 보관한다.
5. 트랜잭션 동기화 매니저는 쓰레드 로컬에 커넥션을 보관한다. 따라서 멀티 쓰레드 환경에 안전하게 커넥션을 보관할 수 있다.

<img width="821" alt="스크린샷 2022-04-18 오후 10 54 42" src="https://user-images.githubusercontent.com/23515771/163818403-2dcf3b60-3ab2-4780-b4e3-412f838a663e.png">

6. 서비스는 비즈니스 로직을 실행하면서 리포지토리의 메서드들을 호출한다. 이때 커넥션을 파라미터로 전달하지 않는다.
7. 리포지토리 메서드들은 트랜잭션이 시작된 커넥션이 필요하다. 리포지토리는 `DataSourceUtils.getConnection()` 을 사용해서 트랜잭션 동기화 매니저에
   보관된 커넥션을 꺼내서 사용한다. 이
   과정을 통해서 자연스럽게 같은 커넥션을 사용하고, 트랜잭션도 유지된다.
8. 획득한 커넥션을 사용해서 SQL을 데이터베이스에 전달해서 실행한다.

<img width="823" alt="스크린샷 2022-04-18 오후 10 55 24" src="https://user-images.githubusercontent.com/23515771/163818487-1c26b4ed-8849-4bbd-b716-696fa2072e56.png">

9. 비즈니스 로직이 끝나고 트랜잭션을 종료한다. 트랜잭션은 커밋하거나 롤백하면 종료된다.
10. 트랜잭션을 종료하려면 동기화된 커넥션이 필요하다. 트랜잭션 동기화 매니저를 통해 동기화된 커넥션을 획득한다.
11. 획득한 커넥션을 통해 데이터베이스에 트랜잭션을 커밋하거나 롤백한다.
12. 전체 리소스를 정리한다.
    - 트랜잭션 동기화 매니저를 정리한다. `ThreadLocal은 사용후 꼭 정리해야 한다.`
    - `con.setAutoCommit(true)` 로 되돌린다. 커넥션 풀을 고려해야 한다.
    - `con.close()` 를 호출해셔 커넥션을 종료한다. 커넥션 풀을 사용하는 경우 `con.close()` 를 호출하면 커넥션 풀에 반환된다.

### 정리

- 트랜잭션 추상화 덕분에 서비스 코드는 이제 JDBC 기술에 의존하지 않는다.
    - 이후 JDBC에서 JPA로 변경해도 서비스 코드를 그대로 유지할 수 있다.
    - 기술 변경시 의존관계 주입만 `DataSourceTransactionManager` 에서 `JpaTransactionManager` 로 변경해주면 된다.
    - `java.sql.SQLException` 이 아직 남아있지만 이 부분은 뒤에 예외 문제에서 해결하자.
- 트랜잭션 동기화 매니저 덕분에 커넥션을 파라미터로 넘기지 않아도 된다.

## :round_pushpin: 선언전 트랜잭션 관리 vs 프로그래밍 방식 트랜잭션 관리

### 선언적 트랜잭션 관리(Declarative Transaction Management)

- `@Transactional` 애노테이션 하나만 선언해서 매우 편리하게 트랜잭션을 적용하는 것을 선언적 트랜잭션 관리라 한다.
- 선언적 트랜잭션 관리는 과거 XML에 설정하기도 했다. 이름 그대로 해당 로직에 트랜잭션을 적용하겠다 라고 어딘가에 선언하기만 하면 트랜잭션이 적용되는 방식이다.

### 프로그래밍 방식의 트랜잭션 관리(Programmatic transaction management)

- 트랜잭션 매니저 또는 트랜잭션 템플릿 등을 사용해서 트랜잭션 관련 코드를 직접 작성하는 것을 프로그래밍 방식의 트랜잭션 관리라 한다.

## :round_pushpin: Spring Boot의 자동 리소스 등록

### DataSource - 자동 등록

- Spring Boot는  `DataSource` 를 자동으로 등록한다.
- 자동으로 등록되는 Spring Bean 이름 -> `dataSource`
- 참고로 개발자가 직접 `DataSource` 를 Bean으로 등록하면, Spring Boot는 `DataSource` 를 자동으로 등록하지 않는다.

### TransactionManager - 자동 등록

- Spring Boot는 적절한 트랜잭션 매니저(`PlatformTransactionManager`)를 자동으로 Spring Bean에 등록한다.
- 자동으로 등록되는 Spring Bean 이름 -> `transactionManager`
- 참고로 개발자가 직접 `TransactionManager` 를 Bean으로 등록하면, Spring Boot는 `TransactionManager` 를 자동으로 등록하지 않는다.

어떤 트랜잭션 매니저를 선택할지는 현재 등록된 라이브러리를 보고 판단하는데, JDBC를 기술을 사용하면 `DataSourceTransactionManager` 를 Bean으로
등록하고, JPA를 사용하면
`JpaTransactionManager` 를 Bean으로 등록한다. `둘 다 사용하는 경우` 에는 `JpaTransactionManager` 를 등록한다.
참고로 `JpaTransactionManager`
는 `DataSourceTransactionManager` 가 제공하는 기능도 대부분 지원한다.

## :round_pushpin: 예외 계층

![스크린샷 2022-04-20 오후 7 49 38](https://user-images.githubusercontent.com/23515771/164215173-9e5515ad-558c-4dc1-b24e-8a80c9dd7dcb.png)

- `Object` : 예외도 객체이다. 모든 객체의 최상위 부모는 `Object` 이므로 예외의 최상위 부모도 `Object` 이다.
- `Throwable` : 최상위 예외이다. 하위에 `Exception` 과 `Error` 가 있다.
- `Error` : 메모리 부족이나 심각한 시스템 오류와 같이 애플리케이션에서 복구 불가능한 시스템 예외이다. 애플리케이션 개발자는 이 예외를 잡으려고 해서는 안된다.
    - 상위 예외를 `catch` 로 잡으면 그 하위 예외까지 함께 잡는다. 따라서 애플리케이션 로직에서는 `Throwable` 예외도 잡으면 안되는데, 앞서
      이야기한 `Error 예외도 함께 잡을 수 있기 때문에다.` 애플리케이션 로직은 이런 이유로 `Exception 부터 필요한 예외로 생각하고 잡으면 된다.`
    - 참고로 `Error` 도 언체크 예외이다
- `Exception` : 체크 예외
    - 애플리케이션 로직에서 사용할 수 있는 `실질적인 최상위 예외이다.`
    - `Exception` 과 그 하위 예외는 모두 `컴파일러가 체크하는 체크 예외이다.` 단 `RuntimeException` 은 예외로 한다.
- `RuntimeException` : 언체크 예외, 런타임 예외
    - `컴파일러가 체크 하지 않는 언체크 예외이다.`
    - `RuntimeException` 과 그 자식 예외는 모두 언체크 예외이다.
    - `RuntimeException` 의 이름을 따라서 `RuntimeException` 과 그 `하위 언체크 예외` 를 **`런타임 예외`** 라고 많이 부른다. 여기서도
      앞으로는 런타임 예외로 종종
      부르겠다.

## :round_pushpin: 예외 기본 규칙

예외는 폭탄 돌리기와 같다. `잡아서 처리하거나, 처리할 수 없으면, 밖으로 던져야 한다.`

### 예외 처리

<img width="839" alt="스크린샷 2022-04-20 오후 8 01 01" src="https://user-images.githubusercontent.com/23515771/164216815-e957100e-d71d-40f5-a186-e14d70ada1b0.png">

5번에서 예외를 처리하면, 이후 애플리케이션 로직은 정상 흐름으로 동작한다.

### 예외 던짐

<img width="841" alt="스크린샷 2022-04-20 오후 8 01 52" src="https://user-images.githubusercontent.com/23515771/164216921-f7d4b463-adce-40f1-9958-04a8bdd5916a.png">

예외를 처리하지 못하면 호출한 곳으로 예외를 계속 던지게 된다.

### 예외에 대한 2가지 기본 규칙

1. 예외를 잡아서 처리하거나, 던져야 한다.
2. 예외를 잡거나 던질때, 지정한 예외뿐만 아니라 그 예외 자식들도 함께 처리된다.
    - `Exception` 을 `catch` 로 잡으면, 그 `하위 예외들도 모두 잡을 수 있다.`
    - `Exception` 을 `throws` 로 던지면 그 `하위 예외들도 모두 던질 수 있다.`

### 예외를 처리하지 못하고 계속 던지면 어떻게 될까?

- 자바 `main()` 쓰레드의 경우 예외 로그를 출력하면서 시스템이 종료된다.
- 웹 애플리케이션의 경우 여러 사용자의 요청을 처리하기 때문에 하나의 예외 때문에 시스템이 종료되면 안된다. `WAS가 해당 예외를 받아서 처리하는데`, 주로 사용자에게 개발자가
  지정한 오류 페이지를 보여준다.

## :round_pushpin: 체크 예외

`Exception` 과 그 하위 예외들은 모두 컴파일러가 체크하는 체크 예외이다. 단, `RuntimeException` 은 예외로 한다. (체크 예외가 아니다.) 체크 예외는
잡아서 처리하거나, 밖으로 던지도록
선언해야 한다. 그렇지 않으면, `컴파일 오류가 발생한다.`

### 체크 예외의 장단점

체크 예외는 예외를 잡아서 처리할 수 없을때, 예외를 밖으로 던지는 `throws` 예외를 필수로 선언해야 한다. 그렇지 않으면 컴파일 오류가 발생한다. 이것 때문에 장점과 단점이
동시에 존재한다.

- `장점` : 개발자가 실수로 예외를 누락하지 않도록 컴파일러를 통해 문제를 잡아주는 훌륭한 안전 장치이다.
- `단점` : 하지만 실제로는 개발자가 모든 체크 예외를 반드시 잡거나 던지도록 처리해야 하기 때문에, 너무 번거로운 일이 된다. 크게 신경쓰고 싶지 않은 예외까지 모두 챙겨야
  한다. 추가로 의존관계에 따른 단점도
  있는데 이 부분은 뒤에서 설명하겠다.

## :round_pushpin: 언체크 예외

`RuntimeException` 과 그 하위 예외들은 언체크 예외로 분류 된다. 즉, 컴파일러가 예외를 체크하지 않는다는 뜻이다. 언체크 예외와 체크 예외는 기본적으로 동일한데
차이가
있다면, `예외를 던지는 throws를 선언하지 않고, 생략할 수 있다.` 이 경우 자동으로 예외를 던진다.

### 체크 예외 vs 언체크 예외

- `체크 예외` : 예외를 잡아서 처리하지 않으면, 반드시 `throws` 를 선언해서 예외를 던져야 한다.
- `언체크 예외` : 예외를 잡아서 처리하지 않아도, `throws` 를 생략할 수 있다.

### 언체크 예외의 장단점

언체크 예외는 예외를 잡아서 처리할 수 없을때, 예외를 밖으로 던지는 `throws` 예외를 생략할 수 있다. 이것 때문에 장점과 단점이 동시에 존재한다.

- `장점` : 신경쓰고 싶지 않은 언체크 예외를 무시할 수 있다. 체크 예외의 경우 처리할 수 없는 예외를 밖으로 던지려면 항상 `throws` 예외 를 선언해야 하지만, 언체크
  예외는 이 부분을 생략할 수 있다.
  이후에 설명하겠지만, 신경쓰고 싶지 않은 예외의 의존관계를 참조하지 않아도 되는 장점이 있다.
- `단점` : 언체크 예외는 개발자가 실수로 예외를 누락할 수 있다. 반면에 체크 예외는 컴파일러를 통해 예외 누락을 잡아준다.

## :round_pushpin: 체크 예외 활용

### 기본적으로 다음 2가지를 기억하자

1. 기본적으로 `언체크(런타임)` 예외를 사용하자
2. 체크 예외는 비즈니스 로직상 의도적으로 던지는 예외에서만 사용하자
    - 이 경우 `해당 예외를 잡아서 반드시 처리해야 하는 문제` 일 때만 `체크 예외를 사용해야 한다.` 예를 들어서 다음과 같은 경우가 있다.
    - 체크 예외 예)
        - 계좌 이체 실패 예외
        - 결제시 포인트 부족 예외
        - 로그인 ID, PW 불일치 예외
    - 물론 이 경우에도 `100% 체크 예외로 만들어야 하는 것은 아니다.` 다만 계좌 이체 실패처럼 매우 심각한 문제는 개발자가 실수로 예외를 놓치면 안된다고 판단할 수
      있다. 이 경우 체크 예외로 만들어
      두면 컴파일러를 통해 놓친 예외를 인지할 수 있다.

### 체크 예외의 문제점

<img width="843" alt="스크린샷 2022-04-20 오후 9 29 41" src="https://user-images.githubusercontent.com/23515771/164230579-f5e1b9b6-4f02-4095-afb4-ebe74a840085.png">

- `리포지토리` 는 DB에 접근해서 데이터를 저장하고 관리한다. 여기서는 `SQLException` **`체크 예외`** 를 던진다.
- `NetworkClient` 는 외부 네트워크에 접속해서 어떤 기능을 처리하는 객체이다. 여기서는 `ConnectException` **`체크 예외`** 를 던진다.
- `서비스` 는 `리포지토리` 와 `NetworkClient` 를 둘 다 호출한다.
    - 따라서 두 곳에서 올라오는 체크 예외인 `SQLException` 과 `ConnectException` 을 처리해야 한다.
    - 그런데 서비스는 이 둘을 처리할 방법을 모른다. `ConnectException` 처럼 연결이 실패하거나, `SQLException` 처럼 데이터베이스에서 발생하는
      문제처럼 심각한 문제들은 대부분
      애플리케이션 로직에서 처리할 방법이 없다.
- `서비스` 는 `SQLException` 과 `ConnectException` 를 처리할 수 없으므로 둘 다 밖으로 던진다.
    - 체크 예외이기 때문에 던질 경우 다음과 같이 선언해야 한다.
    - **`method() throws SQLException, ConnectException`**
- `컨트롤러` 도 두 예외를 처리할 방법이 없다.
    - 다음을 선언해서 예외를 밖으로 던진다.
    - **`method() throws SQLException, ConnectException`**
- 웹 애플리케이션이라면 서블릿의 오류 페이지나, 또는 스프링 MVC가 제공하는 `ControllerAdvice` 에서 이런 예외를 공통으로 처리한다.
- 이런 문제들은 보통 사용자에게 어떤 문제가 발생했는지 자세히 설명하기가 어렵다. 그래서 사용자에게는 `"서비스에 문제가 있습니다."` 라는 일반적인 메시지를
  보여준다. (`"데이터베이스에 어떤 오류가 발생했어요"` 라고 알려주어도 `일반 사용자가 이해할 수 없다.` 그리고 `보안에도 문제가 될 수 있다.`)
- API라면 보통 `HTTP 상태코드 500(내부 서버 오류)` 을 사용해서 응답을 내려준다.
- 이렇게 해결이 불가능한 `공통 예외는 별도의 오류 로그를 남기고`, 개발자가 오류를 빨리 인지할 수 있도록 `메일, 알림(문자, 슬랙)등을 통해서 전달 받아야 한다.` 예를
  들어서 `SQLException` 이
  잘못된 SQL을 작성해서 발생했다면, 개발자가 해당 SQL을 수정해서 배포하기 전까지 사용자는 같은 문제를 겪게 된다.

### 체크 예외의 2가지 문제

> **복구 불가능한 예외**

`대부분의 예외는 복구가 불가능하다`. 일부 복구가 가능한 예외도 있지만 아주 적다. `SQLException` 을 예를 들면 데이터베이스에 무언가 문제가 있어서 발생하는
예외이다. SQL 문법에 문제가 있을 수도
있고, 데이터베이스 자체에 뭔가 문제가 발생했을 수도 있다. 데이터베이스 서버가 중간에 다운 되었을 수도 있다. 이런 문제들은 대부분 복구가 불가능하다. 특히나 대부분의 서비스나
컨트롤러는 이런 문제를 해결할 수는
없다. 따라서 `이런 문제들은 일관성 있게 공통으로 처리해야 한다.` `오류 로그를 남기고, 개발자가 해당 오류를 빠르게 인지하는 것이 필요하다.` 서블릿, 필터, 스프링
인터셉터,
스프링의 `ControllerAdvice` 를 사용하면, 이런 부분을 깔끔하게 공통으로 해결할 수 있다.

> **의존 관계에 대한 문제**

체크 예외의 또 다른 심각한 문제는 예외에 대한 의존 관계 문제이다. 앞서 `대부분의 예외는 복구 불가능한 예외` 라고 했다. 그런데 체크 예외이기 때문에 컨트롤러나 서비스
입장에서는 본인이 처리할 수 없어도 어쩔
수 없이 `throws` 를 통해 던지는 예외를 선언해야 한다.

향후 리포지토리를 JDBC 기술이 아닌 다른 기술로 변경한다면, 그래서 `SQLException` 이 아니라, 예를 들어서 `JPAException` 으로 예외가 변경된다면 어떻게
될까? `SQLException`
에 의존하던 모든 `서비스`, `컨트롤러` 의 코드를 `JPAException` 에 의존하도록 고쳐야 한다.

### 체크 예외 구현 기술 변경시, 파급 효과

<img width="839" alt="스크린샷 2022-04-20 오후 9 55 27" src="https://user-images.githubusercontent.com/23515771/164235214-9ffe32d4-187b-4a26-ad25-4b5fcd5c87f2.png">

JDBC JPA 같은 기술로 변경하면 예외도 함께 변경해야한다. 그리고 해당 예외를 던지는 모든 다음 부분도 함께 변경해야 한다.

- `logic() throws SQLException` -> `logic() throws JPAException` 로 모두 변경해야 한다.

### 정리

- 처리할 수 있는 체크 예외라면 서비스나 컨트롤러에서 처리하겠지만, 지금처럼 데이터베이스나 네트워크 통신처럼 시스템 레벨에서 올라온 예외들은 대부분 복구가 불가능하다.
  그리고 `실무에서 발생하는 대부분의 예외들은 이런 시스템 예외들이다.`
- 문제는 이런 경우에 체크 예외를 사용하면 아래에서 올라온 복구 불가능한 예외를 서비스, 컨트롤러 같은 각각의 클래스가 모두 알고 있어야 한다. 그래서 불필요한 의존관계 문제가
  발생하게 된다.

## :round_pushpin: 언체크 예외 활용

<img width="840" alt="스크린샷 2022-04-20 오후 10 04 35" src="https://user-images.githubusercontent.com/23515771/164236765-4a55a339-9272-40b8-ac41-ab1836874dcd.png">

- `SQLException` 을 런타임 예외인 `RuntimeSQLException` 으로 변환했다.
- `ConnectException` 대신에 `RuntimeConnectException` 을 사용하도록 바꾸었다.
- 런타임 예외이기 때문에 서비스, 컨트롤러는 해당 예외들을 처리할 수 없다면 별도의 선언 없이 그냥 두면 된다.

### 런타임 예외 - 대부분 복구 불가능한 예외

시스템에서 발생한 예외는 대부분 복구 불가능 예외이다. 런타임 예외를 사용하면 서비스나 컨트롤러가 이런 복구 불가능한 예외를 신경쓰지 않아도 된다. 물론 이렇게 복구 불가능한
예외는 일관성 있게 공통으로 처리해야
한다.

### 런타임 예외 - 의존 관계에 대한 문제

런타임 예외는 해당 객체가 처리할 수 없는 예외는 무시하면 된다. 따라서 체크 예외 처럼 예외를 강제로 의존하지 않아도 된다.

### 언체크 예외 구현 기술 변경시, 파급 효과

<img width="839" alt="스크린샷 2022-04-20 오후 10 13 43" src="https://user-images.githubusercontent.com/23515771/164238388-12eb94c5-6417-48e8-998b-7cf9041c317a.png">

런타임 예외를 사용하면 `중간에 기술이 변경` 되어도 `해당 예외를 사용하지 않는 컨트롤러, 서비스` 에서는 코드를 변경하지 않아도 된다.

구현 기술이 변경되는 경우, `예외를 공통으로 처리하는 곳` 에서는 `예외에 따른 다른 처리가 필요할 수 있다.`
하지만 `공통 처리하는 한곳만 변경하면 되기 때문에 변경의 영향 범위는 최소화 된다.`

### 정리

처음 자바를 설계할 당시에는 체크 예외가 더 나은 선택이라 생각했다. 그래서 자바가 기본으로 제공하는 기능들에는 체크 예외가 많다. 그런데 시간이 흐르면서 복구 할 수 없는 예외가
너무 많아졌다. 특히 라이브러리를
점점 더 많이 사용하면서 처리해야 하는 예외도 더 늘어났다. 체크
예외는 `해당 라이브러리들이 제공하는 모든 예외를 처리할 수 없을 때마다 throws 에 예외를 덕지덕지 붙어야 했다.`
그래서 `개발자들은 throws Exception 이라는 극단적?인 방법도 자주 사용하게 되었다.` 물론 `이 방법은 사용하면 안된다.` 모든 예외를 던진다고 선언하는 것인데,
결과적으로 `어떤 예외를 잡고 어떤 예외를 던지는지 알 수 없기 때문이다.` `체크 예외를 사용한다면 잡을 건 잡고 던질 예외는 명확하게 던지도록 선언해야 한다.` 체크 예외의
이런 문제점
때문에 `최근 라이브러리들은 대부분 런타임 예외를 기본으로 제공한다.` 사실 위에서 예시로 설명한 `JPA` 기술도 `런타임 예외` 를 사용한다. `스프링`
도 `대부분 런타임 예외` 를 제공한다. 런타임 예외도
필요하면 잡을 수 있기 때문에 필요한 경우에는 잡아서 처리하고, 그렇지 않으면 자연스럽게 던지도록 둔다. 그리고 `예외를 공통으로 처리하는 부분을 앞에 만들어서 처리하면 된다.`

- 참고) 그래서 `Kotlin` 에서는 `throws` 가 없다. 즉, `메소드가 던지는 예외를 지정하지 않고`, 발생한 예외를 잡아도 되고, 잡아내지 않아도 된다.

### 런타임 예외는 문서화

- 런타임 예외는 문서화를 잘해야 한다.
- 또는 코드에 `throws 런타임 예외` 을 남겨서 중요한 예외를 인지할 수 있게 해준다.

> JPA EntityManager

```java
public interface EntityManager {

    /**
     * Make an instance managed and persistent.
     * @param entity  entity instance
     * @throws EntityExistsException if the entity already exists.
     * @throws IllegalArgumentException if the instance is not an
     *         entity
     * @throws TransactionRequiredException if there is no transaction when
     *         invoked on a container-managed entity manager of that is of type
     *         <code>PersistenceContextType.TRANSACTION</code>
     */
    void persist(Object entity);
}
```

> 스프링 JdbcTemplate

```java
public class JdbcTemplate {

    /**
     * Issue a single SQL execute, typically a DDL statement.
     * @param sql static SQL to execute
     * @throws DataAccessException if there is any problem
     */
    void execute(String sql) throws DataAccessException;
}
```

## :round_pushpin: 예외 포함과 스택 트레이스

예외를 전환할 때는 꼭! 기존 예외를 포함해야 한다. 그렇지 않으면, 스택 트레이스를 확인할 때, 심각한 문제가 발생한다.

### 기존 예외를 포함하지 않는 경우

```java
public class Repository {

    public void call() {
        try {
            runSQL();
        } catch (SQLException e) {
            throw new RuntimeSQLException(); //기존 예외(e) 제외
        }
    }

    public void runSQL() throws SQLException {
        throw new SQLException("ex");
    }
}
```

```
[Test worker] INFO hello.jdbc.exception.basic.UncheckedAppTest - ex
  hello.jdbc.exception.basic.UncheckedAppTest$RuntimeSQLException: null
    at
  hello.jdbc.exception.basic.UncheckedAppTest$Repository.call(UncheckedAppTest.ja
  va:61)
    at
  hello.jdbc.exception.basic.UncheckedAppTest$Service.logic(UncheckedAppTest.java
  :45)
```

예외를 포함하지 않아서 기존에 발생한 `java.sql.SQLException` 과 `스택 트레이스` 를 확인할 수 없다. 변환한 `RuntimeSQLException` 부터
예외를 확인할 수 있다. 만약 실제
DB에 연동했다면 DB에서 발생한 예외를 확인할 수 없는 심각한 문제가 발생한다. **`예외를 전환할 때는 꼭! 기존 예외를 포함하자`**

## :round_pushpin: 스프링 예외 추상화 이해

<img width="820" alt="스크린샷 2022-04-22 오후 9 01 11" src="https://user-images.githubusercontent.com/23515771/164710406-863a74a4-ba89-4ff8-a50f-e9aa8235ba57.png">

> 참고로 그림을 단순화 하기 위해 일부 계층을 생략했다.

- 스프링은 데이터 접근 계층에 대한 수십 가지 예외를 정리해서 일관된 예외 계층을 제공한다.
- 각각의 예외는 특정 기술에 종속적이지 않게 설계되어 있다. 따라서 서비스 계층에서도 스프링이 제공하는 예외를 사용하면 된다. 예를 들어서 JDBC 기술을 사용하든, JPA
  기술을 사용하든 스프링이 제공하는 예외를 사용하면 된다.
- JDBC나 JPA를 사용할 때 발생하는 예외를 스프링이 제공하는 예외로 변환해주는 역할도 스프링이 제공한다.
- 예외의 최고 상위는 `org.springframework.dao.DataAccessException` 이다. 그림에서 보는 것 처럼 런타임 예외를 상속 받았기 때문에 스프링이
  제공하는 데이터 접근 계층의 모든 예외는 런타임 예외이다.
- `DataAccessException` 은 크게 2가지로 구분하는데 `NonTransient` 예외와 `Transient` 예외이다.
    - `Transient` 는 `일시적` 이라는 뜻이다. `Transient` 하위 예외는 `동일한 SQL을 다시 시도했을 때,
      성공할 가능성이 있다.`
        - 예를 들어서 쿼리 타임아웃, 락과 관련된 오류들이다. 이런 오류들은 데이터베이스 상태가 좋아지거나, 락이 풀렸을 때 다시 시도하면 성공할 수 도 있다.
    - `NonTransient` 는 `일시적이지 않다는` 뜻이다. 같은 SQL을 그대로 반복해서 실행하면 실패한다.
        - SQL 문법 오류, 데이터베이스 제약조건 위배 등이 있다.
