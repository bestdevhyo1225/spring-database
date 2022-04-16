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

JDBC(Java Database Connectivity)는 자바에서 데이터베이스에 접속할 수 있도록 하는 자바 API다. 대표적으로 다음 3가지 기능을 표준 인터페이스로 정의해서 제공한다.

- `java.sql.Connection` - 연결
- `java.sql.Statement` - SQL을 담은 내용
- `java.sql.ResultSet` - SQL 요청 응답

이 JDBC 인터페이스를 각각의 DB 벤더에서 자신의 DB에 맞도록 구현해서 라이브러리를 제공하는데, 이것을 **`JDBC 드라이버`** 라고 한다.

<img width="1704" alt="image" src="https://user-images.githubusercontent.com/23515771/163669304-058ccd6a-d793-4b49-baec-46dfcc6e7f40.png">

`애플리케이션 로직` 에서는 `JDBC 표준 인터페이스` 를 그대로 사용하면 되고, `드라이버` 구현체만 바꾸면 된다. (개인적인 생각으로 **`전략 패턴`** 을 기반으로 구현한 것 같다.)

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
아니라 **`커넥션을 새로 만드는 시간이 추가`** 되기 때문에 **`응답 속도에 영향을 준다.`** 이러한 문제를 한 번에 해결하는 아이디어가 커넥션을 미리 생성해두고 사용하는 **`커넥션 풀`** 이라는
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
- 성능과 사용의 편리함 측면에서 최근에는 `HikariCP` 를 주로 사용한다. 스프링 부트 2.0 부터는 기본 커넥션 풀로 `HikariCP` 를 제공한다. 성능, 사용의 편리함, 안전성 측면에서 이미 검증이
  되었기 때문에 커넥션 풀을 사용할 때는 고민할 것 없이 `HikariCP` 를 사용하면 된다. 실무에서도 레거시 프로젝트가 아닌 이상 대부분 `HikariCP` 를 사용한다.

## :round_pushpin: DataSource 이해

### DriverManager를 통해 커넥션 획득하다가 커넥션 풀로 변경시 문제

![스크린샷 2022-04-16 오후 11 33 21](https://user-images.githubusercontent.com/23515771/163679073-5a373344-abaf-40ac-a138-93ae88b6d323.png)

DriverManager를 사용하다가 HikariCP를 사용하게 되면, 애플리케이션 코드가 변경되는 상황이 발생하게 된다. 왜냐하면 의존관계가 `DriverManager` 에서 `HikariCP` 로 변경되기
때문이다.

### 커넥션 획득하는 방법을 추상화

<img width="839" alt="스크린샷 2022-04-16 오후 11 35 45" src="https://user-images.githubusercontent.com/23515771/163679123-8f8bf71d-a6d5-4d45-9ee3-8f8204f9532a.png">

자바에서는 이러한 문제를 해결하기 위해서 `java.sql.DataSource` 라는 인터페이스를 제공한다. `DataSource` 는 `커넥션을 획득하는 방법을 추상화한 인터페이스` 이다.

```java
public interface DataSource {
    Connection getConnection() throws SQLException;
}
```
